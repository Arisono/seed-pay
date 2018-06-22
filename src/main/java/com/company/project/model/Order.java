package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_order")
public class Order {
   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商户订单id
     */
    @Id
    @Column(name = "orderId")
    private String orderid;

    /**
     * 支付平台订单id
     */
    @Column(name = "transactionId")
    private String transactionid;

    /**
     * 用户id
     */
    @Column(name = "userId")
    private String userid;
    
    
    /**
     * 支付标识 支付宝,微信,小程序
     */
    @Column(name = "payType")
    private String paytype;

    /**
     * 交易状态
     */
    @Column(name = "tradeState")
    private Integer tradestate;

    /**
     * 订单创建时间
     */
    @Column(name = "time_create")
    private Date timeCreate;

    /**
     * 订单金额
     */
    private Float  fee;

    /**
     * 退款金额
     */
    @Column(name = "refundFee")
    private Double refundfee;

    /**
     * 支付完成时间
     */
    @Column(name = "time_payment")
    private Date timePayment;

    /**
     * 退款商户订单号
     */
    @Column(name = "orderRefundId")
    private String orderrefundid;

    /**
     * 支付平台退款订单号
     */
    @Column(name = "refundId")
    private String refundid;

    /**
     * 退款完成时间
     */
    @Column(name = "time_refund")
    private Date timeRefund;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取商户订单id
     *
     * @return orderId - 商户订单id
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * 设置商户订单id
     *
     * @param orderid 商户订单id
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    /**
     * 获取支付平台订单id
     *
     * @return transactionId - 支付平台订单id
     */
    public String getTransactionid() {
        return transactionid;
    }

    /**
     * 设置支付平台订单id
     *
     * @param transactionid 支付平台订单id
     */
    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    /**
     * 获取用户id
     *
     * @return userId - 用户id
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    

    public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	/**
     * 获取交易状态
     *
     * @return tradeState - 交易状态
     */
    public Integer getTradestate() {
        return tradestate;
    }

    /**
     * 设置交易状态
     *
     * @param tradestate 交易状态
     */
    public void setTradestate(Integer tradestate) {
        this.tradestate = tradestate;
    }

    /**
     * 获取订单创建时间
     *
     * @return time_create - 订单创建时间
     */
    public Date getTimeCreate() {
        return timeCreate;
    }

    /**
     * 设置订单创建时间
     *
     * @param timeCreate 订单创建时间
     */
    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    /**
     * 获取订单金额
     *
     * @return fee - 订单金额
     */
    public Float getFee() {
        return fee;
    }

    /**
     * 设置订单金额
     *
     * @param fee 订单金额
     */
    public void setFee(Float fee) {
        this.fee = fee;
    }

    /**
     * 获取退款金额
     *
     * @return refundFee - 退款金额
     */
    public Double getRefundfee() {
        return refundfee;
    }

    /**
     * 设置退款金额
     *
     * @param refundfee 退款金额
     */
    public void setRefundfee(Double refundfee) {
        this.refundfee = refundfee;
    }

    /**
     * 获取支付完成时间
     *
     * @return time_payment - 支付完成时间
     */
    public Date getTimePayment() {
        return timePayment;
    }

    /**
     * 设置支付完成时间
     *
     * @param timePayment 支付完成时间
     */
    public void setTimePayment(Date timePayment) {
        this.timePayment = timePayment;
    }

    /**
     * 获取退款商户订单号
     *
     * @return orderRefundId - 退款商户订单号
     */
    public String getOrderrefundid() {
        return orderrefundid;
    }

    /**
     * 设置退款商户订单号
     *
     * @param orderrefundid 退款商户订单号
     */
    public void setOrderrefundid(String orderrefundid) {
        this.orderrefundid = orderrefundid;
    }

    /**
     * 获取支付平台退款订单号
     *
     * @return refundId - 支付平台退款订单号
     */
    public String getRefundid() {
        return refundid;
    }

    /**
     * 设置支付平台退款订单号
     *
     * @param refundid 支付平台退款订单号
     */
    public void setRefundid(String refundid) {
        this.refundid = refundid;
    }

    /**
     * 获取退款完成时间
     *
     * @return time_refund - 退款完成时间
     */
    public Date getTimeRefund() {
        return timeRefund;
    }

    /**
     * 设置退款完成时间
     *
     * @param timeRefund 退款完成时间
     */
    public void setTimeRefund(Date timeRefund) {
        this.timeRefund = timeRefund;
    }

    /**
     * 获取商品描述
     *
     * @return body - 商品描述
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置商品描述
     *
     * @param body 商品描述
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 获取商品详情
     *
     * @return detail - 商品详情
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置商品详情
     *
     * @param detail 商品详情
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * 获取附加数据
     *
     * @return attach - 附加数据
     */
    public String getAttach() {
        return attach;
    }

    /**
     * 设置附加数据
     *
     * @param attach 附加数据
     */
    public void setAttach(String attach) {
        this.attach = attach;
    }
}