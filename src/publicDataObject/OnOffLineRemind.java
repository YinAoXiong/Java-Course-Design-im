package publicDataObject;

/**
 * @description: 提醒用户好友的上下线情况
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/7/5
 **/
public class OnOffLineRemind {
    private boolean onLine;
    private int userID;

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
