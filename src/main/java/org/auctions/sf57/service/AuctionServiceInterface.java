package org.auctions.sf57.service;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.entity.User;

import java.util.Date;
import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface AuctionServiceInterface {


    Auction findOne(Long auction_id);

    List<Auction> findAll();

    List<Auction> findAllByUser(User user);

    List<Auction> findAllByItem(Item item);

    List<Auction> findAllByOrderByStartDate();

    List<Auction> findAllByOrderByStartDateDesc();

    List<Auction> findAllByItemSold(boolean sold);

    Auction save(Auction auction);

    void remove(Long auction_id);
}
