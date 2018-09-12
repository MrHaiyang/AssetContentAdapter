package tv.jiaying.acadapter.processor.cdn;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * cdn相关配置 各属性说明在配置文件application.propeties
 */
@Component
@ConfigurationProperties(prefix = "adapter.cdn.transfer")
public class TransferConfig {

    private String volumeName;

    private String responseUrl;

    private Boolean startNext;

    private String contentUrl;

    private String transferStatusUrl;

    private String contentInfoUrl;

    private String deleteContentUrl;

    private int serverType;

    private String assetService;

    private long responseTimeInterval;

    public long getResponseTimeInterval() {
        return responseTimeInterval;
    }

    public void setResponseTimeInterval(long responseTimeInterval) {
        this.responseTimeInterval = responseTimeInterval;
    }

    public String getAssetService() {
        return assetService;
    }

    public void setAssetService(String assetService) {
        this.assetService = assetService;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getContentInfoUrl() {
        return contentInfoUrl;
    }

    public void setContentInfoUrl(String contentInfoUrl) {
        this.contentInfoUrl = contentInfoUrl;
    }

    public String getTransferStatusUrl() {
        return transferStatusUrl;
    }

    public void setTransferStatusUrl(String transferStatusUrl) {
        this.transferStatusUrl = transferStatusUrl;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }


    public Boolean getStartNext() {
        return startNext;
    }

    public void setStartNext(Boolean startNext) {
        this.startNext = startNext;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getDeleteContentUrl() {
        return deleteContentUrl;
    }

    public void setDeleteContentUrl(String deleteContentUrl) {
        this.deleteContentUrl = deleteContentUrl;
    }
}
