package org.auctions.sf57.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.dto.AuctionDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by vladimir_antin on 8.5.17..
 */

@Entity
@Table(name="auctions")
public class Auction implements Serializable {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="auction_id", unique=true, nullable=false)
    private long id;

    @Column(name="startPrice", nullable=false)
    private float startPrice;

    @Column(name="startDate", nullable=false)
    private Date startDate;

    @Column(name="endDate")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name="item_id",referencedColumnName = "item_id",nullable = false)
    @JsonBackReference
    private Item item;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="auction")
    @JsonManagedReference
    private Set<Bid> bids = new HashSet<>();

    public Auction(){}

    public Auction fromDTO(AuctionDTO auctionDTO) {
        this.startPrice= auctionDTO.getStartPrice();
        try {
            this.startDate = Sf57Utils.jsFormat.parse(auctionDTO.getStartDate());
        } catch (ParseException e) {
            this.startDate=null;
        }
        try {
            this.endDate = Sf57Utils.jsFormat.parse(auctionDTO.getEndDate());
        } catch (ParseException e) {
            this.endDate=null;
        }
        return this;
    }
    public long getId() {
        return id;
    }

    public Auction setId(long id) {
        this.id = id;
        return this;
    }

    public float getStartPrice() {
        return startPrice;
    }

    public Auction setStartPrice(float startPrice) {
        this.startPrice = startPrice;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Auction setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Auction setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Item getItem() {
        return item;
    }

    public Auction setItem(Item item) {
        this.item = item;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Auction setUser(User user) {
        this.user = user;
        return this;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public Auction setBids(Set<Bid> bids) {
        this.bids = bids;
        return this;
    }
}
