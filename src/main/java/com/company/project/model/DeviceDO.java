package com.company.project.model;

import javax.persistence.*;

@Table(name = "device")
public class DeviceDO {
    /**
     * 设备ID，自增长
     */
    @Id
    @Column(name = "deviceId")
    private Integer deviceid;

    /**
     * 设备名称
     */
    @Column(name = "deviceName")
    private String devicename;

    /**
     * 设备型号
     */
    @Column(name = "deviceModel")
    private String devicemodel;

    /**
     * 设备序列号
     */
    @Column(name = "serialNumber")
    private String serialnumber;

    /**
     * 设备IP
     */
    @Column(name = "deviceIp")
    private String deviceip;

    /**
     * 设备版本号
     */
    private String version;

    /**
     * 设备Mac地址
     */
    @Column(name = "deviceMac")
    private String devicemac;

    /**
     * 设备制造商
     */
    @Column(name = "deviceMaker")
    private String devicemaker;

    /**
     * 设备秘钥
     */
    private String secretkey;

    /**
     * 是否授权   0 未授权，1 授权
     */
    @Column(name = "isAuthorized")
    private Integer isauthorized;

    /**
     * 是否在线  0不在线  1在线
     */
    @Column(name = "isOnline")
    private Integer isonline;

    /**
     * 是否自动开闸模式  0 手动  1 自动
     */
    @Column(name = "isAutomatic")
    private Integer isautomatic;

    @Column(name = "schoolId")
    private Integer schoolid;

    /**
     * 获取设备ID，自增长
     *
     * @return deviceId - 设备ID，自增长
     */
    public Integer getDeviceid() {
        return deviceid;
    }

    /**
     * 设置设备ID，自增长
     *
     * @param deviceid 设备ID，自增长
     */
    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    /**
     * 获取设备名称
     *
     * @return deviceName - 设备名称
     */
    public String getDevicename() {
        return devicename;
    }

    /**
     * 设置设备名称
     *
     * @param devicename 设备名称
     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    /**
     * 获取设备型号
     *
     * @return deviceModel - 设备型号
     */
    public String getDevicemodel() {
        return devicemodel;
    }

    /**
     * 设置设备型号
     *
     * @param devicemodel 设备型号
     */
    public void setDevicemodel(String devicemodel) {
        this.devicemodel = devicemodel;
    }

    /**
     * 获取设备序列号
     *
     * @return serialNumber - 设备序列号
     */
    public String getSerialnumber() {
        return serialnumber;
    }

    /**
     * 设置设备序列号
     *
     * @param serialnumber 设备序列号
     */
    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    /**
     * 获取设备IP
     *
     * @return deviceIp - 设备IP
     */
    public String getDeviceip() {
        return deviceip;
    }

    /**
     * 设置设备IP
     *
     * @param deviceip 设备IP
     */
    public void setDeviceip(String deviceip) {
        this.deviceip = deviceip;
    }

    /**
     * 获取设备版本号
     *
     * @return version - 设备版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置设备版本号
     *
     * @param version 设备版本号
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 获取设备Mac地址
     *
     * @return deviceMac - 设备Mac地址
     */
    public String getDevicemac() {
        return devicemac;
    }

    /**
     * 设置设备Mac地址
     *
     * @param devicemac 设备Mac地址
     */
    public void setDevicemac(String devicemac) {
        this.devicemac = devicemac;
    }

    /**
     * 获取设备制造商
     *
     * @return deviceMaker - 设备制造商
     */
    public String getDevicemaker() {
        return devicemaker;
    }

    /**
     * 设置设备制造商
     *
     * @param devicemaker 设备制造商
     */
    public void setDevicemaker(String devicemaker) {
        this.devicemaker = devicemaker;
    }

    /**
     * 获取设备秘钥
     *
     * @return secretkey - 设备秘钥
     */
    public String getSecretkey() {
        return secretkey;
    }

    /**
     * 设置设备秘钥
     *
     * @param secretkey 设备秘钥
     */
    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    /**
     * 获取是否授权   0 未授权，1 授权
     *
     * @return isAuthorized - 是否授权   0 未授权，1 授权
     */
    public Integer getIsauthorized() {
        return isauthorized;
    }

    /**
     * 设置是否授权   0 未授权，1 授权
     *
     * @param isauthorized 是否授权   0 未授权，1 授权
     */
    public void setIsauthorized(Integer isauthorized) {
        this.isauthorized = isauthorized;
    }

    /**
     * 获取是否在线  0不在线  1在线
     *
     * @return isOnline - 是否在线  0不在线  1在线
     */
    public Integer getIsonline() {
        return isonline;
    }

    /**
     * 设置是否在线  0不在线  1在线
     *
     * @param isonline 是否在线  0不在线  1在线
     */
    public void setIsonline(Integer isonline) {
        this.isonline = isonline;
    }

    /**
     * 获取是否自动开闸模式  0 手动  1 自动
     *
     * @return isAutomatic - 是否自动开闸模式  0 手动  1 自动
     */
    public Integer getIsautomatic() {
        return isautomatic;
    }

    /**
     * 设置是否自动开闸模式  0 手动  1 自动
     *
     * @param isautomatic 是否自动开闸模式  0 手动  1 自动
     */
    public void setIsautomatic(Integer isautomatic) {
        this.isautomatic = isautomatic;
    }

    /**
     * @return schoolId
     */
    public Integer getSchoolid() {
        return schoolid;
    }

    /**
     * @param schoolid
     */
    public void setSchoolid(Integer schoolid) {
        this.schoolid = schoolid;
    }
}