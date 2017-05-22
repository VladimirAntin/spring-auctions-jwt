package org.auctions.sf57.repository;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Bid;
import org.auctions.sf57.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface BidRepository extends JpaRepository<Bid,Long> {
    List<Bid> findAllByUser(User user);

    List<Bid> findAllByAuction(Auction auction);
}
