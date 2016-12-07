package cn.mstar.store.entity;

public class Winxin{

    //    "return_code":"SUCCESS","return_msg":"OK",
//            "appid":"wx9c2b1266bc3a5f2c",
//            "partnerid":"1267865701","noncestr":
//            "DeM1mumikxjVQoSS","sign":"13DE042E2D9E573D566C37EFABA2D23F",
//            "result_code":"FAIL","prepayid":"","trade_type":""


//  "appid":"wx9c2b1266bc3a5f2c",
// "partnerid":"1267865701",
//  "noncestr":"hog0u3un7k8ribxi6tw7khrcdcfu9har",
// "sign":"af302b58ab65a922e4590833a86dc65a",
//  "prepayid":"wx20151016093437aec24e87e60016224104",
// "trade_type":"APP",
// "timestamp":1444959275}
    public String return_code;
    public String return_msg;
    public String appid;
    public String partnerid;
    public String noncestr;
    public String sign;
    public String timestamp;
    public String prepayid;
    public String trade_type;
    public String result_code;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getReturn_code() {
        return return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getSign() {
        return sign;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    @Override
    public String toString() {
        return "Winxin{" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", result_code='" + result_code + '\'' +
                '}';
    }
}