package cn.mstar.store.entity;

/**
 * Created by Administrator on 2015/10/9.
 */
public class JsonResult {

   // {"response":"","error":"0","message":"\u8bc4\u4ef7\u6210\u529f","data":""}
    public String response;
    public String error;
    public String message;


    public void setResponse(String response) {
        this.response = response;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }


}
