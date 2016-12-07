package cn.mstar.store.entity;

/**
 * Created by Administrator on 2015/9/22.
 */
public class ProInfo {
        String proId;
        String name;
        String pic;
        String price;
        String mkprice;
        String categoryId;

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMkprice() {
        return mkprice;
    }

    public void setMkprice(String mkprice) {
        this.mkprice = mkprice;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
