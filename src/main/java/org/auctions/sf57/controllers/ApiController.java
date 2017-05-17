package org.auctions.sf57.controllers;

import io.jsonwebtoken.Claims;
import org.auctions.sf57.dto.UserDTO;
import org.auctions.sf57.entity.User;
import org.auctions.sf57.security.entity.NavItem;
import org.auctions.sf57.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladimir_antin on 12.5.17..
 */

@RestController
@RequestMapping(value="api")
public class ApiController {

    @Autowired
    private UserServiceInterface userService;

    private String ADMIN = "admin";
    private String OWNER = "owner";

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> me(final HttpServletRequest request){
        final Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role!=null){
            User user = userService.findOne(Long.parseLong(claims.getSubject()));
            //DTO
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<UserDTO>(HttpStatus.UNAUTHORIZED);
    }
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/nav_items")
    public ResponseEntity<List<NavItem>> navItems(final HttpServletRequest request){
        final Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role==null){
            return new ResponseEntity<List<NavItem>>(HttpStatus.UNAUTHORIZED);
        }
        List<NavItem> navItems = new ArrayList<>();
        navItems.add(new NavItem("#/home","Home","home"));
        navItems.add(new NavItem("#/auctions","Auctions","shopping cart"));
        if(role.equals(ADMIN)){
            navItems.add(new NavItem("#/items","Items","list"));
            navItems.add(new NavItem("#/users","Users","group"));
        }else if(role.equals(OWNER)){
            navItems.add(new NavItem("#/items","Items","list"));
        }
        navItems.add(new NavItem("#/users/"+claims.getSubject(),"Profile","person"));
        navItems.add(new NavItem("#/logout","Logout","input"));
        return new ResponseEntity<List<NavItem>>(navItems, HttpStatus.OK);
    }



}
