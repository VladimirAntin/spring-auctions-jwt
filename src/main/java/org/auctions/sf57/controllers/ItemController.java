package org.auctions.sf57.controllers;

import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.dto.ItemDTO;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.service.ItemServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */

@RestController
@RequestMapping(value = "api")
public class ItemController {

    @Autowired
    private ItemServiceInterface itemService;

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/items")
    public ResponseEntity<List<ItemDTO>> getAllItems(final HttpServletRequest request){
        List<ItemDTO> itemsDTO = Sf57Utils.itemsToDTO(itemService.findAll());
        return new ResponseEntity<List<ItemDTO>>(itemsDTO, HttpStatus.OK);

    }

    @GetMapping(value="/items/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable("id") long id,final HttpServletRequest request){
        Item item = itemService.findOne(id);

        if (item==null){
            return new ResponseEntity<ItemDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ItemDTO>(new ItemDTO(item),HttpStatus.OK);
    }

}
