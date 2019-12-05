package de.gx.buyresell.io;


import de.gx.buyresell.io.ebay.EbayAuction;
import de.gx.buyresell.io.ebay.ImportEbayAuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class TestRestService {
    ImportEbayAuctions importEbayAuctions;

    @Autowired
    public void setImportEbayAuctions(ImportEbayAuctions importEbayAuctions) {
        this.importEbayAuctions = importEbayAuctions;
    }

    @RequestMapping(method = GET, path = "/crawlEbayAuctions")
//    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
    public ResponseEntity<List<EbayAuction>> crawlEbayAuctions() {
        List<EbayAuction> ebayAuctions = new ArrayList<>();
        try {
            return new ResponseEntity<>(importEbayAuctions.scrapeSoldComputerAuctions(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
