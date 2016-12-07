package cn.mstar.store.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class Data implements Serializable{

    List<Shop> shops;

    class Shop {
        int shopId;
        String shopName;
        List<Goods> goods;
    }

    class Goods {
        int goodsId;
    }
}
