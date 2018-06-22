package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_bookmark")
public class Bookmark {
    /**
     * 自增长id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 书签-名称
     */
    private String name;

    /**
     * 书签-URL
     */
    private String url;

    /**
     * 书签-描述
     */
    @Column(name = "describes")
    private String describe;

    /**
     * 书签-创建日期
     */
    @Column(name = "time_create")
    private Date timeCreate;

    /**
     * 书签-用户id-外键
     */
    private Integer userid;

    /**
     * 书签-分类id-外键
     */
    private Integer classifyid;

    /**
     * 获取自增长id
     *
     * @return id - 自增长id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置自增长id
     *
     * @param id 自增长id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取书签-名称
     *
     * @return name - 书签-名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置书签-名称
     *
     * @param name 书签-名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取书签-URL
     *
     * @return url - 书签-URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置书签-URL
     *
     * @param url 书签-URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取书签-描述
     *
     * @return describe - 书签-描述
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置书签-描述
     *
     * @param describe 书签-描述
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 获取书签-创建日期
     *
     * @return time_create - 书签-创建日期
     */
    public Date getTimeCreate() {
        return timeCreate;
    }

    /**
     * 设置书签-创建日期
     *
     * @param timeCreate 书签-创建日期
     */
    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    /**
     * 获取书签-用户id-外键
     *
     * @return userid - 书签-用户id-外键
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置书签-用户id-外键
     *
     * @param userid 书签-用户id-外键
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取书签-分类id-外键
     *
     * @return classifyid - 书签-分类id-外键
     */
    public Integer getClassifyid() {
        return classifyid;
    }

    /**
     * 设置书签-分类id-外键
     *
     * @param classifyid 书签-分类id-外键
     */
    public void setClassifyid(Integer classifyid) {
        this.classifyid = classifyid;
    }
}