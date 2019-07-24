package com.shenke.Entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "t_paymentMessage")
public class PaymentMessage {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String medicalCardNumber;//就诊卡号

    @Column(length = 100)
    private String outTradeNo;//支付单号

    @Column(length = 100)
    private String payType;//支付方式

    @Column(nullable = true)
    private Double amount; //金额

    private Date payTime;//支付时间

    @Column(length = 100)
    private String detailId;//代缴费明细中的id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicalCardNumber() {
        return medicalCardNumber;
    }

    public void setMedicalCardNumber(String medicalCardNumber) {
        this.medicalCardNumber = medicalCardNumber;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "PaymentMessage{" +
                "id=" + id +
                ", medicalCardNumber='" + medicalCardNumber + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", payType='" + payType + '\'' +
                ", amount=" + amount +
                ", payTime=" + payTime +
                ", detailId='" + detailId + '\'' +
                '}';
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
