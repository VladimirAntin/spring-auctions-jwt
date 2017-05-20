package org.auctions.sf57.dto;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Bid;
import org.auctions.sf57.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by vladimir_antin on 12.5.17..
 */
public class UserDTO implements Serializable{
    private long id;
    private String name;
    private String email;
    private String oldPassword;
    private String password;
    private String picture;
    private String address;
    private String phone;
    private String role;

    public UserDTO(){}

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.role = user.getRole();
    }

    public long getId() {
        return id;
    }

    public UserDTO setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public UserDTO setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public UserDTO setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserDTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getRole() {
        return role;
    }

    public UserDTO setRole(String role) {
        this.role = role;
        return this;
    }

}
