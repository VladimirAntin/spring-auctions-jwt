package org.auctions.sf57.service;

import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.User;
import org.auctions.sf57.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
@Service
public class AuctionService implements AuctionServiceInterface{
    @Autowired
    AuctionRepository auctionRepository;


    @Override
    public Auction findOne(Long auction_id) {
        return auctionRepository.findOne(auction_id);
    }

    @Override
    public List<Auction> findAll() {
        return auctionRepository.findAll();
    }

    @Override
    public List<Auction> findAllByUser(User user) {
        return auctionRepository.findAllByUser(user);
    }

    @Override
    public List<Auction> findAllOrderByStartDate(Date startDate) {
        return auctionRepository.findAllOrderByStartDate(startDate);
    }

    @Override
    public Auction save(Auction auction) {
        return auctionRepository.save(auction);
    }

    @Override
    public void remove(Long auction_id) {
        auctionRepository.delete(auction_id);
    }
}
