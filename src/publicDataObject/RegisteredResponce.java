package publicDataObject;

/**
 * @description: 注册响应
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/5/28
 **/
public class RegisteredResponce {
    private int userID;
    private boolean RegisteredSuccess;

    public boolean isRegisteredSuccess() {
        return RegisteredSuccess;
    }

    public void setRegisteredSuccess(boolean registeredSuccess) {
        RegisteredSuccess = registeredSuccess;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
