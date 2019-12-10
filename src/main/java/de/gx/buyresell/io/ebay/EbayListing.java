package de.gx.buyresell.io.ebay;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EbayListing {
    private String url;
    private String articleNumber;
    private double price;
    private double shipping;
    private String keywords;
}
