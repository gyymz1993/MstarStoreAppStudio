package cn.mstar.store.entity;

import java.io.Serializable;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class ShoppingCartItem implements Serializable{


    public int cartId;
    public int number;
    public int proId;
    public int proSpecialId;
    public String specialTitle;
    public String name;
    public String pic;
    public Double price;
    public String ParentClassName, ClassName;
    public int stock;

    public ShoppingCartItem() {
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "cartId=" + cartId +
                ", number=" + number +
                ", proId=" + proId +
                ", proSpecialId=" + proSpecialId +
                ", specialTitle='" + specialTitle + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", price=" + price +
                ", ParentClassName='" + ParentClassName + '\'' +
                ", ClassName='" + ClassName + '\'' +
                '}';
    }

    public ShoppingCartItem(int cartId, int number, int proId, int proSpecialId, String specialTitle, String name, String pic, Double price, String parentClassName, String className) {
        this.cartId = cartId;
        this.number = number;
        this.proId = proId;
        this.proSpecialId = proSpecialId;
        this.specialTitle = specialTitle;
        this.name = name;
        this.pic = pic;
        this.price = price;
        ParentClassName = parentClassName;
        ClassName = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCartItem)) return false;

        ShoppingCartItem that = (ShoppingCartItem) o;

        if (cartId != that.cartId) return false;
        return proId == that.proId;

    }

    @Override
    public int hashCode() {
        int result = cartId;
        result = 31 * result + proId;
        return result;
    }
}
