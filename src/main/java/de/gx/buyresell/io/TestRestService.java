package de.gx.buyresell.io;


import de.gx.buyresell.io.ebay.EbayListing;
import de.gx.buyresell.io.ebay.EbayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class TestRestService {
    EbayService ebayService;

    @Autowired
    public void setEbayService(EbayService ebayService) {
        this.ebayService = ebayService;
    }

    @RequestMapping(method = GET, path = "/crawlEbayAuctions")
//    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
    public ResponseEntity<List<EbayListing>> crawlEbayAuctions() {
        List<EbayListing> ebayListings = new ArrayList<>();
        try {
            return new ResponseEntity<>(ebayService.scrapeSoldToyAuctions(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
