package tv.jiaying.acadapter.entity;


import javax.persistence.*;

/**
 * 对item的扩展字段
 */
@Entity
public class ItemExtend {

    @Id
    @GeneratedValue
    private long id;

    private String fsContentFormat;

    private String contentFileSize;

    private String HDContent;

    private String subtitleLanguage;

    private int bitRate;

    private String audioType;

    private String codec;

    private String frameRate;

    private String resolution;

    private String encryption;

    private String extend1;

    private String extend2;

    private String extend3;

    private String extend4;

    private String extend5;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFsContentFormat() {
        return fsContentFormat;
    }

    public void setFsContentFormat(String fsContentFormat) {
        this.fsContentFormat = fsContentFormat;
    }

    public String getContentFileSize() {
        return contentFileSize;
    }

    public void setContentFileSize(String contentFileSize) {
        this.contentFileSize = contentFileSize;
    }

    public String getHDContent() {
        return HDContent;
    }

    public void setHDContent(String HDContent) {
        this.HDContent = HDContent;
    }

    public String getSubtitleLanguage() {
        return subtitleLanguage;
    }

    public void setSubtitleLanguage(String subtitleLanguage) {
        this.subtitleLanguage = subtitleLanguage;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(String frameRate) {
        this.frameRate = frameRate;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public String getExtend4() {
        return extend4;
    }

    public void setExtend4(String extend4) {
        this.extend4 = extend4;
    }

    public String getExtend5() {
        return extend5;
    }

    public void setExtend5(String extend5) {
        this.extend5 = extend5;
    }
}
