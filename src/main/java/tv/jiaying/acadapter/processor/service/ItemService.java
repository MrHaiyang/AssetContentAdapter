package tv.jiaying.acadapter.processor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.jiaying.acadapter.entity.*;
import tv.jiaying.acadapter.entity.repository.*;
import tv.jiaying.acadapter.util.ChineseCharUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ItemService {

    private static Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Resource
    ItemRepository itemRepository;

    @Resource
    ProviderRepository providerRepository;

    @Resource
    ItemExtendRepository itemExtendRepository;

    @Resource
    RelevanceRepository relevanceRepository;

    @Resource
    ColumRepository columRepository;


    /**
     * //保存Item类,Provider类和ItemExtend类
     *
     * @param item
     */

    public synchronized void saveItemAndLinkedInfo(Item item) {

        Provider provider = providerRepository.findFirstByProviderId(item.getProvider().getProviderId());
        if(provider == null){
            provider = providerRepository.save(item.getProvider());
        }else{
            item.setProvider(provider);
        }

        ItemExtend itemExtend = item.getItemExtend();
        ItemExtend findItemExtend = itemExtendRepository.findFirstByContentFileSizeAndFsContentFormatAndBitRate(itemExtend.getContentFileSize(), itemExtend.getFsContentFormat(), itemExtend.getBitRate());
        if (findItemExtend == null) {
            itemExtendRepository.save(itemExtend);
        } else {
            item.setItemExtend(findItemExtend);
        }
        Boolean findItem = itemRepository.existsByAssetIdAndProvider(item.getAssetId(), provider);
        if (!findItem) {
            item.setModifyDate(new Date());
            String titleSpell = ChineseCharUtil.getFirstSpell(item.getTitle());
            item.setTitleSpell(titleSpell);
            String directorSpell = ChineseCharUtil.getFirstSpell(item.getDirector());
            item.setDirectorSpell(directorSpell);
            String actorSpell = ChineseCharUtil.getFirstSpell(item.getActors());
            item.setActorsSpell(actorSpell);
            itemRepository.save(item);

            //添加item到relevance
            this.addItemToRelevance(item);

        } else {
            //不做更新
        }
    }

    public void addItemToRelevance(Item item){

        String providerId = item.getProvider().getProviderId();

        List<Colum> colums = columRepository.findAllByProviderId(providerId);

        if(colums.size()==0){
            return ;
        }

        for (Colum colum: colums) {
            Relevance relevance = relevanceRepository.findByParentColumIdAndChildItemId(colum.getId(),item.getId());
            if(relevance==null){
                relevance = new Relevance();
                relevance.setParentColumId(colum.getId());
                relevance.setChildItemId(item.getId());
                relevance.setOnline(false);
                relevance.setLastUpdateTime(new Date());

                relevanceRepository.save(relevance);
            }
        }
    }
}
