package tv.jiaying.acadapter.processor.cdn;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * DeleteContent接口需要的参数
 */
@XmlRootElement(name = "DeleteContent")
public class DeleteContentParam {

    private String providerId;

    private String assetId;

    private String volumeName;

    private int reasonCode;

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

    @XmlAttribute(name = "volumeName")
    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    @XmlAttribute(name = "reasonCode")
    public int getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
    }

    @XmlElement(name = "Input")
    public List<Input> getInputList() {
        return inputList;
    }

    public void setInputList(List<Input> inputList) {
        this.inputList = inputList;
    }
}
