package com.shenke.Entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "t_bangKa")
public class BangKa {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String phone;

    @Column(length = 20)
    private String sex;

    @Column(length = 100)
    private String address;

    @Column(length = 50)
    private String birthdate;

    @Column(length = 50)
    private String idCardNo;

    @Column(length = 100)
    private String medicalCardNumber;

    private Date date;

    private String createDate;

    @Override
    public String toString() {
        return "BangKa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", medicalCardNumber='" + medicalCardNumber + '\'' +
                ", date=" + date +
                ", createDate='" + createDate + '\'' +
                '}';
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex(Object sex) {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getMedicalCardNumber() {
        return medicalCardNumber;
    }

    public void setMedicalCardNumber(String medicalCardNumber) {
        this.medicalCardNumber = medicalCardNumber;
    }
}
