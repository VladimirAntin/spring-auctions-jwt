package org.auctions.sf57.repository;

import org.auctions.sf57.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface BidRepository extends JpaRepository<Bid,Long> {
}
