package tv.jiaying.acadapter.processor.cdn;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Input")
public class Input {

    private String subID;

    private String sourceURl;

    private int serviceType;

    @XmlAttribute(name = "subID")
    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    @XmlAttribute(name = "sourceURL")
    public String getSourceURl() {
        return sourceURl;
    }

    public void setSourceURl(String sourceURl) {
        this.sourceURl = sourceURl;
    }

    @XmlAttribute(name = "serviceType")
    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
}
