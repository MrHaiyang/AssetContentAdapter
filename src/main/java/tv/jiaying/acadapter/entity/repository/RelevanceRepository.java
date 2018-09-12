package tv.jiaying.acadapter.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import tv.jiaying.acadapter.entity.Relevance;
import java.util.List;

public interface RelevanceRepository extends JpaRepository<Relevance, Long> {

    Page<Relevance> findByParentColumId(long parentColumId, Pageable pageable);

    Page<Relevance> findByParentColumIdOrderByPositionDesc(long parentColumId, Pageable pageable);

    List<Relevance> findByParentColumIdOrderByPositionDesc(long parentColumId);

    Boolean existsByParentColumIdAndChildItemId(long parentColumId, long childItemId);

    Relevance findByParentColumIdAndChildItemId(long parentColumId, long childItemId);

    Relevance findByParentColumIdAndChildColumId(long parentColumId, long childColumId);

    Boolean existsByParentColumIdAndChildColumId(long parentColumId, long childColumId);

    @Modifying
    @Transactional
    void deleteByParentColumIdAndChildItemId(long parentColumId, long childItemId);

    @Modifying
    @Transactional
    void deleteByParentColumIdAndChildColumId(long parentColumId, long childColumId);
    @Modifying
    @Transactional
    void deleteByParentColumIdOrChildColumId(long parentColumId, long childColumId);
    @Modifying
    @Transactional
    void deleteByChildItemId(long childItemId);

    List<Relevance> findByParentColumId(long parentColumId);

    List<Relevance> findByChildItemId(long childColumId);



    @Modifying
    @Transactional
    void deleteByParentColumId(long parentColumId);

    @Modifying
    @Transactional
    void deleteByChildColumId(long childColumId);

    Boolean existsByParentColumId(long parentColumId);

    Boolean existsByChildColumId(long childColumId);

}
