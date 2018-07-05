package server;

import com.alibaba.fastjson.JSON;
import publicDataObject.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: Java-Course-Design-im
 * @description: 服务端主处理程序
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018-05-23 20:40
 **/
public class Server extends JFrame implements Runnable,ActionListener {
    private JTextField accountTextField=new JTextField();
    private JButton forceLogoutButton=new JButton();

    private ServerSocket serverSocket=null;
    //保存登录成功的客户端，线程与账号对应
    private Map<Integer,ChatThread> socketMap= new HashMap<>();
    private ArrayList<ChatThread> socketArray= new ArrayList<>();
    private Database database;

    public Server(){
        this.setTitle("服务器端");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        this.setResizable(false);
        this.setLayout(null);
        accountTextField.setBounds(50,0,200,40);
        forceLogoutButton.setBounds(50,200,200,40);
        forceLogoutButton.setText("强制下线");
        this.add(accountTextField);
        this.add(forceLogoutButton);
        this.setVisible(true);
        try {
            serverSocket=new ServerSocket(6666);
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        forceLogoutButton.addActionListener(this);
        //连接数据库
        database=new Database("java","root","111111");
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Information information=new Information();
        information.setAction("Logout");
        int userID=Integer.parseInt(accountTextField.getText());
        ChatThread temp=socketMap.get(userID);
        if(temp!=null){
            temp.sendMessage(JSON.toJSONString(information));
            temp.userID=-1;
            socketArray.add(temp);
            socketMap.remove(userID);

            //通知其他客户端
            OnOffLineRemind onOffLineRemind=new OnOffLineRemind();
            onOffLineRemind.setOnLine(false);
            onOffLineRemind.setUserID(userID);
            String data=JSON.toJSONString(onOffLineRemind);
            information.setAction("OnOffLineRemind");
            information.setData(data);
            sendGroup(JSON.toJSONString(information));
        }else {
            JOptionPane.showMessageDialog(this,"该用户没有上线");
        }
    }

    class ChatThread extends Thread{
        private int userID=-1;
        private Socket socket;
        private DataOutputStream dataOutputStream=null;
        private DataInputStream dataInputStream=null;

        ChatThread(Socket socket){
            this.socket=socket;
            try{
                dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        void sendMessage(String message){
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
                    System.out.println(message);
                    handleMessage(message);
                }
            }catch (Exception e){
                e.printStackTrace();
                //判断登录
                if(userID==-1){
                    socketArray.remove(this);
                }else {
                    socketMap.remove(userID);
                    OnOffLineRemind onOffLineRemind=new OnOffLineRemind();
                    onOffLineRemind.setOnLine(false);
                    onOffLineRemind.setUserID(userID);
                    String data=JSON.toJSONString(onOffLineRemind);
                    Information information=new Information();
                    information.setAction("OnOffLineRemind");
                    information.setData(data);
                    sendGroup(JSON.toJSONString(information));
                }
            }
        }

        /**
        * @description: 处理客户端发来的信息
        * @param: [message]
        * @return: void
        * @author: 尹傲雄 yinaoxiong@gmail.com
        * @date: 2018/7/3
        */
        private void handleMessage(String message){
            Information information=JSON.parseObject(message,Information.class);
            String action=information.getAction();
            String data=information.getData();
            switch (action) {
                case "Message":
                    Message message1 = JSON.parseObject(data, Message.class);
                    //判断是群发还是私聊
                    if (message1.getDestinationID() == -1) {
                        sendGroup(message);
                    } else {
                        socketMap.get(message1.getDestinationID()).sendMessage(message);
                    }
                    return;
                case "Login": {
                    Login login = JSON.parseObject(data, Login.class);
                    int userID = login.getUserID();
                    String password = login.getPassword();
                    //向数据库进行查询
                    String nickName = database.login(userID, password);
                    LoginResponce loginResponce = new LoginResponce();
                    //判断是否登录成功
                    if (nickName.length() == 0) {
                        loginResponce.setLoginSuccess(false);
                    } else {
                        this.userID = login.getUserID();
                        loginResponce.setLoginSuccess(true);
                        loginResponce.setNickName(nickName);
                        ArrayList<Integer> onlineFriendID = new ArrayList<>();
                        for (int key : socketMap.keySet()) onlineFriendID.add(key);
                        loginResponce.setOnlineFriendID(onlineFriendID);
                        //登录成功后向好友发送上线消息,同时从临时array中移入map中
                        OnOffLineRemind onOffLineRemind = new OnOffLineRemind();
                        onOffLineRemind.setOnLine(true);
                        onOffLineRemind.setUserID(userID);
                        data = JSON.toJSONString(onOffLineRemind);
                        information.setAction("OnOffLineRemind");
                        information.setData(data);
                        sendGroup(JSON.toJSONString(information));
                        socketMap.put(userID, this);
                        socketArray.remove(this);
                    }
                    data = JSON.toJSONString(loginResponce);
                    information.setData(data);
                    information.setAction("LoginResponce");
                    break;
                }
                case "Registered": {
                    Registered registered = JSON.parseObject(data, Registered.class);
                    int userID = database.registered(registered.getNickName(), registered.getPassword());
                    information.setAction("RegisteredResponce");
                    RegisteredResponce registeredResponce = new RegisteredResponce();
                    if (userID == -1) {
                        registeredResponce.setRegisteredSuccess(false);
                    } else {
                        registeredResponce.setRegisteredSuccess(true);
                        registeredResponce.setUserID(userID);
                    }
                    data = JSON.toJSONString(registeredResponce);
                    information.setData(data);
                    break;
                }
                case "Logout":
                    OnOffLineRemind onOffLineRemind = new OnOffLineRemind();
                    onOffLineRemind.setOnLine(false);
                    onOffLineRemind.setUserID(userID);
                    data = JSON.toJSONString(onOffLineRemind);
                    information.setAction("OnOffLineRemind");
                    information.setData(data);
                    socketArray.add(socketMap.get(userID));
                    socketMap.remove(userID);
                    userID = -1;
                    sendGroup(JSON.toJSONString(information));
                    return;
            }
            String sendData=JSON.toJSONString(information);
            sendMessage(sendData);
        }

    }




    public static void main(String[] args) {
       new Server();
    }

    @Override
    public void run() {
        try{
            while (true){
                Socket socket=serverSocket.accept();
                ChatThread chatThread=new ChatThread(socket);
                socketArray.add(chatThread);
                chatThread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendGroup(String message){
        for(ChatThread item:socketMap.values()){
            item.sendMessage(message);
        }
    }
}
