package org.auctions.sf57.repository;

import org.auctions.sf57.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findByEmailAndPassword(String email,String password);
}
