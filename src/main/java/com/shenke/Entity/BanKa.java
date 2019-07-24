package com.shenke.Entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "t_banKa")
public class BanKa {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String medicalCardNumber;

    @Column(length = 20)
    private String name;

    @Column(length = 5)
    private String sex;

    @Column(length = 100)
    private String birthday;

    @Column(length = 100)
    private String address;

    @Column(length = 100)
    private String idcard;

    @Column(length = 100)
    private String phone;

    private Date date;

    @Override
    public String toString() {
        return "BanKa{" +
                "id=" + id +
                ", medicalCardNumber='" + medicalCardNumber + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", idcard='" + idcard + '\'' +
                ", phone='" + phone + '\'' +
                ", date=" + date +
                '}';
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

    public String getMedicalCardNumber() {
        return medicalCardNumber;
    }

    public void setMedicalCardNumber(String medicalCardNumber) {
        this.medicalCardNumber = medicalCardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
