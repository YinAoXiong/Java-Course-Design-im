package publicDataObject;

/**
 * @description: 登录发送信息的规范
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/5/25
 **/
public class Login {
    private int userID;
    String password;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
