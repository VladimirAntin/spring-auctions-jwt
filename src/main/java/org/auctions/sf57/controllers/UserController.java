package org.auctions.sf57.controllers;

import org.auctions.sf57.dto.UserDTO;
import org.auctions.sf57.entity.User;
import org.auctions.sf57.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by vladimir_antin on 8.5.17..
 */

@RestController
@RequestMapping(value = "api")
public class UserController {
    @Autowired
    private UserServiceInterface userService;

    private String ADMIN = "admin";

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(final HttpServletRequest request){
        List<UserDTO> users = new ArrayList<>();
        for (User user:userService.findAll()) {
            users.add(new UserDTO(user));
        }
        return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id,final HttpServletRequest request){
        User user = userService.findOne(id);
        if(user==null){
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
    }


    @SuppressWarnings("unchecked")
    @PostMapping(value = "/users")
    public ResponseEntity<UserDTO> post_user(@RequestBody UserDTO userDTO,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role.equals(ADMIN)){
            User user = null;
            try{
                user = userService.save(new User(userDTO));
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
        if(role.equals(ADMIN)){
            User user = userService.findOne(id);
            if(user.getEmail().equals(claims.getSubject())){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            userService.remove(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

}
