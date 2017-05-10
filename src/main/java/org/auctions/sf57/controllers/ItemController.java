package org.auctions.sf57.controllers;

import org.auctions.sf57.entity.Item;
import org.auctions.sf57.service.ItemServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */

@RestController
@RequestMapping(value = "api")
public class ItemController {

    @Autowired
    private ItemServiceInterface itemService;

    @GetMapping(value = "/items")
    public ResponseEntity<List<Item>> getAllItems(){
        return new ResponseEntity<List<Item>>(itemService.findAll(), HttpStatus.OK);
    }
    @GetMapping(value="/items/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") long id){
        Item item = itemService.findOne(id);

        if (item==null){
            return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Item>(item,HttpStatus.OK);
    }

}
