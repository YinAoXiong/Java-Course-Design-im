package client.frame;

import client.net.*;
import client.util.GUIUtil;
import client.util.RunTimePublicData;
import publicDataObject.Information;
import publicDataObject.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.alibaba.fastjson.JSON;
import publicDataObject.LoginResponce;

/**
 * @program: Java-Course-Design-im
 * @description: 用户登录界面
 * @author: 尹傲雄 yinaoxiong@gmail.com
 * @create: 2018-05-23 19:41
 **/
public class LoginFrame extends JFrame implements ActionListener,NetworkListener{
    //定义控件
    private Icon welcomeIcon=new ImageIcon("welcome.jpg");
    private JLabel welcomeLable=new JLabel(welcomeIcon);
    private JLabel accountLable=new JLabel("请输入您的账号");
    private JTextField accountTextField=new JTextField(10);
    private JLabel passwordLable=new JLabel("请输入您的密码");
    private JPasswordField passwordField=new JPasswordField(10);
    private JButton loginButton=new JButton("登录");
    private JButton registerButton=new JButton("注册");
    private JButton exitButton=new JButton("退出");

    public LoginFrame(){
        //界面初始化
        this.setLayout(new FlowLayout());
        this.add(welcomeLable);
        this.add(accountLable);
        this.add(accountTextField);
        this.add(passwordLable);
        this.add(passwordField);
        this.add(registerButton);
        this.add(loginButton);
        this.add(exitButton);
        this.setSize(240,360);
        GUIUtil.toCenter(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        //增加监听
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        exitButton.addActionListener(this);
        RunTimePublicData.network.addNetworkListener(this);
    }
    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==loginButton){
            String account=accountTextField.getText();
            String password=new String(passwordField.getPassword());
            Information information=new Information();
            //设置操作为登录
            information.setAction("Login");
            Login login=new Login();
            login.setPassword(password);
            login.setUserID(Integer.parseInt(account));
            RunTimePublicData.account=login.getUserID();
            String data=JSON.toJSONString(login);
            information.setData(data);
            String sentData=JSON.toJSONString(information);
            RunTimePublicData.network.sendMessage(sentData);
        }else if(e.getSource()==registerButton){
            this.dispose();
            new RegisterFrame();
        }else {
            JOptionPane.showMessageDialog(this,"谢谢光临");
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
       // System.out.println(event.getReceiveString());
        Information information=JSON.parseObject(event.getReceiveString(),Information.class);
        String action=information.getAction();
        String data=information.getData();
        if(action.equals("LoginResponce")){
            LoginResponce loginResponce=JSON.parseObject(data,LoginResponce.class);
            if(loginResponce.isLoginSuccess()){
                RunTimePublicData.nickName=loginResponce.getNickName();
                RunTimePublicData.onlineFriend=loginResponce.getOnlineFriendID();
                JOptionPane.showMessageDialog(this,"登录成功");
                this.dispose();
                new ChatFrame(RunTimePublicData.onlineFriend,RunTimePublicData.nickName+"的客户端");
            }else {
                JOptionPane.showMessageDialog(this,"您输入的密码或者账号有误请重新输入");
            }
        }
    }
}
