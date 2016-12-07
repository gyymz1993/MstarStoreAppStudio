package cn.mstar.store.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/24.
 */
public class GoodsClassify implements Serializable{
    String gcName;
    String gcId;

    public String getGcName() {
        return gcName;
    }

    public void setGcName(String gcName) {
        this.gcName = gcName;
    }

    public String getGcId() {
        return gcId;
    }

    public void setGcId(String gcId) {
        this.gcId = gcId;
    }
}
