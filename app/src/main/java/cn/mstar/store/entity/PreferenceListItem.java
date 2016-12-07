package cn.mstar.store.entity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class PreferenceListItem {
    private int proId;
    private String name;
    private String pic;
    private float price;
    private float mkprice;
    private int sales_way;
    private int categoryId;

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

    public float getMkprice() {
        return mkprice;
    }

    public int getSales_way() {
        return sales_way;
    }

    public int getCategoryId() {
        return categoryId;
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

    public void setMkprice(float mkprice) {
        this.mkprice = mkprice;
    }

    public void setSales_way(int sales_way) {
        this.sales_way = sales_way;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
