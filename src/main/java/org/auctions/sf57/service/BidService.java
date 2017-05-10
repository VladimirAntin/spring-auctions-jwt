package org.auctions.sf57.service;

import org.auctions.sf57.entity.Bid;
import org.auctions.sf57.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
@Service
public class BidService implements BidServiceInterface{
    @Autowired
    BidRepository bidRepository;

    @Override
    public Bid findOne(Long bid_id) {
        return bidRepository.findOne(bid_id);
    }

    @Override
    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    @Override
    public Bid save(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    public void remove(Long bid_id) {
        bidRepository.delete(bid_id);
    }
}
