package org.auctions.sf57.controllers;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;

import org.auctions.sf57.entity.User;
import org.auctions.sf57.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserServiceInterface userService;

	
    @PostMapping(value = "login")
    public LoginResponse login(@RequestBody final UserLogin login)
        throws ServletException {
        User user = userService.findByEmailAndPassword(login.username,login.password);
        if (user==null) {
            throw new ServletException("Invalid login");
        }
        return new LoginResponse(Jwts.builder().setSubject(user.getEmail())
            .claim("role", user.getRole()).setIssuedAt(new Date()).setExpiration(getExpirationDate())
            .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
    }

    @SuppressWarnings("unused")
    private static class UserLogin {
        public String username;
        public String password;
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }

    public Date getExpirationDate(){
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 24); // adds one hour
        return cal.getTime(); // returns new date object, one hour in the future
    }
}
