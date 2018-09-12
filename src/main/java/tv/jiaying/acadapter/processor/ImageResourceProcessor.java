package tv.jiaying.acadapter.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tv.jiaying.acadapter.entity.Item;
import tv.jiaying.acadapter.entity.Provider;
import tv.jiaying.acadapter.entity.repository.ItemRepository;
import tv.jiaying.acadapter.entity.repository.ProviderRepository;
import tv.jiaying.acadapter.util.ChineseCharUtil;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

/**
 * 海报文件处理模块
 */
@Component
@ConfigurationProperties(prefix = "adapter.processor.img")
public class ImageResourceProcessor implements AbstractProcessor {

    private static Logger logger = LoggerFactory.getLogger(ImageResourceProcessor.class);

    //静态资源存放位置 该位置可通过http访问到资源
    private String staticlocation;
    //海报备份路径
    private String backuplocation;

    @Resource
    ItemRepository itemRepository;

    @Resource
    ProviderRepository providerRepository;

    @Override
    public void handle(String filepath) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        File imgFile = new File(filepath);
        if (imgFile.exists()) {

            File errorDirectory = new File(imgFile.getParent(), "error" + "/" + format.format(new Date()));
            if (!errorDirectory.exists() || !errorDirectory.isDirectory()) {
                errorDirectory.mkdirs();
            }
            File errorFile = new File(errorDirectory, imgFile.getName());

            String[] path = imgFile.getName().split("_");
            String providerId = null;
            String assetId = null;
            String posterSize = null;
            //通过文件名得到assetID和海报尺寸(small,mid,large)
            try {
                providerId = path[0];
                assetId = path[1];
                posterSize = path[2];
                //处理small.png这种情况
                if (posterSize.contains(".")) {
                    posterSize = posterSize.substring(0,posterSize.lastIndexOf("."));
                }
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
                imgFile.renameTo(errorFile);
                logger.info(" upload error here :{} >>> {} ", errorFile.getName(), errorFile.getAbsolutePath());
                return;
            }

            Provider provider = providerRepository.findFirstByProviderId(providerId);
            Item item = itemRepository.findFirstByAssetIdAndProvider(assetId, provider);

            File file = null;
            if (item == null) {
                imgFile.renameTo(errorFile);
                logger.info(" upload error :{} >>> {} ", errorFile.getName(), errorFile.getAbsolutePath());
                return;
            } else {

                File backupDirectory = new File(backuplocation);
                if (!backupDirectory.exists() || !backupDirectory.isDirectory()) {
                    backupDirectory.mkdirs();
                }
                String imgType = imgFile.getName().substring(imgFile.getName().lastIndexOf("."));
                file = new File(backupDirectory, providerId + "_" + assetId + "_" + posterSize + "_" + new Date().getTime() + imgType);
                //todo 挂载的方式需要注意文件最后的路径是应用服务器上的位置
                switch (posterSize) {
                    case "small":
                        item.setPosterSmall(staticlocation + file.getName());
                        itemRepository.save(item);
                        imgFile.renameTo(file);
                        break;
                    case "mid":
                        item.setPosterMid(staticlocation + file.getName());
                        itemRepository.save(item);
                        imgFile.renameTo(file);
                        break;
                    case "large":
                        item.setPosterLarge(staticlocation + file.getName());
                        itemRepository.save(item);
                        imgFile.renameTo(file);
                        break;
                    default:
                        imgFile.renameTo(errorFile);
                        logger.info(" upload error :{} >>> {} ", errorFile.getName(), errorFile.getAbsolutePath());
                        return;
                }

            }

            logger.info(" upload success :{} >>> {} ", imgFile.getName(), file.getAbsolutePath());

        }
    }

    public String getStaticlocation() {
        return staticlocation;
    }

    public void setStaticlocation(String staticlocation) {
        this.staticlocation = staticlocation;
    }

    public String getBackuplocation() {
        return backuplocation;
    }

    public void setBackuplocation(String backuplocation) {
        this.backuplocation = backuplocation;
    }
}
