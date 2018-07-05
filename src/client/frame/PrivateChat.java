package client.frame;

import client.net.NetworkEvent;
import client.net.NetworkListener;
import client.util.GUIUtil;
import client.util.RunTimePublicData;
import com.alibaba.fastjson.JSON;
import publicDataObject.Information;
import publicDataObject.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @description: 私聊界面
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018/7/5
 **/
public class PrivateChat extends JFrame implements ActionListener,NetworkListener {
    private int chatUserID;
    private JTextArea messageArea = new JTextArea("以下是聊天记录\n");
    private JTextArea inputField = new JTextArea();
    private JButton sendButton = new JButton();

    public PrivateChat(int chatUserID){
        this.chatUserID=chatUserID;
        this.setTitle("和"+chatUserID+"的私聊界面");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(1000, 1000);
        this.add(messageArea);
        this.add(inputField);
        this.add(sendButton);
        messageArea.setBounds(0, 0, 600, 600);
        messageArea.setEditable(false);
        inputField.setBounds(0, 700, 400, 20);
        sendButton.setBounds(430, 700, 100, 40);
        sendButton.setText("发送");
        //添加监听器
        sendButton.addActionListener(this::actionPerformed);
        RunTimePublicData.network.addNetworkListener(this::networkEvent);
        this.setVisible(true);
        GUIUtil.toCenter(this);
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
        Information information = JSON.parseObject(event.getReceiveString(), Information.class);
        String action = information.getAction();
        String data = information.getData();
        if(action.equals("Message")){
            Message message=JSON.parseObject(data,Message.class);
            if(message.getFromID()==chatUserID&&message.getDestinationID()==RunTimePublicData.account){
                messageArea.append(message.getNickName() + " " + message.getFromID() + "说：\n");
                messageArea.append(message.getMessage()+"\n");
            }else {
                return;
            }
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Message message = new Message();
        message.setMessage(inputField.getText());
        inputField.setText("");
        message.setNickName(RunTimePublicData.nickName);
        message.setFromID(RunTimePublicData.account);
        message.setDestinationID(chatUserID);
        String data = JSON.toJSONString(message);
        Information information = new Information();
        information.setAction("Message");
        information.setData(data);
        String sendData = JSON.toJSONString(information);
        RunTimePublicData.network.sendMessage(sendData);

        messageArea.append("我说：\n"+message.getMessage()+"\n");
    }
}
