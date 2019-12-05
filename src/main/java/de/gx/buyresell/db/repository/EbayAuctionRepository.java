package de.gx.buyresell.db.repository;

import de.gx.buyresell.db.entity.EbayAuction;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EbayAuctionRepository extends CrudRepository<EbayAuction, UUID> {
}
