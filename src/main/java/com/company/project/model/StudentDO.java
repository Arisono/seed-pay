package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_student")
public class StudentDO {
    /**
     * 编号
     */
    @Id
    @Column(name = "stu_id")
    private Long stuId;

    /**
     * 学号
     */
    @Column(name = "stu_number")
    private String stuNumber;

    /**
     * 名字
     */
    @Column(name = "stu_name")
    private String stuName;

    /**
     * 入学日期
     */
    @Column(name = "stu_enroll_date")
    private Date stuEnrollDate;

    /**
     * 毕业时间
     */
    @Column(name = "stu_graduate_date")
    private Date stuGraduateDate;

    /**
     * 生日
     */
    @Column(name = "stu_birthday")
    private Date stuBirthday;

    /**
     * 年龄
     */
    @Column(name = "stu_age")
    private Integer stuAge;

    /**
     * 性别   1男  2女
     */
    @Column(name = "stu_sex")
    private Integer stuSex;

    /**
     * 地址
     */
    @Column(name = "stu_address")
    private String stuAddress;

    /**
     * 头像
     */
    @Column(name = "stu_photo")
    private String stuPhoto;

    /**
     * 状态   1正常   2冻结
     */
    @Column(name = "stu_status")
    private Integer stuStatus;

    /**
     * 备注
     */
    @Column(name = "stu_remarks")
    private String stuRemarks;

    /**
     * 班级
     */
    @Column(name = "clazz_id")
    private Long clazzId;

    /**
     * 学校
     */
    @Column(name = "school_id")
    private Long schoolId;

    /**
     * 获取编号
     *
     * @return stu_id - 编号
     */
    public Long getStuId() {
        return stuId;
    }

    /**
     * 设置编号
     *
     * @param stuId 编号
     */
    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    /**
     * 获取学号
     *
     * @return stu_number - 学号
     */
    public String getStuNumber() {
        return stuNumber;
    }

    /**
     * 设置学号
     *
     * @param stuNumber 学号
     */
    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    /**
     * 获取名字
     *
     * @return stu_name - 名字
     */
    public String getStuName() {
        return stuName;
    }

    /**
     * 设置名字
     *
     * @param stuName 名字
     */
    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    /**
     * 获取入学日期
     *
     * @return stu_enroll_date - 入学日期
     */
    public Date getStuEnrollDate() {
        return stuEnrollDate;
    }

    /**
     * 设置入学日期
     *
     * @param stuEnrollDate 入学日期
     */
    public void setStuEnrollDate(Date stuEnrollDate) {
        this.stuEnrollDate = stuEnrollDate;
    }

    /**
     * 获取毕业时间
     *
     * @return stu_graduate_date - 毕业时间
     */
    public Date getStuGraduateDate() {
        return stuGraduateDate;
    }

    /**
     * 设置毕业时间
     *
     * @param stuGraduateDate 毕业时间
     */
    public void setStuGraduateDate(Date stuGraduateDate) {
        this.stuGraduateDate = stuGraduateDate;
    }

    /**
     * 获取生日
     *
     * @return stu_birthday - 生日
     */
    public Date getStuBirthday() {
        return stuBirthday;
    }

    /**
     * 设置生日
     *
     * @param stuBirthday 生日
     */
    public void setStuBirthday(Date stuBirthday) {
        this.stuBirthday = stuBirthday;
    }

    /**
     * 获取年龄
     *
     * @return stu_age - 年龄
     */
    public Integer getStuAge() {
        return stuAge;
    }

    /**
     * 设置年龄
     *
     * @param stuAge 年龄
     */
    public void setStuAge(Integer stuAge) {
        this.stuAge = stuAge;
    }

    /**
     * 获取性别   1男  2女
     *
     * @return stu_sex - 性别   1男  2女
     */
    public Integer getStuSex() {
        return stuSex;
    }

    /**
     * 设置性别   1男  2女
     *
     * @param stuSex 性别   1男  2女
     */
    public void setStuSex(Integer stuSex) {
        this.stuSex = stuSex;
    }

    /**
     * 获取地址
     *
     * @return stu_address - 地址
     */
    public String getStuAddress() {
        return stuAddress;
    }

    /**
     * 设置地址
     *
     * @param stuAddress 地址
     */
    public void setStuAddress(String stuAddress) {
        this.stuAddress = stuAddress;
    }

    /**
     * 获取头像
     *
     * @return stu_photo - 头像
     */
    public String getStuPhoto() {
        return stuPhoto;
    }

    /**
     * 设置头像
     *
     * @param stuPhoto 头像
     */
    public void setStuPhoto(String stuPhoto) {
        this.stuPhoto = stuPhoto;
    }

    /**
     * 获取状态   1正常   2冻结
     *
     * @return stu_status - 状态   1正常   2冻结
     */
    public Integer getStuStatus() {
        return stuStatus;
    }

    /**
     * 设置状态   1正常   2冻结
     *
     * @param stuStatus 状态   1正常   2冻结
     */
    public void setStuStatus(Integer stuStatus) {
        this.stuStatus = stuStatus;
    }

    /**
     * 获取备注
     *
     * @return stu_remarks - 备注
     */
    public String getStuRemarks() {
        return stuRemarks;
    }

    /**
     * 设置备注
     *
     * @param stuRemarks 备注
     */
    public void setStuRemarks(String stuRemarks) {
        this.stuRemarks = stuRemarks;
    }

    /**
     * 获取班级
     *
     * @return clazz_id - 班级
     */
    public Long getClazzId() {
        return clazzId;
    }

    /**
     * 设置班级
     *
     * @param clazzId 班级
     */
    public void setClazzId(Long clazzId) {
        this.clazzId = clazzId;
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