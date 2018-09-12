package tv.jiaying.acadapter.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tv.jiaying.acadapter.entity.Item;
import tv.jiaying.acadapter.entity.ItemExtend;
import tv.jiaying.acadapter.entity.Provider;
import tv.jiaying.acadapter.entity.repository.ItemExtendRepository;
import tv.jiaying.acadapter.entity.repository.ItemRepository;
import tv.jiaying.acadapter.entity.repository.ProviderRepository;
import tv.jiaying.acadapter.processor.service.ItemService;
import tv.jiaying.acadapter.util.ChineseCharUtil;
import tv.jiaying.acadapter.util.XMLParseTool;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * adi文件处理模块
 */
@Component
public class ADIResourceProcessor implements AbstractProcessor {

    private static Logger logger = LoggerFactory.getLogger(ADIResourceProcessor.class);

    @Resource
    ItemRepository itemRepository;

    @Resource
    ProviderRepository providerRepository;

    @Resource
    ItemExtendRepository itemExtendRepository;

    @Resource
    ItemService itemService;

    @Override
    public void handle(String filepath) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat timesdf = new SimpleDateFormat("HHmmss");

        File xmlFile = new File(filepath);
        if (xmlFile.exists()) {

            String fileTag = "backup";
            try {
                //xml文件转换成Item类
                Item item = new XMLParseTool().parseXMLFileToItemClass(xmlFile);
                //保存Item类,Provider类和ItemExtend类

                itemService.saveItemAndLinkedInfo(item);


            } catch (Exception e) {
                logger.info(e.getMessage(), e);
                //处理错误就投入error文件夹
                fileTag = "error";
            }

            File backupDirectory = new File(xmlFile.getParent(), fileTag + "/" + format.format(new Date()));
            if (!backupDirectory.exists() || !backupDirectory.isDirectory()) {
                backupDirectory.mkdirs();
            }

            //文件名相同的话，rename方法无效，无法覆盖，所以加了date用于唯一标识
            File backupFile = new File(backupDirectory, timesdf.format(new Date()) + "_" + xmlFile.getName());
            xmlFile.renameTo(backupFile);

            logger.info(" {} save to database[item] success", xmlFile.getName());
        }

    }

}
