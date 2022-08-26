package com.easyapp.net.http;
import com.easyapp.net.http.annotation.*;
import com.easyapp.net.http.annotation.verbs.*;
import com.easyapp.net.http.entity.Auth;
import com.easyapp.net.http.entity.Body;
import com.easyapp.net.http.entity.Header;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.easyapp.core.TypeInstance;
import java.lang.reflect.Modifier;


public class HttpService{

    private final Client client;

    private HttpService(String url){
        if(url == null || url.isEmpty()){
            throw new IllegalArgumentException("Invalid URL."); 
        }
        this.client = Client.with(url);
    }

    public static HttpService baseURL(String url){
        return new HttpService(url);
    }

    public HttpService path(String path){
        client.addPath(path);
        return this;
    }

    public HttpService query(String key, String value){
        client.putQuery(key, value);
        return this;
    }

    public HttpService auth(Auth auth){
        client.useAuth(auth);
        return this;
    }

    public HttpService timeOut(int timeOut){
        client.useTimeOut(timeOut);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service){
        if(service == null){
            throw new IllegalArgumentException("service cannot be null");
        }
        if(!service.isInterface()){
            throw new IllegalArgumentException("service need interface");
        }
        return (T) Proxy.newProxyInstance(
            service.getClassLoader(),
            new Class<?>[]{service},
            new ResolvedVerbs(client)
        );
    }

    private interface Verbs{
        AsyncConnection get(Method method, String endPoint, Header header, Object[] args);
        AsyncConnection put(Method method, String endPoint, Header header, Object[] args) throws Exception;
        AsyncConnection post(Method method, String endPoint, Header header, Object[] args) throws Exception;
        AsyncConnection delete(Method method, String endPoint, Header header, Object[] args) throws Exception;
        AsyncConnection head(Method method, String endPoint, Header header, Object[] args);
        AsyncConnection options(Method method, String endPoint, Header header, Object[] args) throws Exception;
        AsyncConnection patch(Method method, String endPoint, Header header, Object[] args) throws Exception;
    }

    private static class ResolvedVerbs implements Verbs, InvocationHandler{

        private static final String PATTERN_INDEX = "\\{[0-9]+\\}";
        private static final String DOTS_SEPARATE = ":";
        private final Client client;

        public ResolvedVerbs(Client client){
            this.client = client;
        }

        @Override
        public Object invoke(Object object, Method method, Object[] args) throws Throwable{
            if(validateMethod(method)){
                Header.Builder header = Header.builder();
                //EncodeQuery
                if(method.isAnnotationPresent(EncodeQuery.class)){
                    String[] values = method.getDeclaredAnnotation(EncodeQuery.class).value();
                    Map<String, String> queries = resolveEncode(method, values, args);
                    for(String key : queries.keySet()){
                        client.putQuery(key, queries.get(key));
                    }
                }
                //EncodeHeader
                if(method.isAnnotationPresent(EncodeHeader.class)){
                    String[] values = method.getDeclaredAnnotation(EncodeHeader.class).value();
                    Map<String, String> queries = resolveEncode(method, values, args);
                    for(String key : queries.keySet()){
                        header.put(key, queries.get(key));
                    }
                }
                //ResolvedVerbs
                if(method.isAnnotationPresent(GET.class)){
                    return get(method, method.getAnnotation(GET.class).value(), header.build(), args);
                }else if(method.isAnnotationPresent(PUT.class)){
                    return put(method, method.getAnnotation(PUT.class).value(), header.build(), args);
                }else if(method.isAnnotationPresent(POST.class)){
                    return post(method, method.getAnnotation(POST.class).value(), header.build(), args);
                }else if(method.isAnnotationPresent(DELETE.class)){
                    return delete(method, method.getAnnotation(DELETE.class).value(), header.build(), args);
                }else if(method.isAnnotationPresent(HEAD.class)){
                    return head(method, method.getAnnotation(HEAD.class).value(), header.build(), args);
                }else if(method.isAnnotationPresent(OPTIONS.class)){
                    return options(method, method.getAnnotation(OPTIONS.class).value(), header.build(), args);
                }else if(method.isAnnotationPresent(PATCH.class)){
                    return patch(method, method.getAnnotation(PATCH.class).value(), header.build(), args);
                }else{
                    throw new UnsupportedOperationException("Method unknown");
                }
            }else{
                return method.invoke(object, args);
            }
        }


        @Override
        public AsyncConnection get(Method method, String endPoint, Header header, Object[] args){
            client.addPath(createPath(endPoint, method, args));
            return client.get(header);
        }

        @Override
        public AsyncConnection put(Method method, String endPoint, Header header, Object[] args) throws Exception{
            client.addPath(createPath(endPoint, method, args));
            Body body = createBody(
                method.getDeclaredAnnotation(PUT.class).body(),
                method.getParameterTypes(),
                args
            );
            return client.put(body, header);
        }

        @Override
        public AsyncConnection post(Method method, String endPoint, Header header, Object[] args) throws Exception{
            client.addPath(createPath(endPoint, method, args));
            Body body = createBody(
                method.getDeclaredAnnotation(POST.class).body(),
                method.getParameterTypes(),
                args
            );
            return client.post(body, header);
        }

