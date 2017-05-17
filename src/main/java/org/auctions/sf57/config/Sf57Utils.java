package org.auctions.sf57.config;

import org.auctions.sf57.dto.ItemDTO;
import org.auctions.sf57.dto.UserDTO;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.entity.User;

import java.util.ArrayList;
import java.util.List;

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

    public static List<UserDTO> usersToDTO(List<User> users){
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user:users) {
            usersDTO.add(new UserDTO(user));
        }
        return usersDTO;
    }

    public static List<ItemDTO> itemsToDTO(List<Item> items){
        List<ItemDTO> itemsDTO = new ArrayList<>();
        for (Item item:items) {
            itemsDTO.add(new ItemDTO(item));
        }
        return itemsDTO;
    }

}
