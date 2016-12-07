package cn.mstar.store.entity;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class ShopEntity {

    private String store_id;
    private String store_tel;
    private String store_name;
    private String store_label;
    private Good goods_count;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_tel() {
        return store_tel;
    }

    public void setStore_tel(String store_tel) {
        this.store_tel = store_tel;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_label() {
        return store_label;
    }

    public void setStore_label(String store_label) {
        this.store_label = store_label;
    }

    public Good getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(Good goods_count) {
        this.goods_count = goods_count;
    }

    public class Good {
        private String proId_2;
        private String proId_1;

        public Good(String proId, String proNum) {
            proId_2 = proId;
            proId_1 = proNum;
        }

        public String getProId_2() {
            return proId_2;
        }

        public void setProId_2(String proId_2) {
            this.proId_2 = proId_2;
        }

        public String getProId_1() {
            return proId_1;
        }

        public void setProId_1(String proId_1) {
            this.proId_1 = proId_1;
        }

        @Override
        public String toString() {
            return "Good{" +
                    "proId_2='" + proId_2 + '\'' +
                    ", proId_12='" + proId_1 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShopEntity{" +
                "store_id='" + store_id + '\'' +
                ", store_tel='" + store_tel + '\'' +
                ", store_name='" + store_name + '\'' +
                ", store_label='" + store_label + '\'' +
                ", goods_count=" + goods_count +
                '}';
    }
}
