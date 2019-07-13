package com.shenke.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_paymentPledge")
public class PaymentPledge {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String medicalCardNumber;

    @Column(length = 100)
    private String admissionNumber;

    @Column(nullable = true)
    private Double amount;

    @Column(length = 100)
    private String outTradeNo;

    @Column(length = 100)
    private String payType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;

    @Override
    public String toString() {
        return "PaymentPledge{" +
                "id=" + id +
                ", medicalCardNumber='" + medicalCardNumber + '\'' +
                ", admissionNumber='" + admissionNumber + '\'' +
                ", amount=" + amount +
                ", outTradeNo='" + outTradeNo + '\'' +
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

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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
