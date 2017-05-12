package org.auctions.sf57.controllers;

import org.auctions.sf57.entity.User;
import org.auctions.sf57.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;

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

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers(final HttpServletRequest request){
    	final Claims claims = (Claims) request.getAttribute("claims");
    	String role = (String)claims.get("role");
        if(role.equals("admin")){
            return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);    		
    	}
        return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);    		
    }

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id,final HttpServletRequest request){
        final Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role.equals("admin")){
            User user = userService.findOne(id);
            if(user==null){
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") long id,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
