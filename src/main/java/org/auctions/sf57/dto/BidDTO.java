package org.auctions.sf57.dto;

import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.entity.Bid;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vladimir_antin on 22.5.17..
 */
public class BidDTO implements Serializable {

    private long id;
    private float price;
    private String dateTime;
    private AuctionDTO auction;
    private UserDTO user;

    public BidDTO(){}

    public BidDTO(Bid bid){
        this.id = bid.getId();
        this.price = bid.getPrice();
        try{
            this.dateTime = Sf57Utils.sdf.format(bid.getDateTime());
        }catch (Exception e){}
        this.auction = new AuctionDTO(bid.getAuction());
        this.user = new UserDTO(bid.getUser());
    }

    public long getId() {
        return id;
    }

    public BidDTO setId(long id) {
        this.id = id;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public BidDTO setPrice(float price) {
        this.price = price;
        return this;
    }

    public String getDateTime() {
        return dateTime;
    }

    public BidDTO setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public AuctionDTO getAuction() {
        return auction;
    }

    public BidDTO setAuction(AuctionDTO auction) {
        this.auction = auction;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public BidDTO setUser(UserDTO user) {
        this.user = user;
        return this;
    }
}
