package org.auctions.sf57.repository;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface AuctionRepository extends JpaRepository<Auction,Long> {
    List<Auction> findAllByItemSold(boolean sold);

    List<Auction> findAllByOrderByStartDate();

    List<Auction> findAllByOrderByStartDateDesc();

    List<Auction> findAllByUser(User user);

    List<Auction> findAllByItem(Item item);

}
