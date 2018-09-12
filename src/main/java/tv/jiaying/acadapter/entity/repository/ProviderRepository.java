package tv.jiaying.acadapter.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import tv.jiaying.acadapter.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider,Long> {
    Provider findFirstByProviderId(String providerId);

    Boolean existsByProviderId(String providerId);

    @Transactional
    @Modifying
    Provider save(Provider provider);
}
