package org.auctions.sf57.controllers.entity;

import io.jsonwebtoken.Claims;
import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.dto.BidDTO;
import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Bid;
import org.auctions.sf57.entity.User;
import org.auctions.sf57.service.AuctionServiceInterface;
import org.auctions.sf57.service.BidServiceInterface;
import org.auctions.sf57.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by vladimir_antin on 22.5.17..
 */

@RestController
@RequestMapping(value = "api")
public class BidsController {
    private String ADMIN = "admin";
    private String OWNER = "owner";

    @Autowired
    private BidServiceInterface bidService;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private AuctionServiceInterface auctionService;

    @GetMapping(value = "/bids")
    public ResponseEntity<List<BidDTO>> getAllBids(){
        List<BidDTO> bidsDTO = Sf57Utils.bidsToDTO(bidService.findAll());
        return new ResponseEntity<List<BidDTO>>(bidsDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/bids/{id}")
    public ResponseEntity<BidDTO> getBidById(@PathVariable("id") long id){
        BidDTO bidDTO = new BidDTO(bidService.findOne(id));
        if(bidDTO==null){
            return new ResponseEntity<BidDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BidDTO>(bidDTO,HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping(value = "/bids/{id}")
    public ResponseEntity removeBidById(@PathVariable("id") long id,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String role = (String)claims.get("role");
        Bid bid = bidService.findOne(id);
        if(bid==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(role.equals(ADMIN) || bid.getUser().getId()==Sf57Utils.long_parser(claims.getSubject())){
            bidService.remove(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/bids")
    public ResponseEntity<BidDTO> postBid(@RequestBody BidDTO bidDTO,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        User user = userService.findOne(Sf57Utils.long_parser(claims.getSubject()));
        if(user!=null){
            Auction auction = auctionService.findOne(bidDTO.getAuction().getId());
            if(auction!=null){
                if(bidDTO.getPrice()>0 && auction.getEndDate()!=null && auction.getEndDate().after(new Date())){
                    Bid bid = new Bid()
                            .setAuction(auction)
                            .setDateTime(new Date())
                            .setPrice(bidDTO.getPrice())
                            .setUser(user);
                    bidService.save(bid);
                    return new ResponseEntity<BidDTO>(new BidDTO(bid),HttpStatus.CREATED);
                }
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
