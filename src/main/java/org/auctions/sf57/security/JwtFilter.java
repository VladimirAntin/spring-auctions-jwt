package org.auctions.sf57.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;


/**
 * @GitHub https://github.com/VladimirAntin/jwt-angular-spring/blob/master/src/main/java/com/nibado/example/jwtangspr/JwtFilter.java
 */

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response =(HttpServletResponse) res;
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("jwt ")) {
            response.sendError(403,"Missing or invalid Authorization header.");
            return;
            //throw new ServletException("Missing or invalid Authorization header."); //500
        }

        final String token = authHeader.substring(4); // The part after "jwt "

        try {
            final Claims claims = Jwts.parser().setSigningKey("secretkey")
                .parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (Exception e) {
            response.sendError(401,"Invalid token.");
            return;
        //throw new ServletException("Invalid token."); //500
        }

        chain.doFilter(req, res);
    }

}
