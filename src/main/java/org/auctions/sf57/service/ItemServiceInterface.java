package org.auctions.sf57.service;

import org.auctions.sf57.entity.Item;

import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
public interface ItemServiceInterface {
    Item findOne(Long item_id);

    List<Item> findAll();

    Item save(Item item);

    void remove(Long item_id);
}
