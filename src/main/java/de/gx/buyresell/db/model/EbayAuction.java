package de.gx.buyresell.db.model;

import de.gx.buyresell.db.repository.EbayListingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EbayAuction {

    EbayListingRepository ebayListingRepository;

    @Autowired
    public void setEbayListingRepository(EbayListingRepository ebayListingRepository) {
        this.ebayListingRepository = ebayListingRepository;
    }


    public List<String> getListingKeywords() {
        return new ArrayList<>();
    }
}
