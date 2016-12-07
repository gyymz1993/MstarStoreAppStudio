package cn.mstar.store.entity;

import java.util.List;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class LogisticsItem {
    public String e_code;
    public String shipping_code;
    public List<LogisticsDetailItem> shipInfo;

    public String getE_code() {
        return e_code;
    }

    public String getShpping_code() {
        return shipping_code;
    }

    public List<LogisticsDetailItem> getShipInfo() {
        return shipInfo;
    }

    public void setE_code(String e_code) {
        this.e_code = e_code;
    }

    public void setShpping_code(String shpping_code) {
        this.shipping_code = shpping_code;
    }

    public void setShipInfo(List<LogisticsDetailItem> shipInfo) {
        this.shipInfo = shipInfo;
    }
}
