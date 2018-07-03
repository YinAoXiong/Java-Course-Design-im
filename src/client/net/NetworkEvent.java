package client.net;

import java.util.EventObject;

/**
 * @description: 网络事件
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/5/24
 **/
public class NetworkEvent extends EventObject {
    private String receiveString;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    NetworkEvent(Object source, String receiveString) {
        super(source);
        this.receiveString=receiveString;
    }

    /**
     * 返回网络接收到的对象
     * @return String
     */
    public String getReceiveString() {
        return receiveString;
    }
}
