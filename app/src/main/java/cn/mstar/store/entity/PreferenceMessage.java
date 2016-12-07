package cn.mstar.store.entity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class PreferenceMessage {
    public int totalNum;
    public String descript;
    public String time;

    public int getTotalNum() {
        return totalNum;
    }

    public String getDescript() {
        return descript;
    }

    public String getTime() {
        return time;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
