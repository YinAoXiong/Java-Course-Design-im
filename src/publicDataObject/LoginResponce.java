package publicDataObject;

import java.util.ArrayList;

/**
 * @description: 登录响应
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/5/28
 **/
public class LoginResponce {
    private boolean LoginSuccess;
    private String nickName;
    private ArrayList<Integer> onlineFriendID=null;

    public boolean isLoginSuccess() {
        return LoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        LoginSuccess = loginSuccess;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ArrayList<Integer> getOnlineFriendID() {
        return onlineFriendID;
    }

    public void setOnlineFriendID(ArrayList<Integer> onlineFriendID) {
        this.onlineFriendID = onlineFriendID;
    }
}
