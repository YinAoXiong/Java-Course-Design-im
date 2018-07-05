package client.frame;

import client.net.NetworkEvent;
import client.net.NetworkListener;
import client.util.GUIUtil;
import client.util.RunTimePublicData;
import com.alibaba.fastjson.JSON;
import publicDataObject.Information;
import publicDataObject.Logout;
import publicDataObject.Message;
import publicDataObject.OnOffLineRemind;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @description: 聊天界面
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/7/5
 **/
public class ChatFrame extends JFrame implements ActionListener, NetworkListener {
    private JTextArea messageArea = new JTextArea("以下是聊天记录\n");
    private JTextArea inputField = new JTextArea();
    private JButton sendButton = new JButton();
    private JComboBox onlineFriendComboBox = new JComboBox();
    private JButton logoutButton = new JButton();
    private JButton privateSend=new JButton();

    public ChatFrame(ArrayList<Integer> onlineFriend,String Title) {
        for (int item : onlineFriend) {
            onlineFriendComboBox.addItem(item);
        }
        this.setTitle(Title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(1000, 1000);
        this.add(messageArea);
        this.add(inputField);
        this.add(sendButton);
        this.add(onlineFriendComboBox);
        this.add(logoutButton);
        this.add(privateSend);
        messageArea.setBounds(0, 0, 600, 600);
        messageArea.setEditable(false);
        inputField.setBounds(0, 700, 400, 20);
        sendButton.setBounds(430, 700, 100, 40);
        sendButton.setText("发送");
        onlineFriendComboBox.setBounds(700, 500, 200, 30);
        logoutButton.setText("下线");
        logoutButton.setBounds(700, 900, 100, 40);
        privateSend.setText("私聊");
        privateSend.setBounds(700,600,200,30);
        //添加监听器
        sendButton.addActionListener(this::actionPerformed);
        logoutButton.addActionListener(this::actionPerformed);
        privateSend.addActionListener(this::actionPerformed);
        RunTimePublicData.network.addNetworkListener(this::networkEvent);
        GUIUtil.toCenter(this);
        this.setVisible(true);

    }

    public static void main(String[] arg) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
//        new ChatFrame(list);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            Message message = new Message();
            message.setMessage(inputField.getText());
            inputField.setText("");
            message.setNickName(RunTimePublicData.nickName);
            message.setFromID(RunTimePublicData.account);
            String data = JSON.toJSONString(message);
            Information information = new Information();
            information.setAction("Message");
            information.setData(data);
            String sendData = JSON.toJSONString(information);
            RunTimePublicData.network.sendMessage(sendData);
        } else if(e.getSource()==logoutButton) {
            Information information = new Information();
            information.setAction("Logout");
            Logout logout = new Logout();
            logout.setUserID(RunTimePublicData.account);
            String data = JSON.toJSONString(logout);
            information.setData(data);

            RunTimePublicData.network.sendMessage(JSON.toJSONString(information));
            System.exit(0);
        }else if(e.getSource()==privateSend){
            int userID=(int)onlineFriendComboBox.getSelectedItem();
            Information information=new Information();
            information.setAction("Message");
            Message message=new Message();
            message.setDestinationID(userID);
            message.setFromID(RunTimePublicData.account);
            message.setNickName(RunTimePublicData.nickName);
            message.setCreatPrivateChat(true);
            String data=JSON.toJSONString(message);
            information.setData(data);
            RunTimePublicData.network.sendMessage(JSON.toJSONString(information));
            new PrivateChat(userID);
        }

    }

    /**
     * @param event
     * @description: 事件函数
     * @param: [event]
     * @return: void
     * @author: 尹傲雄 yinaoxiong@gmail.com
     * @date: 2018/5/25
     */
    @Override
    public void networkEvent(NetworkEvent event) {
        System.out.println(event.getReceiveString());
        Information information = JSON.parseObject(event.getReceiveString(), Information.class);
        String action = information.getAction();
        String data = information.getData();
        if (action.equals("Message")) {
            Message message = JSON.parseObject(data, Message.class);
            //判断是否为群发消息
            if(message.getDestinationID()!=-1){
                if(message.isCreatPrivateChat()){
                    new PrivateChat(message.getFromID());
                }
                return;
            }
            messageArea.append(message.getNickName() + " " + message.getFromID() + "说：\n");
            messageArea.append(message.getMessage()+"\n");
        } else if (action.equals("OnOffLineRemind")) {
            OnOffLineRemind onOffLineRemind = JSON.parseObject(data, OnOffLineRemind.class);
            int userID = onOffLineRemind.getUserID();
            if (onOffLineRemind.isOnLine()) {
                RunTimePublicData.onlineFriend.add(userID);
                onlineFriendComboBox.addItem(userID);
            } else {
                for (int i=0;i<RunTimePublicData.onlineFriend.size();++i){
                    if(userID==RunTimePublicData.onlineFriend.get(i)){
                        RunTimePublicData.onlineFriend.remove(i);
                        break;
                    }
                }
                for(int i=0;i<onlineFriendComboBox.getItemCount();++i)
                {
                    int id=(int)onlineFriendComboBox.getItemAt(i);
                    if(id==userID)
                        onlineFriendComboBox.removeItemAt(i);
                }
            }
        } else if (action.equals("Logout")) {
            JOptionPane.showMessageDialog(this, "你已被强制下线");
            System.exit(0);
        }

    }
}
