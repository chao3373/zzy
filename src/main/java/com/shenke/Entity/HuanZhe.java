package com.shenke.Entity;

import javax.persistence.*;

/***
 * 患者信息
 */
@Entity
@Table(name = "t_huanZhe")
public class HuanZhe {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String patName;

    @Column(length = 100)
    private String general;

    @Column(length = 100)
    private String phone;

    @Column(length = 100)
    private String idCard;

    @Column(length = 100)
    private String birthday;

    @Column(length = 100)
    private String diagnoseName;

    @Column(length = 100)
    private String tradeNo;

    @Column(length = 100)
    private String tradeTime;

    @Column(length = 100)
    private String billDocHospital;

    @Column(length = 100)
    private String billDocName;

    @Column(length = 100)
    private String billDocCode;

    @Column(length = 100)
    private String medicalCardNumber;

    @Column(length = 100)
    private String total;

    @Override
    public String toString() {
        return "HuanZhe{" +
                "id=" + id +
                ", patName='" + patName + '\'' +
                ", general='" + general + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", birthday='" + birthday + '\'' +
                ", diagnoseName='" + diagnoseName + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", billDocHospital='" + billDocHospital + '\'' +
                ", billDocName='" + billDocName + '\'' +
                ", billDocCode='" + billDocCode + '\'' +
                ", medicalCardNumber='" + medicalCardNumber + '\'' +
                ", total='" + total + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDiagnoseName() {
        return diagnoseName;
    }

    public void setDiagnoseName(String diagnoseName) {
        this.diagnoseName = diagnoseName;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getBillDocHospital() {
        return billDocHospital;
    }

    public void setBillDocHospital(String billDocHospital) {
        this.billDocHospital = billDocHospital;
    }

    public String getBillDocName() {
        return billDocName;
    }

    public void setBillDocName(String billDocName) {
        this.billDocName = billDocName;
    }

    public String getBillDocCode() {
        return billDocCode;
    }

    public void setBillDocCode(String billDocCode) {
        this.billDocCode = billDocCode;
    }

    public String getMedicalCardNumber() {
        return medicalCardNumber;
    }

    public void setMedicalCardNumber(String medicalCardNumber) {
        this.medicalCardNumber = medicalCardNumber;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
