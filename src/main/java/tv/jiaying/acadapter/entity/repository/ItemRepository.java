package tv.jiaying.acadapter.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import tv.jiaying.acadapter.entity.Item;
import tv.jiaying.acadapter.entity.Provider;

public interface ItemRepository extends JpaRepository<Item,Long> {

    Item findFirstByProviderAndAssetId(Provider provider, String assetId);

    @Transactional
    Item findFirstByAssetIdAndProvider(String assetId,Provider provider);

    Boolean existsByAssetIdAndProvider(String assetId,Provider provider);

    @Transactional
    @Modifying
    Item save(Item item);
}
