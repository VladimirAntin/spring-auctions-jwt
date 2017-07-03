package org.auctions.sf57.controllers.entity;

import io.jsonwebtoken.Claims;
import org.auctions.sf57.config.Sf57Utils;
import org.auctions.sf57.dto.AuctionDTO;
import org.auctions.sf57.dto.BidDTO;
import org.auctions.sf57.entity.Auction;
import org.auctions.sf57.entity.Item;
import org.auctions.sf57.entity.User;
import org.auctions.sf57.service.AuctionServiceInterface;
import org.auctions.sf57.service.BidServiceInterface;
import org.auctions.sf57.service.ItemServiceInterface;
import org.auctions.sf57.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import java.text.ParseException;
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

    @Autowired
    private ItemServiceInterface itemService;

    @Autowired
    private UserServiceInterface userService;

    @GetMapping(value = "/auctions")
    public ResponseEntity<List<AuctionDTO>> getAllAuctionsOrderByDate(@RequestParam(value = "date", required = false, defaultValue = "") String date){
        List<AuctionDTO> auctionsDTO = null;
        if(date.equals("start")){
            auctionsDTO = Sf57Utils.auctionsToDTO(auctionService.findAllByOrderByStartDate());
        }else if(date.equals("end")){
            auctionsDTO = Sf57Utils.auctionsToDTO(auctionService.findAllByOrderByStartDateDesc());
        }
        else{
            auctionsDTO = Sf57Utils.auctionsToDTO(auctionService.findAll());
        }
        return new ResponseEntity<List<AuctionDTO>>(auctionsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/auctions/filter")
    public ResponseEntity<List<AuctionDTO>> getAllAuctionsFilter(@RequestParam(value = "sold", required = false, defaultValue = "false") boolean sold){
        List<AuctionDTO> auctionsDTO = Sf57Utils.auctionsToDTO(auctionService.findAllByItemSold(sold));
        return new ResponseEntity<List<AuctionDTO>>(auctionsDTO, HttpStatus.OK);
    }
    @GetMapping(value="/auctions/{id}")
    public ResponseEntity<AuctionDTO> getAuctionById(@PathVariable("id") long id){
        Auction auction = auctionService.findOne(id);
        if(auction==null){
            return new ResponseEntity<AuctionDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AuctionDTO>(new AuctionDTO(auction),HttpStatus.OK);
    }

    @GetMapping(value = "/auctions/{id}/bids")
    public ResponseEntity<List<BidDTO>> getAuctionBids(@PathVariable("id") long id){
        Auction auction = auctionService.findOne(id);
        if(auction==null){
            return new ResponseEntity<List<BidDTO>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<BidDTO>>(Sf57Utils.bidsToDTO(auction.getBids()), HttpStatus.OK);
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

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/auctions")
    public ResponseEntity<AuctionDTO> postAuction(@RequestBody AuctionDTO auctionDTO, final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        User user = userService.findOne(Sf57Utils.long_parser(claims.getSubject()));
        if(user!=null){
            Item item = itemService.findOne(auctionDTO.getItem().getId());
            if(auctionDTO.getStartPrice()<=0){
                return new ResponseEntity<AuctionDTO>(HttpStatus.CONFLICT);
            }
            try{
                Auction auction = new Auction()
                        .fromDTO(auctionDTO)
                        .setUser(user)
                        .setItem(item);
                Date todayAt00 = new Date();
                todayAt00.setHours(0);
                if(auction.getStartDate().before(todayAt00)){
                    return new ResponseEntity<AuctionDTO>(HttpStatus.CONFLICT);
                }
                if(auction.getEndDate()!=null && auction.getEndDate().before(auction.getStartDate())){
                    return new ResponseEntity<AuctionDTO>(HttpStatus.CONFLICT);
                }
                auctionService.save(auction);
                return new ResponseEntity<AuctionDTO>(new AuctionDTO(auction),HttpStatus.CREATED);
            }catch (Exception e){
                return new ResponseEntity<AuctionDTO>(HttpStatus.CONFLICT); //409
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
    @SuppressWarnings("unchecked")
    @PutMapping(value = "/auctions/{id}")
    public ResponseEntity<AuctionDTO> updateAuctionById(@PathVariable("id") long id, @RequestBody AuctionDTO auctionDTO,final HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        User user = userService.findOne(Sf57Utils.long_parser(claims.getSubject()));
        if(auctionDTO==null){
            return new ResponseEntity<AuctionDTO>(HttpStatus.NO_CONTENT);
        }
        Auction auction = auctionService.findOne(id);
        if(auction==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(auctionDTO.getStartPrice()>=0){
            auction.setStartPrice(auctionDTO.getStartPrice());
        }
        try{
            if(auction.getUser().getId()!=user.getId() || user.getRole()!=ADMIN){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            Date startDate = Sf57Utils.jsFormat.parse(auctionDTO.getStartDate());
            Date endDate = null;
            if(auctionDTO.getEndDate()!=null){
                try {
                    endDate = Sf57Utils.jsFormat.parse(auctionDTO.getEndDate());
                }catch (ParseException e){
                }
            }
            Date todayAt00 = new Date();
            todayAt00.setHours(0);
            if(!startDate.before(todayAt00)){
                auction.setStartDate(startDate);
            }
            if(endDate!=null && endDate.after(auction.getStartDate()) && endDate.after(todayAt00)){
                auction.setEndDate(endDate);
            }else if(endDate==null){
                auction.setEndDate(null);
            }else{
                throw new Exception("Not greater than today");
            }
            auctionService.save(auction);
            return new ResponseEntity<AuctionDTO>(new AuctionDTO(auction),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }
}
