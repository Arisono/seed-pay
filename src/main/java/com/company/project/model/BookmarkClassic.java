package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_bookmark_classify")
public class BookmarkClassic {
    /**
     * 主键-自增长id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分类-名称
     */
    private String name;

    /**
     * 分类-创建日期
     */
    @Column(name = "time_create")
    private Date timeCreate;

    /**
     * 分类-用户id-外键
     */
    private Integer userid;

    /**
     * 获取主键-自增长id
     *
     * @return id - 主键-自增长id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键-自增长id
     *
     * @param id 主键-自增长id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分类-名称
     *
     * @return name - 分类-名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类-名称
     *
     * @param name 分类-名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取分类-创建日期
     *
     * @return time_create - 分类-创建日期
     */
    public Date getTimeCreate() {
        return timeCreate;
    }

    /**
     * 设置分类-创建日期
     *
     * @param timeCreate 分类-创建日期
     */
    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    /**
     * 获取分类-用户id-外键
     *
     * @return userid - 分类-用户id-外键
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置分类-用户id-外键
     *
     * @param userid 分类-用户id-外键
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}