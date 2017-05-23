package org.auctions.sf57.controllers.entity;

import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.dto.AuctionDTO;
import org.auctions.sf57.dto.BidDTO;
import org.auctions.sf57.dto.UserDTO;
import org.auctions.sf57.entity.User;
import org.auctions.sf57.service.AuctionServiceInterface;
import org.auctions.sf57.service.BidServiceInterface;
import org.auctions.sf57.service.UserServiceInterface;
import org.auctions.sf57.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vladimir_antin on 8.5.17..
 */

@RestController
@RequestMapping(value = "api")
public class UserController {
    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private StorageService storageService;

    private String ADMIN = "admin";

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role.equals(ADMIN)){
            List<UserDTO> users = Sf57Utils.usersToDTO(userService.findAll());
            return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
        }
        return new ResponseEntity<List<UserDTO>>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id){
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/auctions")
    public ResponseEntity<List<AuctionDTO>> getUserAuctions(@PathVariable("id") long id){
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity<List<AuctionDTO>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<AuctionDTO>>(Sf57Utils.auctionsToDTO(user.getAuctions()), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/bids")
    public ResponseEntity<List<BidDTO>> getUserBids(@PathVariable("id") long id){
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity<List<BidDTO>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<BidDTO>>(Sf57Utils.bidsToDTO(user.getBids()), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/users")
    public ResponseEntity<UserDTO> post_user(@RequestBody UserDTO userDTO,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role.equals(ADMIN)){
            User user = null;
            if(!Sf57Utils.validate(userDTO.getEmail(),1,30) ||
                !Sf57Utils.validate(userDTO.getName(),1,30) ||
                !Sf57Utils.validate(userDTO.getPassword(),1,10) ||
                !Sf57Utils.validate(userDTO.getPhone(),0,30)){
                return new ResponseEntity<UserDTO>(HttpStatus.CONFLICT); //409
            }
            try{
                userDTO.setPicture("/images/profile.png");
                user = userService.save(new User().fromDTO(userDTO));
                return new ResponseEntity<UserDTO>(new UserDTO(user),HttpStatus.CREATED);
            }catch (Exception e){
                return new ResponseEntity<UserDTO>(HttpStatus.CONFLICT); //409
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") long id,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(role.equals(ADMIN)){
            if(user.getId()==Long.parseLong(claims.getSubject())){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            userService.remove(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
    @SuppressWarnings("unchecked")
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable("id") long id, @RequestBody UserDTO userDTO,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (Sf57Utils.validate(userDTO.getName(),1,30)) {
            user.setName(userDTO.getName());
        }
        if(Sf57Utils.validate(userDTO.getPhone(),0,30)) {
            long phoneCheck = Sf57Utils.long_parser(userDTO.getPhone());
            if(phoneCheck!=0){
                user.setPhone(userDTO.getPhone());
            }else if(userDTO.getPhone().equals("")){
                user.setPhone("");
            }
        }
        user.setAddress(userDTO.getAddress());
        if(role.equals(ADMIN)){
            user.setRole(userDTO.getRole());
            userService.save(user);
            return new ResponseEntity<UserDTO>(new UserDTO(user),HttpStatus.OK);
        }else if(Long.parseLong(claims.getSubject())==id){
            userService.save(user);
            return new ResponseEntity<UserDTO>(new UserDTO(user),HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @PatchMapping(value = "/users/{id}/password")
    public ResponseEntity<UserDTO> updatePassword(@PathVariable("id") long id, @RequestBody UserDTO userDTO,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(role.equals(ADMIN)){
            if(Sf57Utils.validate(userDTO.getPassword(),1,10)){
                user.setPassword(userDTO.getPassword());
                userService.save(user);
                return new ResponseEntity(new UserDTO(user), HttpStatus.OK);
            }
        }else if(Long.parseLong(claims.getSubject())==id){
            if(user.getPassword().equals(userDTO.getOldPassword()) &&
                    Sf57Utils.validate(userDTO.getPassword(),1,10)){
                user.setPassword(userDTO.getPassword());
                userService.save(user);
                return new ResponseEntity(new UserDTO(user), HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value="/users/{id}/upload")
    public ResponseEntity userFileUpload(@PathVariable("id") long id, @RequestParam("file") MultipartFile file,final HttpServletRequest request) {
        String filename =  file.getOriginalFilename();
        if(Sf57Utils.photo_formats(filename)){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String urlPhoto = "users/"+String.valueOf(id)+".png";
        if(role.equals(ADMIN)){
            storageService.store(file,urlPhoto);
            user.setPicture("files/"+urlPhoto);
            userService.save(user);
            return new ResponseEntity(HttpStatus.OK);
        }else if(Long.parseLong(claims.getSubject())==id){
            storageService.store(file,urlPhoto);
            user.setPicture("files/"+urlPhoto);
            userService.save(user);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }


}
