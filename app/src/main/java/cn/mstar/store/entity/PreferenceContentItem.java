package cn.mstar.store.entity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class PreferenceContentItem {

    public int activity_id;
    public String name;
    public String pic;
    public String time;
    public String descript;

    public int getActivity_id() {
        return activity_id;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public String getTime() {
        return time;
    }

    public String getDescript() {
        return descript;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
