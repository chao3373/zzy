package com.shenke.Entity;

import javax.persistence.*;

@Entity
@Table(name = "t_charges")
public class Charges {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String chargeId;

    @Column(length = 100)
    private String quantity;

    @Column(length = 100)
    private String price;

    @Column(length = 100)
    private String chargeName;

    @Column(length = 100)
    private String frequencyName;

    @Column(length = 100)
    private String usageName;

    @Column(length = 100)
    private String dose;

    @Column(length = 100)
    private String groupId;

    @Column(length = 100)
    private String chargeType;

    @Column(length = 100)
    private String memo;

    @Column(length = 100)
    private String tradeNo;

    @Override
    public String toString() {
        return "Charges{" +
                "id=" + id +
                ", chargeId='" + chargeId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", chargeName='" + chargeName + '\'' +
                ", frequencyName='" + frequencyName + '\'' +
                ", usageName='" + usageName + '\'' +
                ", dose='" + dose + '\'' +
                ", groupId='" + groupId + '\'' +
                ", chargeType='" + chargeType + '\'' +
                ", memo='" + memo + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                '}';
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public String getUsageName() {
        return usageName;
    }

    public void setUsageName(String usageName) {
        this.usageName = usageName;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
