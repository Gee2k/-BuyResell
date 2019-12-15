package de.gx.buyresell.io.ebay;

import de.gx.buyresell.db.entity.EbayListingEntity;
import de.gx.buyresell.db.service.DBService;
import de.gx.buyresell.io.UrlScraper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@Slf4j
public class EbayService {

    private String finishedEbayUrl;
    private String finishedToysEbayUrl;
    private List<String> ebayFinishedAuctionsUrlList;

    private UrlScraper urlScraper;
    private DBService dbService;
    private CircuitBreaker circuitBreaker;

    @Autowired
    public void setFinishedEbayUrl(@Value("${io.ebay.url.finished.computer}") String finishedEbayUrl) {
        this.finishedEbayUrl = finishedEbayUrl;
    }

    @Autowired
    public void setFinishedToysEbayUrl(@Value("${io.ebay.url.finished.toys}") String finishedToysEbayUrl) {
        this.finishedToysEbayUrl = finishedToysEbayUrl;
    }

    @Autowired
    public void setEbayFinishedAuctionsUrlList(@Value("#{'${io.ebay.url.list}'.split(',')}") List<String> ebayFinishedAuctionsUrlList) {
        this.ebayFinishedAuctionsUrlList = ebayFinishedAuctionsUrlList;
    }

    @Autowired
    public void setDbService(DBService dbService) {
        this.dbService = dbService;
    }

    @Autowired
    public void setUrlScraper(UrlScraper urlScraper) {
        this.urlScraper = urlScraper;
    }

    @Autowired
    public void setCircuitBreaker(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public List<EbayListing> scrapeSoldComputerAuctions() throws IOException {
        return scrapeSoldEbayAuctions(finishedEbayUrl);
    }

    public List<EbayListing> scrapeSoldToyAuctions() throws IOException {
        return scrapeSoldEbayAuctions(finishedToysEbayUrl);
    }

    private List<EbayListing> scrapeSoldEbayAuctions(String url) {
        List<EbayListing> receivedAuctionListings = new ArrayList<>();
//        receivedAuctionListings = urlScraper.scrapeElementsFromUrl(url);

        //1
//        Function<String, List<EbayListing>> decorated = CircuitBreaker.decorateFunction(circuitBreaker, urlScraper::scrapeElementsFromUrl);
//        receivedAuctionListings = decorated.apply(url);

        //2
        Supplier<List<EbayListing>> ebayListingsSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> urlScraper.scrapeElementsFromUrl(url));
        receivedAuctionListings = ebayListingsSupplier.get();

        //save in db
        long entryAmount = dbService.amountEbayListings();
        log.debug("[ImportEbayAuctions] Try to save {} items to db", receivedAuctionListings.size());
        receivedAuctionListings.forEach(item -> {
         //            dbService.saveEbayListing(new EbayListingEntity(item.getArticleNumber(), item.getUrl(), item.getPrice(), item.getShipping(), item.getKeywords()));
            dbService.saveIfNotExists(new EbayListingEntity(item.getArticleNumber(), item.getUrl(), item.getPrice(), item.getShipping(), item.getKeywords()));
        });
        log.debug("saved {} (new) retrieved listings", dbService.amountEbayListings() - entryAmount);
        return receivedAuctionListings;
    }
}
