package de.gx.buyresell.io.ebay;

import de.gx.buyresell.db.entity.EbayListingEntity;
import de.gx.buyresell.db.service.DBService;
import de.gx.buyresell.io.util.NumberFormatConverter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ImportEbayAuctions {

    String FINISHED_AUCTIONS_COMPUTER_URI = "https://www.ebay.de/sch/i.html?_nkw&_in_kw=1&_ex_kw&_sacat=58058&LH_Sold=1&_udlo&_udhi&LH_ItemCondition=3&_samilow&_samihi&_sadis=10&_fpos&LH_SALE_CURRENCY=0&_sop=12&_dmd=1&_ipg=50&LH_Complete=1";


    private DBService dbService;
    private NumberFormatConverter numberFormatConverter;
    private Jsoup jsoup;


    @Autowired
    public void setDbService(DBService dbService) {
        this.dbService = dbService;
    }

    @Autowired
    public void setNumberFormatConverter(NumberFormatConverter numberFormatConverter) {
        this.numberFormatConverter = numberFormatConverter;
    }

    public List<EbayListing> scrapeSoldComputerAuctions() throws IOException {
        return scrapeSoldEbayAuctions(FINISHED_AUCTIONS_COMPUTER_URI);
    }

    private double parsePrice(String price) {
        // <span class="bold bidsold">
        // <b>EUR</b>
        // 16,00
        // </span>

        // oder

        //<span class="fee">+ EUR 5,50 versand</span>

        log.debug("[ImportEbayAuctions] trying to parse and convert \"{}\" into double", price);

        double returnValue = 0.0;
//        Pattern pattern = Pattern.compile("[0-9]{1,2},{1}[0-9]{2}");
        Pattern pattern = Pattern.compile("([.]{0,1}[0-9]{0,2}){0,}[0-9]{1},{1}[0-9]{2}");
        Matcher matcher = pattern.matcher(price);

        try {
            if (price.contains(",") && matcher.find()) {
                return numberFormatConverter.parse(matcher.group(0));
            }
        }
        catch (Exception e){
            log.debug("[ImportEbayAuctions] conversion failed");
        }
        return 0.00;
    }

    private List<EbayListing> scrapeSoldEbayAuctions(String url) throws IOException {
        List<EbayListing> receivedAuctionUrls = new ArrayList<>();

//        website = Jsoup.connect(url).userAgent("${jsoup.userAgent}").get();
        Document website = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
        log.debug("[ImportEbayAuctions] parsing url: {}", url);

        Element element = website.getElementById("ResultSetItems");
        Elements foundSoldItems = element.getElementsByClass("sresult lvresult clearfix li");
        log.debug("[ImportEbayAuctions] found [{}] elements", foundSoldItems.size());

        foundSoldItems.forEach(entry -> {

            String articleUrl = entry.getElementsByClass("vip").attr("href");
            double sellingPrice = parsePrice(entry.getElementsByClass("bold bidsold").text());  //wenn leer, dann binsold was eine range von bis ist.
            double shipping = parsePrice(entry.getElementsByClass("lvshipping").text());
            log.debug("[ImportEbayAuctions] add url: {}", articleUrl);
            receivedAuctionUrls.add(new EbayListing(articleUrl, sellingPrice, shipping));
        });

        //save in db
        receivedAuctionUrls.forEach(item -> {
            dbService.saveEbayListing(new EbayListingEntity("", item.getUrl(), item.getPrice(), item.getShipping()));
        });

        return receivedAuctionUrls;
    }
}
