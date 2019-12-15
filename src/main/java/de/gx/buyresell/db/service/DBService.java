package de.gx.buyresell.db.service;

import de.gx.buyresell.db.entity.EbayListingEntity;
import de.gx.buyresell.db.repository.EbayListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBService {

    private EbayListingRepository ebayListingRepository;

    @Autowired
    public void setEbayListingRepository(EbayListingRepository ebayListingRepository) {
        this.ebayListingRepository = ebayListingRepository;
    }

    public void saveEbayListing(EbayListingEntity ebayListingEntity) {
        ebayListingRepository.save(ebayListingEntity);
    }

    public boolean saveIfNotExists(EbayListingEntity ebayListingEntity) {
        if(!ebayListingRepository.existsEbayListingEntityByArticleId(ebayListingEntity.getArticleId())) {
            ebayListingRepository.save(ebayListingEntity);
            return true;
        }
        return false;
    }

    public List<EbayListingEntity> getEbayListings() {
        return ebayListingRepository.findAll();
    }

    public long amountEbayListings() {
        return ebayListingRepository.count();
    }
}
