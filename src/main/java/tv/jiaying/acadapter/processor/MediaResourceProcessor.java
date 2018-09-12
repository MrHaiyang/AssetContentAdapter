package tv.jiaying.acadapter.processor;



import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import tv.jiaying.acadapter.entity.Colum;
import tv.jiaying.acadapter.entity.Item;
import tv.jiaying.acadapter.entity.Provider;
import tv.jiaying.acadapter.entity.Relevance;
import tv.jiaying.acadapter.entity.repository.ColumRepository;
import tv.jiaying.acadapter.entity.repository.ItemRepository;
import tv.jiaying.acadapter.entity.repository.ProviderRepository;
import tv.jiaying.acadapter.entity.repository.RelevanceRepository;
import tv.jiaying.acadapter.processor.cdn.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 媒资处理模块
 */
@Component
@ConfigurationProperties(prefix = "adapter.processor.media")
public class MediaResourceProcessor implements AbstractProcessor {

    private static Logger logger = LoggerFactory.getLogger(MediaResourceProcessor.class);

    //媒资存放地址
    private String service;

    private String backup;

    @Resource
    ItemRepository itemRepository;

    @Resource
    ProviderRepository providerRepository;

    @Resource
    ColumRepository columRepository;

    @Resource
    RelevanceRepository relevanceRepository;

    //cdn注入时相关参数
    @Autowired
    TransferConfig transferConfig;

    //cdn接口
    @Resource
    CDNTransfer cdnTransfer;

    private static final String READY_FILE_PREFIX = "go_";

    private static final String UPLOAD_FILE_PREFINX = "transfering_";

    private static final String ERROR_FILE_PREFINX = "error_";

    @Override
    public void handle(String filepath) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


        File localFile = new File(filepath);

        File mediaFile = new File(localFile.getParentFile(), localFile.getName().replaceFirst(READY_FILE_PREFIX, UPLOAD_FILE_PREFINX));
//        try {
//            FileCopyUtils.copy(localFile,mediaFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        localFile.renameTo(mediaFile);
        if (mediaFile.exists()) {

            File errorDirectory = new File(mediaFile.getParent());
            if (!errorDirectory.exists() || !errorDirectory.isDirectory()) {
                errorDirectory.mkdirs();
            }
            File errorFile = new File(errorDirectory, mediaFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, ERROR_FILE_PREFINX));

            //媒资文件格式  transfering_providerId_assertId_xxx.mp4
            String[] path = mediaFile.getName().split("_");
            String providerId = null;
            String assetId = null;
            String subId = null;
            int transferBitRate;
            try {
                providerId = path[1];
                assetId = path[2];
            } catch (Exception e) {
                mediaFile.renameTo(errorFile);
                logger.info("{} transfer failed,check file format", mediaFile.getName());
                return;
            }
            String fileType = mediaFile.getName().substring(mediaFile.getName().lastIndexOf("."));
            //去除汉字，重新命名

            File renameFile = new File(mediaFile.getParent(), UPLOAD_FILE_PREFINX + providerId + "_" + assetId + "_" + new Date().getTime() + fileType);
            //File renameFile = new File(mediaFile.getParent(), UPLOAD_FILE_PREFINX+new Date().getTime()+fileType);

            mediaFile.renameTo(renameFile);

            Provider providerforCheck = providerRepository.findFirstByProviderId(providerId);
            Item item = itemRepository.findFirstByAssetIdAndProvider(assetId, providerforCheck);
            if (item == null) {
                renameFile.renameTo(errorFile);
                logger.info("{} transfer failed,item not found", renameFile.getName());
                return;
            }

            TransferContentParam transferContentParam = new TransferContentParam();

            Provider provider = item.getProvider();
            //如果文件名上的标识和adi文件里内容不一致，退出，不注入
            if (!providerId.equals(provider.getProviderId())) {
                renameFile.renameTo(errorFile);
                logger.info("{} transfer failed,check file format", renameFile.getName());
                return;
            }
            providerId = provider.getProviderId();
            String contentName = item.getTitle();
            String volumeName = transferConfig.getVolumeName();
            String responseURL = transferConfig.getResponseUrl();
            boolean startNext = transferConfig.getStartNext();

            if (item.getItemExtend().getBitRate() == 0) {
                transferBitRate = 8000;
            } else {
                try {
                    long fileSize =renameFile.length();
                    String[] runtime =item.getRunTime().split(":");
                    int hour =Integer.parseInt(runtime[0]);
                    int min =Integer.parseInt(runtime[1]);
                    int sec =Integer.parseInt(runtime[2]);
                    int totalSec =hour*3600+min*60+sec;
                    int temp  = (int)((fileSize*8/totalSec)/1000);

                    transferBitRate = (int)(temp +200);
                    logger.info("bite:{}",transferBitRate);
                } catch (NumberFormatException e) {
                    logger.info(e.getMessage(),e);
                    File errorFile1 = new File(errorDirectory, mediaFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, ERROR_FILE_PREFINX));
                    renameFile.renameTo(errorFile1);
                    return;
                }

