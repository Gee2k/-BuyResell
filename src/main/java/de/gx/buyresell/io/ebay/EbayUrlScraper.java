package de.gx.buyresell.io.ebay;

import de.gx.buyresell.io.UrlScraper;
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
public class EbayUrlScraper implements UrlScraper {

    private NumberFormatConverter numberFormatConverter;

    @Autowired
    public void setNumberFormatConverter(NumberFormatConverter numberFormatConverter) {
        this.numberFormatConverter = numberFormatConverter;
    }

    @Override
    public List<EbayListing> scrapeElementsFromUrl(String url) {
        List<EbayListing> receivedAuctionListings = new ArrayList<>();
        Document website = null;
        try {
            website = Jsoup.connect(url).userAgent("${jsoup.userAgent}").get();
        } catch (IOException e) {
//            e.printStackTrace();
            throw new RuntimeException("error fetching data");
        }
        log.debug("[ImportEbayAuctions] parsing url: {}", url);

        Element element = website.getElementById("ResultSetItems");
        Elements foundSoldItems = element.getElementsByClass("sresult lvresult clearfix li");
        log.debug("[ImportEbayAuctions] found [{}] elements", foundSoldItems.size());
//        return foundSoldItems;

        foundSoldItems.forEach(entry -> {
            String articleUrl = entry.getElementsByClass("vip").attr("href");
            double sellingPrice = parsePrice(entry.getElementsByClass("bold bidsold").text());  //wenn leer, dann binsold was eine range von bis ist.
            double shipping = parsePrice(entry.getElementsByClass("lvshipping").text());
            log.debug("[ImportEbayAuctions] add url: {}", articleUrl);
            String auctionNumber = parseAuctionNumberFromUrl(articleUrl);

            String keywords = extractKeywordsFromUrl(articleUrl);
            receivedAuctionListings.add(new EbayListing(articleUrl, auctionNumber, sellingPrice, shipping, keywords));
        });

        return receivedAuctionListings;
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

    private String parseAuctionNumberFromUrl(String articleUrl) {
        int firstIndex = articleUrl.lastIndexOf("/");
        int lastIndex = articleUrl.lastIndexOf("?");
        String auctionNumberString = articleUrl.substring(firstIndex + 1, lastIndex).trim();
        log.debug("[ImportEbayAuctions] auction id: {}", auctionNumberString);

        return auctionNumberString;
    }

    private String extractKeywordsFromUrl(String articleUrl) {
        int lastIndex = articleUrl.lastIndexOf("/");
        int firstIndex = articleUrl.lastIndexOf("/", lastIndex - 1);
//        log.debug("[ImportEbayAuctions] keywords index from: {} - to: {}", firstIndex , lastIndex);
        String keywords = articleUrl.substring(firstIndex + 1, lastIndex);
        keywords = keywords.replaceAll("-", " ");
        log.debug("[ImportEbayAuctions] keywords: {}", keywords);

        return keywords;
    }
}
