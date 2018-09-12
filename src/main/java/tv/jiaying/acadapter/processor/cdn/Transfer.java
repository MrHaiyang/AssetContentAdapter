package tv.jiaying.acadapter.processor.cdn;

import org.springframework.stereotype.Component;

@Component
public interface Transfer {

    int transferContent(TransferContentParam param);

    Object getTransferStatus(GetTransferStatusParam param);

    Object getContentInfo(GetContentInfoParam param);

    Boolean cancelTransfer();

    Boolean deleteContent(DeleteContentParam param);



}
