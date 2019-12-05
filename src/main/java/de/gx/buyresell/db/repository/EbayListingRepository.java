package de.gx.buyresell.db.repository;

import de.gx.buyresell.db.entity.EbayListingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EbayListingRepository extends CrudRepository<EbayListingEntity, UUID> {
}
