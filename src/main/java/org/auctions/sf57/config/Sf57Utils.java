package org.auctions.sf57.config;

import org.auctions.sf57.dto.AuctionDTO;
import org.auctions.sf57.dto.BidDTO;
import org.auctions.sf57.dto.ItemDTO;
import org.auctions.sf57.dto.UserDTO;
import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Bid;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by vladimir_antin on 16.5.17..
 */
public class Sf57Utils {

    public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat jsFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

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
    public static boolean photo_formats(String filename){
        if(!contains(filename,".png") &&
                !contains(filename,".jpg") &&
                !contains(filename,".jpeg") &&
                !contains(filename,".gif")){
            return true;
        }
        return false;
    }

    public static boolean validate(String name,long minLength,long maxLength){
        if(name != null && name.length()<=maxLength && name.length()>=minLength){
            return true;
        }
        return false;
    }

    public static List<UserDTO> usersToDTO(Collection<User> users){
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user:users) {
            usersDTO.add(new UserDTO(user));
        }
        return usersDTO;
    }

    public static List<ItemDTO> itemsToDTO(Collection<Item> items){
        List<ItemDTO> itemsDTO = new ArrayList<>();
        for (Item item:items) {
            itemsDTO.add(new ItemDTO(item));
        }
        return itemsDTO;
    }
    public static List<AuctionDTO> auctionsToDTO(Collection<Auction> auctions){
        List<AuctionDTO> auctionDTO = new ArrayList<>();
        for (Auction auction:auctions) {
            auctionDTO.add(new AuctionDTO(auction));
        }
        return auctionDTO;
    }

    public static List<BidDTO> bidsToDTO(Collection<Bid> bids){
        List<BidDTO> bidDTO = new ArrayList<>();
        for (Bid bid:bids) {
            bidDTO.add(new BidDTO(bid));
        }
        return bidDTO;
    }

}
