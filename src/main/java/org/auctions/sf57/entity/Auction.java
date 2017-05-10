package org.auctions.sf57.entity;

import javax.persistence.*;
import java.io.Serializable;
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

    @Column(name="startPrice", unique=false, nullable=false)
    private float startPrice;

    @Column(name="startDate", unique=false, nullable=false)
    private Date startDate;

    @Column(name="endDate", unique=false, nullable=true)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name="item_id",referencedColumnName = "item_id",nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "user_id",nullable = false)
    private User user;

    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER, mappedBy="auction")
    private Set<Bid> bids = new HashSet<>();

    public Auction(){}

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
