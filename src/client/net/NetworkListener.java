package client.net;

import java.util.EventListener;

/**
 * 网络事件监听接口
 */
public interface NetworkListener extends EventListener {
    /**
    * @description: 事件函数
    * @param: [event]
    * @return: void
    * @author: 尹傲雄 yinaoxiong@gmail.com
    * @date: 2018/5/25
    */
    void networkEvent(NetworkEvent event);
}