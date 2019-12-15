package de.gx.buyresell.io;

import de.gx.buyresell.io.ebay.EbayListing;
import java.util.List;

public interface UrlScraper {
    List<EbayListing> scrapeElementsFromUrl(String url);
}
