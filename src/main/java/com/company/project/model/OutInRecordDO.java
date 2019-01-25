package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "out_in_record")
public class OutInRecordDO {
    /**
     * 编号
     */
    @Id
    @Column(name = "record_id")
    private Long recordId;

    /**
     * 记录名
     */
    @Column(name = "record_name")
    private String recordName;

    /**
     * 出校时间
     */
    @Column(name = "out_date")
    private Date outDate;

    /**
     * 入校时间
     */
    @Column(name = "in_date")
    private Date inDate;

    /**
     * 详情
     */
    @Column(name = "record_details")
    private String recordDetails;

    /**
     * 备注
     */
    @Column(name = "record_remarks")
    private String recordRemarks;

    /**
     * 学生
     */
    @Column(name = "stu_id")
    private Long stuId;

    /**
     * 学校
     */
    @Column(name = "school_id")
    private Long schoolId;

    /**
     * 获取编号
     *
     * @return record_id - 编号
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * 设置编号
     *
     * @param recordId 编号
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取记录名
     *
     * @return record_name - 记录名
     */
    public String getRecordName() {
        return recordName;
    }

    /**
     * 设置记录名
     *
     * @param recordName 记录名
     */
    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    /**
     * 获取出校时间
     *
     * @return out_date - 出校时间
     */
    public Date getOutDate() {
        return outDate;
    }

    /**
     * 设置出校时间
     *
     * @param outDate 出校时间
     */
    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    /**
     * 获取入校时间
     *
     * @return in_date - 入校时间
     */
    public Date getInDate() {
        return inDate;
    }

    /**
     * 设置入校时间
     *
     * @param inDate 入校时间
     */
    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    /**
     * 获取详情
     *
     * @return record_details - 详情
     */
    public String getRecordDetails() {
        return recordDetails;
    }

    /**
     * 设置详情
     *
     * @param recordDetails 详情
     */
    public void setRecordDetails(String recordDetails) {
        this.recordDetails = recordDetails;
    }

    /**
     * 获取备注
     *
     * @return record_remarks - 备注
     */
    public String getRecordRemarks() {
        return recordRemarks;
    }

    /**
     * 设置备注
     *
     * @param recordRemarks 备注
     */
    public void setRecordRemarks(String recordRemarks) {
        this.recordRemarks = recordRemarks;
    }

    /**
     * 获取学生
     *
     * @return stu_id - 学生
     */
    public Long getStuId() {
        return stuId;
    }

    /**
     * 设置学生
     *
     * @param stuId 学生
     */
    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    /**
     * 获取学校
     *
     * @return school_id - 学校
     */
    public Long getSchoolId() {
        return schoolId;
    }

    /**
     * 设置学校
     *
     * @param schoolId 学校
     */
    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }
}