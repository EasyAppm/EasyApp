package com.easyapp.util;

import android.content.Context;

import com.easyapp.core.TypeValidator;

import java.lang.reflect.Method;

public class ReflectionUtils{

    private ReflectionUtils(){}

    public static boolean isSubClass(String classEntryName, String...superClassNames) throws ClassNotFoundException{
        return isSubClass(null, classEntryName, superClassNames);
    }

    public static boolean isSubClass(ClassLoader loader, String classEntryName, String... superClassNames) throws ClassNotFoundException{
        if(classEntryName == null){
            throw new IllegalArgumentException("classEntryName cannot be null");
        }
        if(superClassNames == null || superClassNames.length == 0){
            throw new IllegalArgumentException("superClasseNames cannot be null or empty");
        }
        Class<?> classEntry;
        Class<?>[] superClasses = new Class<?>[superClassNames.length];
        if(loader == null){
            classEntry = loadClass(classEntryName);
            for(int i = 0; i < superClasses.length; i++){
                superClasses [i] = loadClass(superClassNames [i]);
            }
        }else{
            classEntry = loadClass(loader, classEntryName, false);
            for(int i = 0; i < superClasses.length; i++){
                superClasses [i] = loadClass(loader, superClassNames [i], false);
            }
        }
        return isSubClass(classEntry, superClasses);
    }

    public static boolean isSubClass(Class<?> classEntry, Class<?> superClass){
        return isSubClass(classEntry, new Class[]{superClass});
    }

    public static boolean isSubClass(Class<?> classEntry, Class<?>... superClasses){
        if(classEntry == null){
            throw new IllegalArgumentException("classEntry cannot be null");
        }
        if(superClasses == null || superClasses.length == 0){
            throw new IllegalArgumentException("superClasses cannot be null or empty");
        }
        for(Class<?> superClass : superClasses){
            if(classEntry.getSuperclass().equals(superClass)){
                return true;
            }
        }
        return false;
    }

    public static Class<?> loadClassInContext(Context context, String className) throws ClassNotFoundException{
        return loadClassInContext(context, className, false);
    }

    public static Class<?> loadClassInContext(Context context, String className, boolean initialize) throws ClassNotFoundException{
        if(context == null){
            throw new IllegalArgumentException("Context cannot be null");
        }
        ClassLoader loader = context.getClassLoader();
        if(loader == null){
            throw new ClassNotFoundException(className + " not found because the context class loader is null");
        }
        return loadClass(loader, className, initialize);
    }

    public static Class<?>[] loadClasses(String... classNames) throws ClassNotFoundException{
        return loadClasses(null, false, classNames);
    }

    public static Class<?>[] loadClasses(ClassLoader loader, String... classNames) throws ClassNotFoundException{
        return loadClasses(loader, false, classNames);
    }

    public static Class<?>[] loadClasses(ClassLoader classLoader, boolean initialize, String... classNames) throws ClassNotFoundException{
        if(classNames == null){
            throw new IllegalArgumentException("classNames cannot be null");
        }
        int length = classNames.length;
        Class<?>[] classes = new Class<?>[length];
        for(int i = 0; i < length; i++){
            String className = classNames [i];
            if(classLoader == null){
                classes [i] = Class.forName(className);
            }else{
                classes [i] = Class.forName(className, initialize, classLoader);
            }
        }
        return classes;
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException{
        if(className == null){
            throw new IllegalArgumentException("className cannot be null");
        }
        return Class.forName(className);
    }

    public static Class<?> loadClass(ClassLoader loader, String className, boolean initialize) throws ClassNotFoundException{
        if(className == null){
            throw new IllegalArgumentException("className cannot be null");
        }
        return Class.forName(className, initialize, loader);
    }

    public static boolean isInterface(ClassLoader loader, String classEntryName) throws ClassNotFoundException {
        return isInterface(loadClass(loader, classEntryName, false));
    }

    public static boolean isInterface(Class<?> classEntry){
        return notNull(classEntry, "classEntry").isInterface();
    }

    public static String getName(Class<?> classEntry){
        return notNull(classEntry, "classEntry").getName();
    }

    public static String getSimpleName(Class<?> classEntry){
        return notNull(classEntry, "classEntry").getSimpleName();
    }

    public static Method[] getMethods(Class<?> classEntry){
        return notNull(classEntry, "classEntry").getMethods();
    }

    public static Method getMethod(Class<?> classEntry, String name, Class<?>... argumentsTypes) throws NoSuchMethodException {
        return notNull(classEntry, "classEntry").getMethod(name, argumentsTypes);
    }

    private static <T> T notNull(T object, String name){
        return TypeValidator.argumentNonNull(object, name + " cannot be null.");
    }

}