        @Override
        public AsyncConnection delete(Method method, String endPoint, Header header, Object[] args) throws Exception{
            client.addPath(createPath(endPoint, method, args));
            Body body = createBody(
                method.getDeclaredAnnotation(DELETE.class).body(),
                method.getParameterTypes(),
                args
            );
            return client.delete(body, header);
        }

        @Override
        public AsyncConnection head(Method method, String endPoint, Header header, Object[] args){
            client.addPath(createPath(endPoint, method, args));
            return client.head(header);
        }

        @Override
        public AsyncConnection options(Method method, String endPoint, Header header, Object[] args) throws Exception{
            client.addPath(createPath(endPoint, method, args));
            Body body = createBody(
                method.getDeclaredAnnotation(OPTIONS.class).body(),
                method.getParameterTypes(),
                args
            );
            return client.options(body, header);
        }

        @Override
        public AsyncConnection patch(Method method, String endPoint, Header header, Object[] args) throws Exception{
            client.addPath(createPath(endPoint, method, args));
            Body body = createBody(
                method.getDeclaredAnnotation(PATCH.class).body(),
                method.getParameterTypes(),
                args
            );
            return client.patch(body, header);
        }


        private String createPath(String endPoint, Method method, Object[] args){
            if(args == null || args.length == 0) return endPoint;
            Matcher matcher = Pattern.compile(PATTERN_INDEX).matcher(endPoint);
            while(matcher.find()){
                String group = matcher.group();
                int pos = Integer.valueOf(group.substring(1, group.length() - 1));
                if(pos >= args.length){
                    throw new IndexOutOfBoundsException(
                        "Path position "
                        + pos + " is outside the parameters range of the " 
                        + method.getName() + " method"
                    );
                }else{
                    String value = getArgumentValueToString(pos, args); //getArgumentValue(method, String.class, pos, args);
                    if(value != null){
                        endPoint = endPoint.replace(group, value);
                    }
                }
            }
            return endPoint;
        }

        private Body createBody(int position, Class<?>[] typeArgs, Object[] args) throws FileNotFoundException{
            if(position < 0){
                return null;
            }else if(args == null || typeArgs == null){
                throw new UnsupportedOperationException("Cannot create body in a method with no arguments");
            }else if(position >= args.length){
                throw new IndexOutOfBoundsException(
                    "Body position "
                    + position + " is outside the maximum limit of " 
                    + args.length
                ); 
            }
            Class<?> type = typeArgs[position];
            if(type == String.class){
                return Body.create((String)args[position]);
            }else if(type == File.class){
                return Body.create((File)args[position]);
            }else if(type == InputStream.class){
                return Body.create((InputStream)args[position]);
            }else if(args[position] instanceof byte[] || args[position] instanceof Byte[]){
                return Body.create((byte[])args[position]);
            }else{
                throw new UnsupportedOperationException("Cannot create a body with " + type.getSimpleName());
            }
        }

        private Map<String, String> resolveEncode(Method method, String[] encodeValues, Object[] args){
            Map<String, String> map = new LinkedHashMap<>();
            if(encodeValues.length == 0){
                return map;
            }else{
                for(String encodeValue : encodeValues){
                    String[] values = encodeValue.contains(DOTS_SEPARATE) ? encodeValue.split(DOTS_SEPARATE) : null;
                    if(values != null){
                        String key = values[0].trim();
                        String value = values[1].trim();
                        if(!key.isEmpty() && !value.isEmpty()){
                            if(value.matches(PATTERN_INDEX)){
                                int pos = Integer.valueOf(value.substring(1, value.length() - 1));
                                value = getArgumentValueToString(pos, args); //getArgumentValue(method, String.class, pos, args);
                                if(value != null) map.put(key, value);
                            }else{
                                map.put(key, value);
                            }
                        }
                    }
                }
            }
            return map;
        }

        /**
         * Verifica se o metodo tem como retorno a
         * AsyncConnection, e se contém algum verbo http
         **/
        private boolean validateMethod(Method method){
            final Class<? extends Annotation>[] annotations = new Class[]{
                GET.class, POST.class, PUT.class,
                DELETE.class, PATCH.class, OPTIONS.class
            };
            if(method.getReturnType().getSimpleName().equals(AsyncConnection.class.getSimpleName())){
                for(Class<? extends Annotation> verb : annotations){
                    if(method.isAnnotationPresent(verb)) return true;
                }
            }
            return false;
        }

        /**
         * Retorna o valor de um argumento
         * pelo tipo esperado e posição
         **/
        private <T> T getArgumentValue(Method method, Class<T> expectedType, int pos, Object[] args){
            if(args != null && (args.length > 0 && pos < args.length)){
                Class<?>[] types = method.getParameterTypes();
                if(types[pos] == expectedType) return (T) args[pos];
            }
            return null;
        }

        /**
         * Retorna o valor de um argumento
         * convertido para string, apenas se
         * o mesmo tiver o metodo toString declarado
         **/
        private String getArgumentValueToString(int pos, Object[] args){
            if(args != null && (args.length > 0 && pos < args.length)){
                for(Method method : args[pos].getClass().getDeclaredMethods()){
                    if(!Modifier.isPublic(method.getModifiers()) && method.getParameterCount() != 0) 
                        continue;
                    else if(method.getReturnType() == String.class && method.getName().equals("toString")){
                        return TypeInstance.toString(args[pos], null);
                    }
                }
            }
            return null;
        }
    }


}
