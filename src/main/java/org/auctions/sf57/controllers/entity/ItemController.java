package org.auctions.sf57.controllers.entity;

import io.jsonwebtoken.Claims;
import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.dto.AuctionDTO;
import org.auctions.sf57.dto.ItemDTO;
import org.auctions.sf57.dto.UserDTO;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.entity.User;
import org.auctions.sf57.service.AuctionServiceInterface;
import org.auctions.sf57.service.ItemServiceInterface;
import org.auctions.sf57.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by vladimir_antin on 8.5.17..
 */

@RestController
@RequestMapping(value = "api")
public class ItemController {

    private String ADMIN = "admin";
    private String OWNER = "owner";
    @Autowired
    private ItemServiceInterface itemService;

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/items")
    public ResponseEntity<List<ItemDTO>> getAllItems(){
        List<ItemDTO> itemsDTO = Sf57Utils.itemsToDTO(itemService.findAll());
        return new ResponseEntity<List<ItemDTO>>(itemsDTO, HttpStatus.OK);
    }

    @GetMapping(value="/items/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable("id") long id){
        Item item = itemService.findOne(id);
        if (item==null){
            return new ResponseEntity<ItemDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ItemDTO>(new ItemDTO(item),HttpStatus.OK);
    }

    @GetMapping(value="/items/{id}/auctions")
    public ResponseEntity<List<AuctionDTO>> getItemAuctions(@PathVariable("id") long id){
        Item item = itemService.findOne(id);
        if (item==null){
            return new ResponseEntity<List<AuctionDTO>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<AuctionDTO>>(
                Sf57Utils.auctionsToDTO(item.getAuctions()),HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping(value = "/items/{id}")
    public ResponseEntity removeItemById(@PathVariable("id") long id,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        Item item = itemService.findOne(id);
        if(item==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(role.equals(ADMIN) || (role.equals(OWNER) && !item.isSold())){
            itemService.remove(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/items")
    public ResponseEntity<ItemDTO> postItem(@RequestBody ItemDTO itemDTO){
        Item item = null;
        if(!Sf57Utils.validate(itemDTO.getName(),1,30)){
            return new ResponseEntity<ItemDTO>(HttpStatus.CONFLICT);
        }
        try{
            itemDTO.setSold(false);
            item = itemService.save(new Item().fromDTO(itemDTO));
            return new ResponseEntity<ItemDTO>(new ItemDTO(item),HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<ItemDTO>(HttpStatus.CONFLICT); //409
        }
    }

    @SuppressWarnings("unchecked")
    @PutMapping(value = "/items/{id}")
    public ResponseEntity<ItemDTO> updateItemById(@PathVariable("id") long id, @RequestBody ItemDTO itemDTO,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        Item item = itemService.findOne(id);
        if(item==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (Sf57Utils.validate(itemDTO.getName(),1,30)) {
            item.setName(itemDTO.getName());
        }
        item.setDescription(itemDTO.getDescription());
        item.setSold(itemDTO.isSold());
        if(role.equals(ADMIN) || (role.equals(OWNER) && !item.isSold())){
            itemService.save(item);
            return new ResponseEntity<ItemDTO>(new ItemDTO(item),HttpStatus.OK);
        }else if(Long.parseLong(claims.getSubject())==id){
            itemService.save(item);
            return new ResponseEntity<ItemDTO>(new ItemDTO(item),HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value="/items/{id}/upload")
    public ResponseEntity ItemFileUpload(@PathVariable("id") long id, @RequestParam("file") MultipartFile file, final HttpServletRequest request) {
        String filename =  file.getOriginalFilename();
        if(Sf57Utils.photo_formats(filename)){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        Item item = itemService.findOne(id);
        if(item==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String urlPhoto = "items/"+String.valueOf(id)+".png";
        if(role.equals(ADMIN) || (role.equals(OWNER) && !item.isSold())){
            storageService.store(file,urlPhoto);
            item.setPicture("files/"+urlPhoto);
            itemService.save(item);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
