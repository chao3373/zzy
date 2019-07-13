package com.shenke.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_cardRecharge")
public class CardRecharge {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String medicalCardNumber;

    @Column(length = 100)
    private String outTradeNo;

    @Column(nullable = true)
    private Double payAmount;

    @Column(length = 100)
    private String payType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;

    @Override
    public String toString() {
        return "CardRecharge{" +
                "id=" + id +
                ", medicalCardNumber='" + medicalCardNumber + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", payAmount=" + payAmount +
                ", payType='" + payType + '\'' +
                ", payTime=" + payTime +
                '}';
    }

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

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
