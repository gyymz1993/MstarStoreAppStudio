package cn.mstar.store.entity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CouponEntity {
    private int voucherId;
    private int storeId;
    private String store_name;
    private float voucherLimit;
    private String voucherLimitMsg;
    private int voucherPrice;
    private int voucherState;
    private String voucherImg;
    private String validDate;

    public int getVoucherId() {
        return voucherId;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getStore_name() {
        return store_name;
    }

    public float getVoucherLimit() {
        return voucherLimit;
    }

    public String getVoucherLimitMsg() {
        return voucherLimitMsg;
    }

    public int getVoucherPrice() {
        return voucherPrice;
    }

    public int getVoucherState() {
        return voucherState;
    }

    public String getVoucherImg() {
        return voucherImg;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setVoucherLimit(float voucherLimit) {
        this.voucherLimit = voucherLimit;
    }

    public void setVoucherLimitMsg(String voucherLimitMsg) {
        this.voucherLimitMsg = voucherLimitMsg;
    }

    public void setVoucherPrice(int voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public void setVoucherState(int voucherState) {
        this.voucherState = voucherState;
    }

    public void setVoucherImg(String voucherImg) {
        this.voucherImg = voucherImg;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
