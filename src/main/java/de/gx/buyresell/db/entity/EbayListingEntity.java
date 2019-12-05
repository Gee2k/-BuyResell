package de.gx.buyresell.db.entity;

import javax.persistence.Entity;

@Entity
public class EbayListingEntity extends BaseEntity {
   String itemId;
   String url;
   double price;
   double shipping;

   public EbayListingEntity(String itemId, String url, double price, double shipping) {
      this.itemId = itemId;
      this.price = price;
      this.shipping = shipping;
   }
}
