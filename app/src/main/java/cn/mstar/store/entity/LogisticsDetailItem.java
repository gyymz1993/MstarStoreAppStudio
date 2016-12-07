package cn.mstar.store.entity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class LogisticsDetailItem {

    public String time;
    public String location;
    public String context;
    public String ftime;

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getContext() {
        return context;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
