package utils;

import java.util.Set;

public class MyHelper {
    private MyHelper(){

    }
    public static String setStr(Set<? extends  Number> numbers){
        String x = numbers.toString();
        x = x.replace("[","{");
        x = x.replace("]","}");
        return x;

    }
}
