package de.gx.buyresell.io.ebay;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EbayListing {
    String url;
    double price;
    double shipping;
}