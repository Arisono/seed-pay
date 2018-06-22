package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_sockets_messages")
public class SocketsMessages {
    /**
     * 自增长id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 消息文本
     */
    @Column(name = "socket_data")
    private String socketData;

    /**
     * 消息接收： 0,发送时间：1
     */
    @Column(name = "socket_time")
    private Date socketTime;

    /**
     * 消息类型 0：接收1：发送
     */
    @Column(name = "socket_type")
    private Integer socketType;

    /**
     * 客户端socket唯一标识：外键
     */
    @Column(name = "socket_id")
    private String socketId;

    /**
     * 消息-用户id-外键
     */
    private Integer userid;

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
     * 获取消息文本
     *
     * @return socket_data - 消息文本
     */
    public String getSocketData() {
        return socketData;
    }

    /**
     * 设置消息文本
     *
     * @param socketData 消息文本
     */
    public void setSocketData(String socketData) {
        this.socketData = socketData;
    }

    /**
     * 获取消息接收： 0,发送时间：1
     *
     * @return socket_time - 消息接收： 0,发送时间：1
     */
    public Date getSocketTime() {
        return socketTime;
    }

    /**
     * 设置消息接收： 0,发送时间：1
     *
     * @param socketTime 消息接收： 0,发送时间：1
     */
    public void setSocketTime(Date socketTime) {
        this.socketTime = socketTime;
    }

    /**
     * 获取消息类型 0：接收1：发送
     *
     * @return socket_type - 消息类型 0：接收1：发送
     */
    public Integer getSocketType() {
        return socketType;
    }

    /**
     * 设置消息类型 0：接收1：发送
     *
     * @param socketType 消息类型 0：接收1：发送
     */
    public void setSocketType(Integer socketType) {
        this.socketType = socketType;
    }

    /**
     * 获取客户端socket唯一标识：外键
     *
     * @return socket_id - 客户端socket唯一标识：外键
     */
    public String getSocketId() {
        return socketId;
    }

    /**
     * 设置客户端socket唯一标识：外键
     *
     * @param socketId 客户端socket唯一标识：外键
     */
    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    /**
     * 获取消息-用户id-外键
     *
     * @return userid - 消息-用户id-外键
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置消息-用户id-外键
     *
     * @param userid 消息-用户id-外键
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}