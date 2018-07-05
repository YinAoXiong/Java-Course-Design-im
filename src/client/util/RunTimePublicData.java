package client.util;

import client.net.Network;

import java.util.ArrayList;

/**
 * @program: Java-Course-Design-im
 * @description: 用于储存客户端运行时共享的数据
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018-05-23 19:30
 **/
public class RunTimePublicData {
    /**用于储存用户的账号*/
    public static int account;
    /** 用于储存用户在登录时获得的token */
    public static String token;
    /** 用于储存用户的昵称*/
    public static String nickName;
    /**在线网友列表*/
    public static ArrayList<Integer> onlineFriend;
    /**公用网络对象*/
    public static Network network;

}
