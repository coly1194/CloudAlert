package com.aiops.cloudalert.ui;

import com.aiops.cloudalert.settings.AppSettingsState;
import com.aiops.cloudalert.util.HttpClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.Forest;
import com.intellij.notification.*;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.*;

public class CloseDialog extends JDialog {
    // 获取通知组管理器
    private NotificationGroupManager manager = NotificationGroupManager.getInstance();

    // 获取注册的通知组
    private NotificationGroup balloon = manager.getNotificationGroup("com.aiops.notification.balloon");
    private JSONObject value;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton youxiaoRadioButton;
    private JRadioButton wuxiaoRadioButton;
    private JRadioButton wubaoRadioButton;
    private JTextField solutionTextField;
    private JTextPane textPane;
    private JTextField biaoti;
    private JTextField duixiang;
    private JTextField app;
    private JTextField shijian;
    private JLabel solutionLabel;

    private int validStatus;

    ButtonGroup group=new ButtonGroup();


    public CloseDialog(JSONObject value, JButton refresh, boolean action, JSONArray apps) {
        this.value = value;
        setContentPane(contentPane);
        textPane.setText(value.getString("alarmContent"));
        biaoti.setText(value.getString("alarmName"));
        duixiang.setText(value.getString("entityName"));
        for(int i =0;i<apps.size();i++){
            if(apps.getJSONObject(i).getString("appId").equals(value.getString("app"))){
                app.setText(apps.getJSONObject(i).getString("description"));
                break;
            }
        }
        shijian.setText(value.getString("creationTime"));
        if(!action){
            wuxiaoRadioButton.setVisible(false);
            wubaoRadioButton.setVisible(false);
            youxiaoRadioButton.setVisible(false);
            buttonOK.setVisible(false);
            solutionTextField.setVisible(false);
            solutionLabel.setVisible(false);
        }
        group.add(wuxiaoRadioButton);
        group.add(wubaoRadioButton);
        group.add(youxiaoRadioButton);
        group.setSelected(youxiaoRadioButton.getModel(),true);

        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        wuxiaoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validStatus = 2;
            }
        });
        youxiaoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validStatus = 0;
            }
        });
        wubaoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validStatus = 1;
            }
        });
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK(refresh);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(JButton refresh) {
        AppSettingsState settings = AppSettingsState.getInstance();
        String username = settings.username;
        String password = settings.password;
        HttpClient myClient = Forest.client(HttpClient.class);
        // 在此处添加您的代码
        String comments = solutionTextField.getText();
        if(StringUtils.isBlank(comments)){
            comments="调用rest api关闭";
        }else {
            comments="调用rest api关闭:"+comments;
        }
        String res = myClient.close(username,password,value.getString("id"),comments,validStatus);
        JSONObject r = JSONObject.parseObject(res);
        if(r.getInteger("code")==200){
            Notification notification = balloon.createNotification("关闭告警"+value.getString("id")+"成功!", NotificationType.INFORMATION);
            Notifications.Bus.notify(notification);
            refresh.doClick();
        }else {
            Notification notification = balloon.createNotification("关闭"+value.getString("id")+"失败!", NotificationType.WARNING);
            Notifications.Bus.notify(notification);
            refresh.doClick();
        }
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public JSONObject getValue() {
        return value;
    }

    public void setValue(JSONObject value) {
        this.value = value;
    }
}
