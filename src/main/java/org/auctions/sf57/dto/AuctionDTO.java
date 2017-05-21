package org.auctions.sf57.dto;

import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Bid;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.entity.User;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vladimir_antin on 19.5.17..
 */
public class AuctionDTO implements Serializable{
    private long id;
    private float startPrice;
    private String startDate;
    private String endDate;
    private ItemDTO item;
    private UserDTO user;

    public AuctionDTO(){}

    public AuctionDTO(Auction auction){
        this.id = auction.getId();
        this.startPrice = auction.getStartPrice();
        try{
            this.startDate = Sf57Utils.sdf.format(auction.getStartDate());
        }catch (Exception e){}
        try{
            this.endDate = Sf57Utils.sdf.format(auction.getEndDate());
        }catch (Exception e){}
        this.item = new ItemDTO(auction.getItem());
        this.user = new UserDTO(auction.getUser());
    }

    public long getId() {
        return id;
    }

    public AuctionDTO setId(long id) {
        this.id = id;
        return this;
    }

    public float getStartPrice() {
        return startPrice;
    }

    public AuctionDTO setStartPrice(float startPrice) {
        this.startPrice = startPrice;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public AuctionDTO setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public AuctionDTO setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public ItemDTO getItem() {
        return item;
    }

    public AuctionDTO setItem(ItemDTO item) {
        this.item = item;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public AuctionDTO setUser(UserDTO user) {
        this.user = user;
        return this;
    }

}
