package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_sockets")
public class Sockets {
    /**
     * 自增长id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户端ip地址
     */
    @Column(name = "socket_ip")
    private String socketIp;

    /**
     * 客户端上线时间
     */
    @Column(name = "socket_online_time")
    private Date socketOnlineTime;

    /**
     * 客户端下线时间
     */
    @Column(name = "socket_offline_time")
    private Date socketOfflineTime;

    /**
     * socket连接状态-1:上线  0:离线
     */
    @Column(name = "socket_state")
    private Integer socketState;

    /**
     * socket-用户id-外键
     */
    private Integer userid;
    
    
    @Transient
    private User user;

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
     * 获取客户端ip地址
     *
     * @return socket_ip - 客户端ip地址
     */
    public String getSocketIp() {
        return socketIp;
    }

    /**
     * 设置客户端ip地址
     *
     * @param socketIp 客户端ip地址
     */
    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    /**
     * 获取客户端上线时间
     *
     * @return socket_online_time - 客户端上线时间
     */
    public Date getSocketOnlineTime() {
        return socketOnlineTime;
    }

    /**
     * 设置客户端上线时间
     *
     * @param socketOnlineTime 客户端上线时间
     */
    public void setSocketOnlineTime(Date socketOnlineTime) {
        this.socketOnlineTime = socketOnlineTime;
    }

    /**
     * 获取客户端下线时间
     *
     * @return socket_offline_time - 客户端下线时间
     */
    public Date getSocketOfflineTime() {
        return socketOfflineTime;
    }

    /**
     * 设置客户端下线时间
     *
     * @param socketOfflineTime 客户端下线时间
     */
    public void setSocketOfflineTime(Date socketOfflineTime) {
        this.socketOfflineTime = socketOfflineTime;
    }

    /**
     * 获取socket连接状态-1:上线  0:离线
     *
     * @return socket_state - socket连接状态-1:上线  0:离线
     */
    public Integer getSocketState() {
        return socketState;
    }

    /**
     * 设置socket连接状态-1:上线  0:离线
     *
     * @param socketState socket连接状态-1:上线  0:离线
     */
    public void setSocketState(Integer socketState) {
        this.socketState = socketState;
    }

    /**
     * 获取socket-用户id-外键
     *
     * @return userid - socket-用户id-外键
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置socket-用户id-外键
     *
     * @param userid socket-用户id-外键
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
//    
    
}