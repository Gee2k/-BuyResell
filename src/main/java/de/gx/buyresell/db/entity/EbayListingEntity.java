package de.gx.buyresell.db.entity;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Slf4j
public class EbayListingEntity extends BaseEntity {
   private String itemId;
   private String url;
   private double price;
   private double shipping;

//   List<String> keywords = new ArrayList<>();

   public EbayListingEntity(String itemId, String url, double price, double shipping) {
      this.itemId = itemId;
      this.url = url;
      this.price = price;
      this.shipping = shipping;
      log.debug("Ebay Listing saved to DB with: \"{}\" \"{}\" \"{}\" \"{}\"", itemId, url, price, shipping);
   }


   public List<String> getListingKeywords() {
      return null;
   }

}
