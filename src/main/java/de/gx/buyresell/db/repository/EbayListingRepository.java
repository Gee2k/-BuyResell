package de.gx.buyresell.db.repository;

import de.gx.buyresell.db.entity.EbayListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EbayListingRepository extends JpaRepository<EbayListingEntity, UUID> {
    EbayListingEntity findEbayListingEntityByArticleId(String articleId);
    boolean existsEbayListingEntityByArticleId(String articleId);
}
