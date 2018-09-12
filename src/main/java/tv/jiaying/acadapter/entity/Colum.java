package tv.jiaying.acadapter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

/**
 * 栏目信息
 */
@Entity
@Table(name = "colum")
@Proxy(lazy = false)
public class Colum {

    @Id
    @GeneratedValue
    private long id;

    private Boolean isRoot; //是否是根栏目

    public enum Type {
        normal, //普通栏目
        series  //电视剧
    }

    private String providerId; //提供商ID

    @Column(unique = true)
    private String name; //栏目名称

    private String nameSpell;

    private Type type;  //栏目类型

    private String posterSmall; //小海报

    private String posterMid; //中海报

    private String posterLarge; //大海报

    private String brief; //简介

    private String director; //导演

    private String directorSpell;

    private String actors; //演员

    private String actorsSpell;

    private String year; //年份

    private String genre; //电视剧类型 若是普通栏目可为空

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date licensingWindowStart; //版权开始时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date LicensingWindowEnd; //版权结束时间

    private Boolean online; //栏目上下架


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPosterSmall() {
        return posterSmall;
    }

    public void setPosterSmall(String posterSmall) {
        this.posterSmall = posterSmall;
    }

    public String getPosterMid() {
        return posterMid;
    }

    public void setPosterMid(String posterMid) {
        this.posterMid = posterMid;
    }

    public String getPosterLarge() {
        return posterLarge;
    }

    public void setPosterLarge(String posterLarge) {
        this.posterLarge = posterLarge;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getLicensingWindowStart() {
        return licensingWindowStart;
    }

    public void setLicensingWindowStart(Date licensingWindowStart) {
        this.licensingWindowStart = licensingWindowStart;
    }

    public Date getLicensingWindowEnd() {
        return LicensingWindowEnd;
    }

    public void setLicensingWindowEnd(Date licensingWindowEnd) {
        LicensingWindowEnd = licensingWindowEnd;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getNameSpell() {
        return nameSpell;
    }

    public void setNameSpell(String nameSpell) {
        this.nameSpell = nameSpell;
    }

    public String getDirectorSpell() {
        return directorSpell;
    }

    public void setDirectorSpell(String directorSpell) {
        this.directorSpell = directorSpell;
    }

    public String getActorsSpell() {
        return actorsSpell;
    }

    public void setActorsSpell(String actorsSpell) {
        this.actorsSpell = actorsSpell;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}

