
package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author yinhf
 */
public class PubReductionFee implements Serializable {
    
    private String pef_bussinessNum;//业务编号
    private String pef_meterNo; 
    private String pef_userName;
    private String pef_belongPar;  //所属母表
    private String pef_parType;
    private String pef_allotType;
    private String  pef_applyTime;
    private double pef_shareRadio;
    private double pef_shareSize;
    private double pef_shareFee;
    private double pef_refund;
    private double  pef_Total;
    private String pef_community;
    private String pef_buildNo;
    private String pef_unitNo;
    private String pef_roomNo;
    private String pef_rateType;
    private double pef_accountBalance;

    public String getPef_bussinessNum() {
        return pef_bussinessNum;
    }

    public void setPef_bussinessNum(String pef_bussinessNum) {
        this.pef_bussinessNum = pef_bussinessNum;
    }

    public String getPef_meterNo() {
        return pef_meterNo;
    }

    public void setPef_meterNo(String pef_meterNo) {
        this.pef_meterNo = pef_meterNo;
    }

    public String getPef_userName() {
        return pef_userName;
    }

    public void setPef_userName(String pef_userName) {
        this.pef_userName = pef_userName;
    }

    public String getPef_belongPar() {
        return pef_belongPar;
    }

    public void setPef_belongPar(String pef_belongPar) {
        this.pef_belongPar = pef_belongPar;
    }

    public String getPef_parType() {
        return pef_parType;
    }

    public void setPef_parType(String pef_parType) {
        this.pef_parType = pef_parType;
    }

    public String getPef_allotType() {
        return pef_allotType;
    }

    public void setPef_allotType(String pef_allotType) {
        this.pef_allotType = pef_allotType;
    }

    public String getPef_applyTime() {
        return pef_applyTime;
    }

    public void setPef_applyTime(String pef_applyTime) {
        this.pef_applyTime = pef_applyTime;
    }

    public double getPef_shareRadio() {
        return pef_shareRadio;
    }

    public void setPef_shareRadio(double pef_shareRadio) {
        this.pef_shareRadio = pef_shareRadio;
    }

    public double getPef_shareSize() {
        return pef_shareSize;
    }

    public void setPef_shareSize(double pef_shareSize) {
        this.pef_shareSize = pef_shareSize;
    }

    public double getPef_shareFee() {
        return pef_shareFee;
    }

    public void setPef_shareFee(double pef_shareFee) {
        this.pef_shareFee = pef_shareFee;
    }

    public double getPef_refund() {
        return pef_refund;
    }

    public void setPef_refund(double pef_refund) {
        this.pef_refund = pef_refund;
    }

    public double getPef_Total() {
        return pef_Total;
    }

    public void setPef_Total(double pef_Total) {
        this.pef_Total = pef_Total;
    }

    public String getPef_community() {
        return pef_community;
    }

    public void setPef_community(String pef_community) {
        this.pef_community = pef_community;
    }

    public String getPef_buildNo() {
        return pef_buildNo;
    }

    public void setPef_buildNo(String pef_buildNo) {
        this.pef_buildNo = pef_buildNo;
    }

    public String getPef_unitNo() {
        return pef_unitNo;
    }

    public void setPef_unitNo(String pef_unitNo) {
        this.pef_unitNo = pef_unitNo;
    }

    public String getPef_roomNo() {
        return pef_roomNo;
    }

    public void setPef_roomNo(String pef_roomNo) {
        this.pef_roomNo = pef_roomNo;
    }

    public String getPef_rateType() {
        return pef_rateType;
    }

    public void setPef_rateType(String pef_rateType) {
        this.pef_rateType = pef_rateType;
    }

    public double getPef_accountBalance() {
        return pef_accountBalance;
    }

    public void setPef_accountBalance(double pef_accountBalance) {
        this.pef_accountBalance = pef_accountBalance;
    }
    
}