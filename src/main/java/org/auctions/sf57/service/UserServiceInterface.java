package org.auctions.sf57.service;

import org.auctions.sf57.entity.User;

import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface UserServiceInterface {

    User findByEmail(String email);

    User findByEmailAndPassword(String email,String password);

    User findOne(Long user_id);

    List<User> findAll();

    User save(User user);

    void remove(Long user_id);
}
