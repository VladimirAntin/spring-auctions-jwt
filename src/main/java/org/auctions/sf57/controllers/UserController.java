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
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        if(role.equals(ADMIN)){
            List<UserDTO> users = new ArrayList<>();
            for (User user:userService.findAll()) {
                users.add(new UserDTO(user));
            }
            return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
        }
        return new ResponseEntity<List<UserDTO>>(HttpStatus.UNAUTHORIZED);
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
            if(userDTO.getEmail().length()>30 || userDTO.getEmail().length()<1 ||
                userDTO.getName().length()>30 || userDTO.getName().length()<1 ||
                userDTO.getPassword().length()<1 || userDTO.getPassword().length()>10 ||
                userDTO.getPhone().length()>30){
                return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST); //400
            }
            try{
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
        if(role.equals(ADMIN)){
            User user = userService.findOne(id);
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
        if (userDTO.getName() != null && userDTO.getName().length()<=30 && userDTO.getName().length()>=1) {
            user.setName(userDTO.getName());
        }
        if(userDTO.getPhone() != null && userDTO.getPhone().length()<=30) {
            long phoneCheck;
            try{
                phoneCheck = Long.parseLong(userDTO.getPhone());
            }catch (Exception e){
                phoneCheck = 0;
            }
            if(phoneCheck!=0){
                user.setPhone(userDTO.getPhone());
            }else if(userDTO.getPhone().equals("")){
                user.setPhone("");
            }
        }
        user.setAddress(userDTO.getAddress());
        if(role.equals(ADMIN)){
            userService.save(user);
            return new ResponseEntity(new UserDTO(user),HttpStatus.OK);
        }else if(Long.parseLong(claims.getSubject())==id){
            userService.save(user);
            return new ResponseEntity(new UserDTO(user),HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @PatchMapping(value = "/users/{id}/role")
    public ResponseEntity updateRole(@PathVariable("id") long id, @RequestBody UserDTO userDTO,final HttpServletRequest request){

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
        if(user.getPassword().equals(userDTO.getOldPassword()) && userDTO.getPassword()!=null
                && userDTO.getPassword().length()>=1 && userDTO.getPassword().length()<10){
                user.setPassword(userDTO.getPassword());
        }else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if(role.equals(ADMIN)){
            userService.save(user);
            return new ResponseEntity(new UserDTO(user), HttpStatus.OK);
        }else if(Long.parseLong(claims.getSubject())==id){
            userService.save(user);
            return new ResponseEntity(new UserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
