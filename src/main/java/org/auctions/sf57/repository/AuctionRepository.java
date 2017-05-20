package org.auctions.sf57.repository;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface AuctionRepository extends JpaRepository<Auction,Long> {
    List<Auction> findAllOrderByStartDate(Date startDate);

    List<Auction> findAllByUser(User user);
}
