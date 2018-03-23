package com.joymeter.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayResult implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	    private int status;

	    private int cjoyId;

	    private String meterNo;

	    private BigDecimal money;

	    private String access_token;

	    private String style;

	    private String returnUrl;
     
     private String operateId;
     
     private String postUrl;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCjoyId() {
        return cjoyId;
    }

    public void setCjoyId(int cjoyId) {
        this.cjoyId = cjoyId;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }


    @Override
    public String toString() {
        return "PayResult [status=" + status + ", cjoyId=" + cjoyId + ", meterNo=" + meterNo + ", money=" + money
                + ", access_token=" + access_token + ", style=" + style + ", returnUrl=" + returnUrl + ", operateId="
                + operateId + ", postUrl=" + postUrl + "]";
    }
     
     
	    
}
