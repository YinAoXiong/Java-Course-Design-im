package publicDataObject;

/**
 * @description: 定义网络间的信息传输格式
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/5/25
 **/
public class Information {
    //描述动作，例如注册，登录
    private String action=null;
    private String data=null;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}
