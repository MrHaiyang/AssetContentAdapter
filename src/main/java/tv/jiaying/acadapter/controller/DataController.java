package tv.jiaying.acadapter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tv.jiaying.acadapter.controller.pojos.ResultBean;
import tv.jiaying.acadapter.entity.Item;
import tv.jiaying.acadapter.entity.repository.ItemRepository;
import tv.jiaying.acadapter.processor.cdn.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/content")
@CrossOrigin
public class DataController {

    private static Logger logger = LoggerFactory.getLogger(DataController.class);

    @Resource
    CDNTransfer cdnTransfer;

    //cdn注入时相关参数
    @Autowired
    TransferConfig transferConfig;

    @Resource
    ItemRepository itemRepository;

    @PostMapping("/deleteContent")
    @CrossOrigin
    public ResultBean deleteContent(long id,String param){

//        Item item = itemRepository.getOne(id);
//        if(item==null){
//            return new ResultBean(1,"item is null");
//        }
        DeleteContentParam deleteContentParam = new DeleteContentParam();

        String providerId;
        String assetId;
        String subId;
        try {
            providerId = param.split("-")[0];
            assetId = param.split("-")[1].substring(0,6);
            subId = param.split("-")[1].substring(6);
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            return new ResultBean(1,"delete fail");
        }

        deleteContentParam.setProviderId(providerId);
        deleteContentParam.setAssetId(assetId);
        deleteContentParam.setVolumeName(transferConfig.getVolumeName());
        deleteContentParam.setReasonCode(202);

        List<Input> inputs = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Input input = new Input();
            input.setSubID(subId);
            input.setServiceType(3);
            inputs.add(input);
        }

        deleteContentParam.setInputList(inputs);

        Boolean deleteSuccess = cdnTransfer.deleteContent(deleteContentParam);
        if(deleteSuccess){
            //item.setPlayUrl(null);
            //itemRepository.save(item);
            return new ResultBean(0,"delete success");
        }
        return new ResultBean(1,"delete fail");

    }

//
//    @GetMapping("/contentInfo")
//    @ResponseBody
//    public String getcdnTransferContentInfo(){
//        GetContentInfoParam param = new GetContentInfoParam();
//        param.setAssetId("GPAC0120180801200030");
//        param.setProviderId("10068");
//        param.setVolumeName("volumeA");
//
//        List<Input> inputs = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            Input input = new Input();
//            input.setSubID("800");
//            input.setServiceType(3);
//            inputs.add(input);
//        }
//
//
//        param.setInputList(inputs);
//
//        return cdnTransfer.getContentInfo(param).toString();
//    }
//
//    @GetMapping("/TransferStatus")
//    @ResponseBody
//    public String getTransferStatus(){
//        GetTransferStatusParam param = new GetTransferStatusParam();
//        param.setAssetId("GPAC0120180801200030");
//        param.setProviderId("10068");
//        param.setVolumeName("volumeA");
//
//        List<Input> inputs = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            Input input = new Input();
//            input.setSubID("800");
//            input.setServiceType(3);
//            inputs.add(input);
//        }
//
//
//        param.setInputList(inputs);
//
//        return cdnTransfer.getTransferStatus(param).toString();
//    }
}
