package client.frame;

import client.net.NetworkEvent;
import client.net.NetworkListener;
import client.util.GUIUtil;
import client.util.RunTimePublicData;
import com.alibaba.fastjson.JSON;
import publicDataObject.Information;
import publicDataObject.Registered;
import publicDataObject.RegisteredResponce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @program: Java-Course-Design-im
 * @description: 用户注册界面
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018-05-23 20:05
 **/
public class RegisterFrame extends JFrame implements ActionListener,NetworkListener {
    //定义控件
    private JLabel nickNameLable = new JLabel("请输入您的昵称");
    private JTextField nickNameTextField = new JTextField(10);
    private JLabel passwordLable = new JLabel("请输入您的密码");
    private JPasswordField passwordField = new JPasswordField(10);
    private JLabel passwordLableConfirm = new JLabel("请确认密码");
    private JPasswordField passwordFieldConfirm = new JPasswordField(10);
    private JButton loginButton = new JButton("登录");
    private JButton registerButton = new JButton("注册");
    private JButton exitButton = new JButton("退出");


    public RegisterFrame() {
        super("注册");
        this.setLayout(new FlowLayout());
        this.add(nickNameLable);
        this.add(nickNameTextField);
        this.add(passwordLable);
        this.add(passwordField);
        this.add(passwordLableConfirm);
        this.add(passwordFieldConfirm);
        this.add(loginButton);
        this.add(registerButton);
        this.add(exitButton);
        this.setSize(240, 220);
        GUIUtil.toCenter(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        //增加监听
        loginButton.addActionListener(this::actionPerformed);
        registerButton.addActionListener(this::actionPerformed);
        exitButton.addActionListener(this::actionPerformed);
        RunTimePublicData.network.addNetworkListener(this::networkEvent);

    }


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String password1 = new String(passwordField.getPassword());
            String password2 = new String(passwordFieldConfirm.getPassword());
            if (!password1.equals(password2)) {
                JOptionPane.showMessageDialog(this, "两次输入的密码不相符");
                return;
            }
            String nickName = nickNameTextField.getText();
            Information information=new Information();
            information.setAction("Registered");
            Registered registered=new Registered();
            registered.setNickName(nickName);
            registered.setPassword(password1);
            String data=JSON.toJSONString(registered);
            information.setData(data);
            String sendData=JSON.toJSONString(information);
            RunTimePublicData.network.sendMessage(sendData);
        } else if (e.getSource() == loginButton) {
            this.dispose();
            new LoginFrame();
        } else {
            JOptionPane.showMessageDialog(this, "谢谢光临");
            System.exit(0);
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
        Information information=JSON.parseObject(event.getReceiveString(),Information.class);
        String action=information.getAction();
        String data=information.getData();
        if(action.equals("RegisteredResponce")){
            RegisteredResponce registeredResponce=JSON.parseObject(data,RegisteredResponce.class);
            int userID=registeredResponce.getUserID();
            JOptionPane.showMessageDialog(this,"您的账号是:"+userID+"。请您牢记！");
        }
    }
}
