package com.easyapp.core;

public class TypeInstance{

    public static boolean isByte(Object object){
        return object instanceof Byte;
    }

    public static boolean isShort(Object object){
        return object instanceof Short;
    }

    public static boolean isInteger(Object object){
        return object instanceof Integer;
    }

    public static boolean isLong(Object object){
        return object instanceof Long;
    }

    public static boolean isFloat(Object object){
        return object instanceof Float;
    }

    public static boolean isDouble(Object object){
        return object instanceof Double;
    }

    public static boolean isCharacter(Object object){
        return object instanceof Character;
    }

    public static boolean isString(Object object){
        return object instanceof String;
    }

    public static boolean isNumber(Object object){
        return object instanceof Number;
    }

    public static Byte toByte(Object object){
        return toByte(object, (byte)0);
    }

    public static Short toShort(Object object){
        return toShort(object, (short)0);
    }

    public static Integer toInteger(Object object){
        return toInteger(object, 0);
    }

    public static Long toLong(Object object){
        return toLong(object, (long)0);
    }

    public static Float toFloat(Object object){
        return toFloat(object, (float)0);
    }

    public static Double toDouble(Object object){
        return toDouble(object, (double)0);
    }

    public static String toString(Object object){
        return toString(object, "");
    }

    public static Byte toByte(Object object, Byte defValue){
        try{
            return Byte.valueOf(object.toString());
        }catch(Throwable e){
            return defValue;
        }
    }

    public static Short toShort(Object object, Short defValue){
        try{
            return Short.valueOf(object.toString());
        }catch(Throwable e){
            return defValue;
        }
    }

    public static Integer toInteger(Object object, Integer defValue){
        try{
            return Integer.valueOf(object.toString());
        }catch(Throwable e){
            return defValue;
        }
    }

    public static Long toLong(Object object, Long defValue){
        try{
            return Long.valueOf(object.toString());
        }catch(Throwable e){
            return defValue;
        }
    }

    public static Float toFloat(Object object, Float defValue){
        try{
            return Float.valueOf(object.toString());
        }catch(Throwable e){
            return defValue;
        }
    }

    public static Double toDouble(Object object, Double defValue){
        try{
            return Double.valueOf(object.toString());
        }catch(Throwable e){
            return defValue;
        }
    }


    public static String toString(Object object, String defValue){
        try{
            return object.toString();
        }catch(Throwable e){
            return defValue;
        }
    }

    


}
