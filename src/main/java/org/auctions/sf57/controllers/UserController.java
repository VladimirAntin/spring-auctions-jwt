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

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(final HttpServletRequest request){
    	final Claims claims = (Claims) request.getAttribute("claims");
    	String role = (String)claims.get("role");
        if(role.equals("admin")){
            List<UserDTO> users = new ArrayList<>();
            for (User user:userService.findAll()) {
                users.add(new UserDTO(user));
            }
            return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
    	}
        return new ResponseEntity<List<UserDTO>>(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id,final HttpServletRequest request){
        final Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role.equals("admin")){
            User user = userService.findOne(id);
            if(user==null){
                return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<UserDTO>(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") long id,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
