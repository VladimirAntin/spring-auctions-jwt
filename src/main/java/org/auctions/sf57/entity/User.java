package org.auctions.sf57.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.auctions.sf57.dto.UserDTO;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by vladimir_antin on 8.5.17..
 */

@Entity
@Table(name="users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private long id;

    @Column(name = "name", unique = false, nullable = false)
    @Size(max = 30)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    @Size(max = 30)
    private String email;

    @Column(name = "password", unique = false, nullable = false)
    @Size(max = 10)
    private String password;

    @Column(name = "picture", unique = false, nullable = true, columnDefinition = "TEXT")
    private String picture;

    @Column(name = "address", unique = false, nullable = true, columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone", unique = false, nullable = true)
    @Size(max = 30)
    private String phone;

    @Column(name = "role", unique = false, nullable = false)
    @Size(max = 15)
    private String role;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Auction> auctions = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Bid> bids = new HashSet<>();

    public User() {
    }

    public User fromDTO(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.email = userDTO.getEmail();
        this.picture = userDTO.getPicture();
        this.address = userDTO.getAddress();
        this.phone = userDTO.getPhone();
        setRole(userDTO.getRole());
        return this;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public User setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Set<Auction> getAuctions() {
        return auctions;
    }

    public User setAuctions(Set<Auction> auctions) {
        this.auctions = auctions;
        return this;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public User setBids(Set<Bid> bids) {
        this.bids = bids;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        if(role.equals("admin")){
            this.role = role;
        }else if(role.equals("owner")){
            this.role = role;
        }else{
            this.role="bidder";
        }
        return this;
    }
}
