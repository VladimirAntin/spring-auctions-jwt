package org.auctions.sf57.dto;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Item;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vladimir_antin on 17.5.17..
 */
public class ItemDTO implements Serializable {

    private long id;
    private String name;
    private String description;
    private String picture;
    private boolean sold;
    private Set<Auction> auctions = new HashSet<>();

    public ItemDTO(){}

    public ItemDTO(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.picture = item.getPicture();
        this.sold = item.isSold();
        this.auctions = item.getAuctions();
    }

    public long getId() {
        return id;
    }

    public ItemDTO setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public ItemDTO setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public boolean isSold() {
        return sold;
    }

    public ItemDTO setSold(boolean sold) {
        this.sold = sold;
        return this;
    }

    public Set<Auction> getAuctions() {
        return auctions;
    }

    public ItemDTO setAuctions(Set<Auction> auctions) {
        this.auctions = auctions;
        return this;
    }
}
