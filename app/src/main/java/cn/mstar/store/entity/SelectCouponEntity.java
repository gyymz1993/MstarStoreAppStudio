package cn.mstar.store.entity;

import java.io.Serializable;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class SelectCouponEntity implements Serializable{
    private int voucherId;
    private int price;
    private String code;
    private String voucherInfo;
    private String storeName;
    private String endtime;

    public int getVoucherId() {
        return voucherId;
    }

    public int getPrice() {
        return price;
    }

    public String getCode() {
        return code;
    }

    public String getVoucherInfo() {
        return voucherInfo;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setVoucherInfo(String voucherInfo) {
        this.voucherInfo = voucherInfo;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
