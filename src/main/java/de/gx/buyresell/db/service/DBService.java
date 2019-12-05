package de.gx.buyresell.db.service;

import de.gx.buyresell.db.entity.EbayAuction;
import de.gx.buyresell.db.repository.EbayAuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBService {

    private EbayAuctionRepository ebayAuctionRepository;

    @Autowired
    public void setEbayAuctionRepository(EbayAuctionRepository ebayAuctionRepository) {
        this.ebayAuctionRepository = ebayAuctionRepository;
    }

    public void saveEbayAuction(EbayAuction ebayAuction) {
        ebayAuctionRepository.save(ebayAuction);
    }

    public List<EbayAuction> getEbayAuctions() {
        List<EbayAuction> ebayAuctions = new ArrayList<>();
        ebayAuctionRepository.findAll().forEach(ebayAuctions::add);
        return ebayAuctions;
    }
}
