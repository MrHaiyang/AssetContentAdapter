package tv.jiaying.acadapter.processor.cdn;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 注入接口参数
 */
@XmlRootElement(name = "TransferContent")
public class TransferContentParam {

    private String providerId;

    private String assetId;

    private int transferBitRate;

    private String contentName;

    private String volumeName;

    private String responseURL;

    private Boolean startNext;

    private List<Input> inputList;

    @XmlAttribute(name = "providerID")
    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @XmlAttribute(name = "assetID")
    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    @XmlAttribute(name = "transferBitRate")
    public int getTransferBitRate() {
        return transferBitRate;
    }

    public void setTransferBitRate(int transferBitRate) {
        this.transferBitRate = transferBitRate;
    }

    @XmlAttribute(name = "contentName")
    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    @XmlAttribute(name = "volumeName")
    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    @XmlAttribute(name = "responseURL")
    public String getResponseURL() {
        return responseURL;
    }

    public void setResponseURL(String responseURL) {
        this.responseURL = responseURL;
    }

    @XmlAttribute(name = "startNext")
    public Boolean getStartNext() {
        return startNext;
    }

    public void setStartNext(Boolean startNext) {
        this.startNext = startNext;
    }

    @XmlElement(name = "Input")
    public List<Input> getInputList() {
        return inputList;
    }

    public void setInputList(List<Input> inputList) {
        this.inputList = inputList;
    }
}
