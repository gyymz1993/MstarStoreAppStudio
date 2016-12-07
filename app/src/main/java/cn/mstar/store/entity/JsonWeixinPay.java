package cn.mstar.store.entity;

import java.util.Date;

/**
 * Created by Administrator on 2015/10/12.
 */
public class JsonWeixinPay extends  JsonResult{

    public Winxin data;

    public Winxin getData() {
        return data;
    }

    public void setData(Winxin data) {
        this.data = data;
    }
}