                //logger.info("transferBitRate:{}",transferBitRate);
            }
            transferContentParam.setProviderId(providerId);
            //用数据库唯一Id代替assetId
            transferContentParam.setAssetId(Long.toString(item.getId()));
            transferContentParam.setTransferBitRate(transferBitRate);
            transferContentParam.setContentName(contentName);
            transferContentParam.setVolumeName(volumeName);
            transferContentParam.setResponseURL(responseURL);
            transferContentParam.setStartNext(startNext);

            List<Input> inputs = new ArrayList<>();
            String downloadPath = service + "/" + renameFile.getName();
            for (int i = 0; i < 1; i++) {
                Input input = new Input();
                input.setSubID(Long.toString(transferBitRate));
                input.setSourceURl(downloadPath);
                input.setServiceType(transferConfig.getServerType());
                inputs.add(input);
            }

            transferContentParam.setInputList(inputs);

            //内容注入接口
            int result = cdnTransfer.transferContent(transferContentParam);

            if(result==409){
                DeleteContentParam deleteContentParam = new DeleteContentParam();
                deleteContentParam.setProviderId(providerId);
                deleteContentParam.setAssetId(Long.toString(item.getId()));
                deleteContentParam.setVolumeName(transferConfig.getVolumeName());
                deleteContentParam.setReasonCode(202);

                List<Input> inputList = new ArrayList<>();
                for (int i = 0; i < 1; i++) {
                    Input input = new Input();
                    input.setSubID(Long.toString(transferBitRate));
                    input.setServiceType(3);
                    inputList.add(input);
                }

                deleteContentParam.setInputList(inputList);

                Boolean deleteSuccess = cdnTransfer.deleteContent(deleteContentParam);

                if(deleteSuccess){
                    logger.info("{} delete success",mediaFile.getName());
                    File retry = new File(errorDirectory, mediaFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, READY_FILE_PREFIX));
                    renameFile.renameTo(retry);
                    return;
                }else{
                    logger.info("{} delete fail",mediaFile.getName());
                    File error = new File(errorDirectory, mediaFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, ERROR_FILE_PREFINX));
                    renameFile.renameTo(error);
                    return;
                }

            }

            File backupDirectory = new File(backup,  format.format(new Date()));
            if (!backupDirectory.exists() || !backupDirectory.isDirectory()) {
                backupDirectory.mkdirs();
            }
            File backupFile = new File(backupDirectory, renameFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, ""));

            //调用查询注入状态接口
            if (result == 200) {

                String providerIdforthread = providerId;

                GetTransferStatusParam param = new GetTransferStatusParam();
                param.setProviderId(providerId);
                param.setAssetId(Long.toString(item.getId()));
                param.setVolumeName(volumeName);
                param.setInputList(inputs);

                ExecutorService executorService = Executors.newFixedThreadPool(1);
                //注入进程监控
                executorService.submit(new Runnable() {
                    Boolean transferIsProcessing = true;

                    @Override
                    public void run() {
                        while (transferIsProcessing) {

                            String resultXml = cdnTransfer.getTransferStatus(param).toString();

                            try {
                                Document doc = DocumentHelper.parseText(resultXml);
                                Element root = doc.getRootElement();
                                Element output = root.element("Output");
                                String state = output.attribute("state").getValue();
                                String reasonCode = output.attribute("reasonCode").getValue();
                                String percentComplete = output.attributeValue("percentComplete");
                                logger.info("asset:{} >>>{} {}%", renameFile.getName(), state, percentComplete);
                                switch (state) {
                                    case "Complete":
                                        item.setTransferUrl(downloadPath);
                                        item.setAssetService(transferConfig.getAssetService());
                                        //格式如 10049_1000018000
                                        item.setPlayUrl(item.getProvider().getProviderId() + "-" + item.getId() + transferBitRate);
                                        item.setOnline(true);
                                        itemRepository.save(item);
                                        //更新relevance
                                        makeRelevanceOfItemOnline(item);
                                        File backupFile = new File(backupDirectory, renameFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, "success_"));
                                        //logger.info("backupFile:{}",backupFile.getAbsolutePath());
                                        Boolean rm = renameFile.renameTo(backupFile);
                                        if(!rm){
                                            File success = new File(renameFile.getParent(), renameFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, "success_"));
                                            renameFile.renameTo(success);
//                                            logger.info("rm_success:{}",rm);
                                        }
                                        //logger.info("rm:{}",rm);
                                        transferIsProcessing = false;
                                        break;
                                    case "Delete":
                                        logger.info("asset :{} already deleted", renameFile.getName());
                                        transferIsProcessing = false;
                                        File errorFile1 = new File(errorDirectory, mediaFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, reasonCode+ERROR_FILE_PREFINX));

                                        renameFile.renameTo(errorFile1);
                                        break;
                                    case "Ingest Failure":
                                        logger.info("asset :{} Ingest Failure,reasonCode:{}", renameFile.getName(),reasonCode);
                                        transferIsProcessing = false;
                                        File errorFile2 = new File(errorDirectory, mediaFile.getName().replaceFirst(UPLOAD_FILE_PREFINX, reasonCode+ERROR_FILE_PREFINX));
                                        renameFile.renameTo(errorFile2);
                                        break;
                                    default:
                                        ;
                                }

                            } catch (DocumentException e) {
                                logger.info(e.getMessage(), e);
                            }

                            try {
                                Thread.sleep(transferConfig.getResponseTimeInterval());
                            } catch (InterruptedException e) {
                                logger.info(e.getMessage(), e);
                            }

                        }

                    }
                });

            } else {
                renameFile.renameTo(errorFile);
            }

        }

    }

    /**
     * 设置关联中item上线
     * @param item
     */
    public void makeRelevanceOfItemOnline(Item item){

        List<Relevance> relevances = relevanceRepository.findByChildItemId(item.getId());

        for (Relevance relevance: relevances) {
            relevance.setOnline(true);
            relevanceRepository.save(relevance);
        }
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }
}
