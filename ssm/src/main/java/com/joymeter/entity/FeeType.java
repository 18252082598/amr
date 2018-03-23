package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class FeeType implements Serializable {

    private static final long serialVersionUID = 6853824540333970999L;

    private Long feeTypeId;
    private String feeTypeName;
    private String meterType;
    private Double basicUnitPrice;//基本单价
    private Double disposingUnitCost;//排污费
    private Double otherCost;//其他费用
    private Double totalUnitCost;//合计单价
    private Double extraCost;//附加费
    private String paymentMethod;//结算周期
    private String isLevelPrice;//
    private Double levelOneStartVolume;//
    private Double levelOneUnitPrice;//
    private Double levelOneTotalPrice;//
    private Double levelTwoStartVolume;//
    private Double levelTwoUnitPrice;//
    private Double levelTwoTotalPrice;//
    private Double levelThreeStartVolume;//
    private Double levelThreeUnitPrice;//
    private Double levelThreeTotalPrice;//
    private Double levelFourStartVolume;//
    private Double levelFourUnitPrice;//
    private Double levelFourTotalPrice;//
    private Double levelFiveStartVolume;//
    private Double levelFiveUnitPrice;//
    private Double levelFiveTotalPrice;//
    private String feeTypeStatus;//状态
    private String operatorName;//操作员
    private Timestamp addTime;//添加时间

    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public String getFeeTypeName() {
        return feeTypeName;
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public Double getBasicUnitPrice() {
        return basicUnitPrice;
    }

    public void setBasicUnitPrice(Double basicUnitPrice) {
        this.basicUnitPrice = basicUnitPrice;
    }

    public Double getDisposingUnitCost() {
        return disposingUnitCost;
    }

    public void setDisposingUnitCost(Double disposingUnitCost) {
        this.disposingUnitCost = disposingUnitCost;
    }

    public Double getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(Double otherCost) {
        this.otherCost = otherCost;
    }

    public Double getTotalUnitCost() {
        return totalUnitCost;
    }

    public void setTotalUnitCost(Double totalUnitCost) {
        this.totalUnitCost = totalUnitCost;
    }

    public Double getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(Double extraCost) {
        this.extraCost = extraCost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getIsLevelPrice() {
        return isLevelPrice;
    }

    public void setIsLevelPrice(String isLevelPrice) {
        this.isLevelPrice = isLevelPrice;
    }

    public Double getLevelOneStartVolume() {
        return levelOneStartVolume;
    }

    public void setLevelOneStartVolume(Double levelOneStartVolume) {
        this.levelOneStartVolume = levelOneStartVolume;
    }

    public Double getLevelOneUnitPrice() {
        return levelOneUnitPrice;
    }

    public void setLevelOneUnitPrice(Double levelOneUnitPrice) {
        this.levelOneUnitPrice = levelOneUnitPrice;
    }

    public Double getLevelOneTotalPrice() {
        return levelOneTotalPrice;
    }

    public void setLevelOneTotalPrice(Double levelOneTotalPrice) {
        this.levelOneTotalPrice = levelOneTotalPrice;
    }

    public Double getLevelTwoStartVolume() {
        return levelTwoStartVolume;
    }

    public void setLevelTwoStartVolume(Double levelTwoStartVolume) {
        this.levelTwoStartVolume = levelTwoStartVolume;
    }

    public Double getLevelTwoUnitPrice() {
        return levelTwoUnitPrice;
    }

    public void setLevelTwoUnitPrice(Double levelTwoUnitPrice) {
        this.levelTwoUnitPrice = levelTwoUnitPrice;
    }

    public Double getLevelTwoTotalPrice() {
        return levelTwoTotalPrice;
    }

    public void setLevelTwoTotalPrice(Double levelTwoTotalPrice) {
        this.levelTwoTotalPrice = levelTwoTotalPrice;
    }

    public Double getLevelThreeStartVolume() {
        return levelThreeStartVolume;
    }

    public void setLevelThreeStartVolume(Double levelThreeStartVolume) {
        this.levelThreeStartVolume = levelThreeStartVolume;
    }

    public Double getLevelThreeUnitPrice() {
        return levelThreeUnitPrice;
    }

    public void setLevelThreeUnitPrice(Double levelThreeUnitPrice) {
        this.levelThreeUnitPrice = levelThreeUnitPrice;
    }

    public Double getLevelThreeTotalPrice() {
        return levelThreeTotalPrice;
    }

    public void setLevelThreeTotalPrice(Double levelThreeTotalPrice) {
        this.levelThreeTotalPrice = levelThreeTotalPrice;
    }

    public Double getLevelFourStartVolume() {
        return levelFourStartVolume;
    }

    public void setLevelFourStartVolume(Double levelFourStartVolume) {
        this.levelFourStartVolume = levelFourStartVolume;
    }

    public Double getLevelFourUnitPrice() {
        return levelFourUnitPrice;
    }

    public void setLevelFourUnitPrice(Double levelFourUnitPrice) {
        this.levelFourUnitPrice = levelFourUnitPrice;
    }

    public Double getLevelFourTotalPrice() {
        return levelFourTotalPrice;
    }

    public void setLevelFourTotalPrice(Double levelFourTotalPrice) {
        this.levelFourTotalPrice = levelFourTotalPrice;
    }

    public Double getLevelFiveStartVolume() {
        return levelFiveStartVolume;
    }

    public void setLevelFiveStartVolume(Double levelFiveStartVolume) {
        this.levelFiveStartVolume = levelFiveStartVolume;
    }

    public Double getLevelFiveUnitPrice() {
        return levelFiveUnitPrice;
    }

    public void setLevelFiveUnitPrice(Double levelFiveUnitPrice) {
        this.levelFiveUnitPrice = levelFiveUnitPrice;
    }

    public Double getLevelFiveTotalPrice() {
        return levelFiveTotalPrice;
    }

    public void setLevelFiveTotalPrice(Double levelFiveTotalPrice) {
        this.levelFiveTotalPrice = levelFiveTotalPrice;
    }

    public String getFeeTypeStatus() {
        return feeTypeStatus;
    }

    public void setFeeTypeStatus(String feeTypeStatus) {
        this.feeTypeStatus = feeTypeStatus;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

}
