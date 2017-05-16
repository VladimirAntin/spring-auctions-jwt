package org.auctions.sf57.config;

/**
 * Created by vladimir_antin on 16.5.17..
 */
public class Sf57Utils {

    public static boolean contains(String first,String second){
        return first.toLowerCase().contains(second);
    }

    public static long long_parser(String num){
        try{
            return Long.parseLong(num);
        }catch (Exception e){
            return  0;
        }
    }

    public static boolean validate(String name,long minLength,long maxLength){
        if(name != null && name.length()<=maxLength && name.length()>=minLength){
            return true;
        }
        return false;
    }
}
