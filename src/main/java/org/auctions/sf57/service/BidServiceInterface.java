package org.auctions.sf57.service;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Bid;
import org.auctions.sf57.entity.User;

import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface BidServiceInterface {

    Bid findOne(Long bid_id);

    List<Bid> findAllByUser(User user);

    List<Bid> findAllByAuction(Auction auction);

    List<Bid> findAll();

    Bid save(Bid bid);

    void remove(Long bid_id);
}
