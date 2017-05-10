package org.auctions.sf57.service;

import org.auctions.sf57.entity.Item;
import org.auctions.sf57.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */
@Service
public class ItemService implements ItemServiceInterface {
    @Autowired
    ItemRepository itemRepository;

    @Override
    public Item findOne(Long item_id) {
        return itemRepository.findOne(item_id);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void remove(Long item_id) {
        itemRepository.delete(item_id);
    }
}
