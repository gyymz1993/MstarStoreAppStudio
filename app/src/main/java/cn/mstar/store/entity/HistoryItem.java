package cn.mstar.store.entity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class HistoryItem {
    public int proId;
    public String name;
    public String pic;
    public float price;
    public int browse_id;
    public String browse_time;

    public int getProId() {
        return proId;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public float getPrice() {
        return price;
    }

    public int getBrowse_id() {
        return browse_id;
    }

    public String getBrowse_time() {
        return browse_time;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setBrowse_id(int browse_id) {
        this.browse_id = browse_id;
    }

    public void setBrowse_time(String browse_time) {
        this.browse_time = browse_time;
    }
}
