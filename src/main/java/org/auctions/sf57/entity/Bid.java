package org.auctions.sf57.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by vladimir_antin on 8.5.17..
 */
@Entity
@Table(name="bids")
public class Bid implements Serializable {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="bid_id", unique=true, nullable=false)
    private long id;

    @Column(name="price", unique=false, nullable=false)
    private float price;

    @Column(name="dateTime", unique=false, nullable=false)
    private Date dateTime;

    @ManyToOne
    @JoinColumn(name="auction_id",referencedColumnName = "auction_id",nullable = false)
    @JsonBackReference
    private Auction auction;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    public Bid(){}

    public long getId() {
        return id;
    }

    public Bid setId(long id) {
        this.id = id;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public Bid setPrice(float price) {
        this.price = price;
        return this;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public Bid setDateTime(Date dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public Auction getAuction() {
        return auction;
    }

    public Bid setAuction(Auction auction) {
        this.auction = auction;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Bid setUser(User user) {
        this.user = user;
        return this;
    }
}
