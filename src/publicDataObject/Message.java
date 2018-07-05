package publicDataObject;

/**
 * @description: 信息传送
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/5/28
 **/
public class Message {
    private String nickName;
    private int fromID;
    private int destinationID=-1;
    private String message;
    private boolean creatPrivateChat=false;

    public boolean isCreatPrivateChat() {
        return creatPrivateChat;
    }

    public void setCreatPrivateChat(boolean creatPrivateChat) {
        this.creatPrivateChat = creatPrivateChat;
    }

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(int destinationID) {
        this.destinationID = destinationID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
