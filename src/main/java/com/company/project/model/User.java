package com.company.project.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Table(name = "tbl_user")
public class User {
    /**
     * 自增长id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户登录账户-唯一性约束
     */
    private String username;

    /**
     * 密码-MD5存储
     */
    private String password;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 手机号-唯一性约束
     */
    private String phone;

    /**
     * 邮箱-唯一性约束
     */
    private String email;

    /**
     * 注册时间
     */
    @Column(name = "register_date")
    private Date registerDate;
    
    
    @Transient
    private List<Sockets> sockets;

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
     * 获取用户登录账户-唯一性约束
     *
     * @return username - 用户登录账户-唯一性约束
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户登录账户-唯一性约束
     *
     * @param username 用户登录账户-唯一性约束
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码-MD5存储
     *
     * @return password - 密码-MD5存储
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码-MD5存储
     *
     * @param password 密码-MD5存储
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取性别
     *
     * @return sex - 性别
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取手机号-唯一性约束
     *
     * @return phone - 手机号-唯一性约束
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号-唯一性约束
     *
     * @param phone 手机号-唯一性约束
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取邮箱-唯一性约束
     *
     * @return email - 邮箱-唯一性约束
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱-唯一性约束
     *
     * @param email 邮箱-唯一性约束
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取注册时间
     *
     * @return register_date - 注册时间
     */
    public Date getRegisterDate() {
        return registerDate;
    }

    /**
     * 设置注册时间
     *
     * @param registerDate 注册时间
     */
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

	public List<Sockets> getSockets() {
		return sockets;
	}

	public void setSockets(List<Sockets> sockets) {
		this.sockets = sockets;
	}
    
    
}