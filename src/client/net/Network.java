package client.net;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @program: Java-Course-Design-im
 * @description: 网络操作与网络事件源
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018-05-23 21:00
 **/
public class Network implements Runnable{
    private CopyOnWriteArrayList<NetworkListener> listeners=new CopyOnWriteArrayList<>();
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
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            while (true){
                String message=dataInputStream.readUTF();
                //触发网络事件
                messageComming(message);
            }

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
    * @param: [Message]
    * @return: void
    * @author: 尹傲雄 yinaoxiong@gmail.com
    * @date: 2018/5/25
    */
    private void messageComming(String message){
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
    private synchronized void notifyLIsteners(NetworkEvent event){
        for (NetworkListener listener : listeners) {
            listener.networkEvent(event);
        }
    }
}
