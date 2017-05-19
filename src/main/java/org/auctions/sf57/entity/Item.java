package org.auctions.sf57.entity;

import org.auctions.sf57.dto.ItemDTO;
import org.auctions.sf57.dto.UserDTO;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by vladimir_antin on 8.5.17..
 */
@Entity
@Table(name="items")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="item_id", unique=true, nullable=false)
    private long id;

    @Column(name="name", unique=false, nullable=false)
    @Size(max = 30)
    private String name;

    @Column(name="description", unique=false, nullable=false, columnDefinition = "TEXT")
    private String description;

    @Column(name="picture", unique=false, nullable=true, columnDefinition = "TEXT")
    private String picture;

    @Column(name="sold", unique=false, nullable=false)
    private boolean sold;

    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER, mappedBy="item")
    private Set<Auction> auctions = new HashSet<>();

    public Item(){}

    public Item fromDTO(ItemDTO itemDTO) {
        this.name = itemDTO.getName();
        this.description = itemDTO.getDescription();
        this.picture = itemDTO.getPicture();
        this.sold = itemDTO.isSold();
        this.auctions = itemDTO.getAuctions();
        return this;
    }

    public long getId() {
        return id;
    }

    public Item setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public Item setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public boolean isSold() {
        return sold;
    }

    public Item setSold(boolean sold) {
        this.sold = sold;
        return this;
    }

    public Set<Auction> getAuctions() {
        return auctions;
    }

    public Item setAuctions(Set<Auction> auctions) {
        this.auctions = auctions;
        return this;
    }
}
