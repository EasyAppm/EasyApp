package com.easyapp.net.http;
import com.easyapp.net.http.entity.Auth;
import com.easyapp.net.http.entity.Body;
import com.easyapp.net.http.entity.Header;
import com.easyapp.net.http.entity.Request;

public final class Client extends URLHandler{

    private Auth auth;
    private Integer timeOut;

    private Client(String url){
        super(url);
    }

    public static Client with(String url){
        return new Client(url);
    }

    public Client useAuth(Auth auth){
        this.auth = auth;
        return this;
    }

    public Client useTimeOut(int timeOut){
        this.timeOut = timeOut;
        return this;
    }

    public boolean isUsingAuth(){
        return auth != null;
    }

    public boolean isUsingTimeOut(){
        return timeOut != null;
    }

    public String getUrl(){
        return toString();
    }

    /**GET**/

    public AsyncConnection get(){
        return createAsyncConnection(
            Request.get(getUrl()).build()
        );
    }

    public AsyncConnection get(Header header){
        return createAsyncConnection(
            Request.get(getUrl())
            .setHeader(header)
            .build()
        );
    }

    /**HEAD**/

    public AsyncConnection head(){
        return createAsyncConnection(
            Request.head(getUrl()).build()
        );
    }

    public AsyncConnection head(Header header){
        return createAsyncConnection(
            Request.head(getUrl())
            .setHeader(header)
            .build()
            
        );
    }

    /**POST**/

    public AsyncConnection post(Body body){
        return createAsyncConnection(
            Request.post(getUrl(), body).build()
        );
    }

    public AsyncConnection post(Body body, Header header){
        return createAsyncConnection(
            Request.post(getUrl(), body)
            .setHeader(header)
            .build()
        );
    }

    /**PUT**/

    public AsyncConnection put(Body body){
        return createAsyncConnection(
            Request.put(getUrl(), body).build()
        );
    }

    public AsyncConnection put(Body body, Header header){
        return createAsyncConnection(
            Request.put(getUrl(), body)
            .setHeader(header)
            .build()
        );
    }

    /**PATCH**/

    public AsyncConnection patch(Body body){
        return createAsyncConnection(
            Request.post(getUrl(), body).build()
        );
    }

    public AsyncConnection patch(Body body, Header header){
        return createAsyncConnection(
            Request.post(getUrl(), body)
            .setHeader(header)
            .build()
        );
    }

    /**DELETE**/

    public AsyncConnection delete(){
        return createAsyncConnection(
            Request.head(getUrl()).build()
        );
    }

    public AsyncConnection delete(Body body){
        return createAsyncConnection(
            Request.head(getUrl())
            .setBody(body)
            .build()
        );
    }

    public AsyncConnection delete(Body body, Header header){
        return createAsyncConnection(
            Request.head(getUrl())
            .setBody(body)
            .setHeader(header)
            .build()
        );
    }

    /**OPTIONS**/


    public AsyncConnection options(){
        return createAsyncConnection(
            Request.options(getUrl()).build()
        );
    }

    public AsyncConnection options(Body body){
        return createAsyncConnection(
            Request.options(getUrl())
            .setBody(body)
            .build()
        );
    }

    public AsyncConnection options(Body body, Header header){
        return createAsyncConnection(
            Request.options(getUrl())
            .setBody(body)
            .setHeader(header)
            .build()
        );
    }

    /**TRACE**/

    public AsyncConnection trace(){
        return createAsyncConnection(
            Request.trace(getUrl()).build()
        );
    }

    public AsyncConnection trace(Body body){
        return createAsyncConnection(
            Request.trace(getUrl())
            .setBody(body)
            .build()
        );
    }

    public AsyncConnection trace(Body body, Header header){
        return createAsyncConnection(
            Request.trace(getUrl())
            .setBody(body)
            .setHeader(header)
            .build()
        );
    }

    private AsyncConnection createAsyncConnection(Request request){
        Request.Builder builder = null;
        if(isUsingAuth()){
            builder = new Request.Builder(request)
                .setHeader(new Header.Builder(request.getHeader())
                .putAuth(auth)
                .build());
        }
        if(isUsingTimeOut()){
            builder = new Request.Builder(request)
                .setTimeOut(timeOut);
        }
        return AsyncConnection.open(builder == null ? request : builder.build());
    }
}
