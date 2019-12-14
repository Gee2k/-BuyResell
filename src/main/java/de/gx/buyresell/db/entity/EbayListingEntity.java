package de.gx.buyresell.db.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Slf4j
public class EbayListingEntity extends BaseEntity {
   private String articleId;
   private String url;
   private double price;
   private double shipping;
   private String keywords;

   public EbayListingEntity(String articleId, String url, double price, double shipping, String keywords) {
      this.articleId = articleId;
      this.url = url;
      this.price = price;
      this.shipping = shipping;
      this.keywords = keywords;
   }
}
