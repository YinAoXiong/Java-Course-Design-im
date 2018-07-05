package client.main;

import client.frame.LoginFrame;
import client.net.Network;
import client.util.RunTimePublicData;

/**
 * @program: Java-Course-Design-im
 * @description: 入口类
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018-05-23 20:30
 **/
public class Main {
    public static void main(String [] args){
        //初始化网络连接
        RunTimePublicData.network=new Network("127.0.0.1",6666);
        new LoginFrame();
    }
}
