package client.net;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * @program: Java-Course-Design-im
 * @description: 网络操作与网络事件源
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018-05-23 21:00
 **/
public class Network implements Runnable{
    private Collection listeners;
    private Socket socket=null;
    private DataInputStream dataInputStream=null;
    private DataOutputStream dataOutputStream=null;

    public Network(String ip,int port){
        try {
            socket=new Socket(ip,port);
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void sendMessage(String message){
        try {
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            String message=dataInputStream.readUTF();
            //触发网络事件
            messageComming(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * @description: 添加监听器
     * @param: [listener]
     * @return: void
     * @author: 尹傲雄 yinaoxiong@gmail.com
     * @date: 2018/5/25
     */
    public void addNetworkListener(NetworkListener listener){
        if(listeners==null)
            listeners=new HashSet();
        listeners.add(listener);
    }

    /**
     * @description: 移除监听器
     * @param: [listener]
     * @return: void
     * @author: 尹傲雄 yinaoxiong@gmail.com
     * @date: 2018/5/25
     */
    public void removeNetwordLIstener(NetworkListener listener){
        if(listeners==null)
            return;
        listeners.remove(listener);
    }

    /**
    * @description: 触发网络事件
    * @param: [message]
    * @return: void
    * @author: 尹傲雄 yinaoxiong@gmail.com
    * @date: 2018/5/25
    */
    protected void messageComming(String message){
        if(listeners==null)
            return;
        NetworkEvent networkEvent=new NetworkEvent(this,message);
        notifyLIsteners(networkEvent);
    }

    /**
     * @description: 通知所有监听器
     * @param: [event]
     * @return: void
     * @author: 尹傲雄 yinaoxiong@gmail.com
     * @date: 2018/5/25
     */
    private void notifyLIsteners(NetworkEvent event){
        for (Object listener1 : listeners) {
            NetworkListener listener = (NetworkListener) listener1;
            listener.networkEvent(event);
        }
    }
}
