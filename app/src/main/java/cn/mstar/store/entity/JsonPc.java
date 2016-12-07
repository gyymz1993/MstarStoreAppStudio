package cn.mstar.store.entity;

/**
 * Created by Administrator on 2015/10/13.
 */
public class JsonPc extends  JsonResult{

    public Pc data;

    public Pc getData() {
        return data;
    }

    public void setData(Pc data) {
        this.data = data;
    }

    public class Pc{
        String pic;

        public String getPic() {
            return pic;
        }
        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
