package org.auctions.sf57.controllers.entity;

import io.jsonwebtoken.Claims;
import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.dto.AuctionDTO;
import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.service.AuctionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by vladimir_antin on 19.5.17..
 */
@RestController
@RequestMapping(value = "api")
public class AuctionController {
    private String ADMIN = "admin";
    private String OWNER = "owner";

    @Autowired
    private AuctionServiceInterface auctionService;

    @GetMapping(value = "/auctions")
    public ResponseEntity<List<AuctionDTO>> getAllAuctions(final HttpServletRequest request){
        List<AuctionDTO> auctionsDTO = Sf57Utils.auctionsToDTO(auctionService.findAll());
        return new ResponseEntity<List<AuctionDTO>>(auctionsDTO, HttpStatus.OK);
    }

    @GetMapping(value="/auctions/{id}")
    public ResponseEntity<AuctionDTO> getItemById(@PathVariable("id") long id, final HttpServletRequest request){
        Auction auction = auctionService.findOne(id);
        if(auction==null){
            return new ResponseEntity<AuctionDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AuctionDTO>(new AuctionDTO(auction),HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping(value = "/auctions/{id}")
    public ResponseEntity removeAuctionById(@PathVariable("id") long id,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        Auction auction = auctionService.findOne(id);
        if(auction==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(role.equals(ADMIN) || (role.equals(OWNER) && (auction.getStartDate().before(new Date())))){
            auctionService.remove(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }


}
