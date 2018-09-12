package tv.jiaying.acadapter.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import tv.jiaying.acadapter.entity.ItemExtend;

public interface ItemExtendRepository extends JpaRepository<ItemExtend,Long> {
    ItemExtend findFirstByContentFileSizeAndFsContentFormatAndBitRate(String contentFileSize,String FSContentFormat,int bitRate);
}
