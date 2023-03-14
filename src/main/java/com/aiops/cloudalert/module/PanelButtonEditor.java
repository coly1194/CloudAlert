package com.aiops.cloudalert.module;

import com.aiops.cloudalert.settings.AppSettingsState;
import com.aiops.cloudalert.util.HttpClient;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.Forest;
import com.intellij.notification.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelButtonEditor extends DefaultCellEditor {
    private static final Log log = LogFactory.getLog(PanelButtonEditor.class);
    private JPanel panel;
    private JButton ackButton;
    private JButton closeButton;
    private JSONObject value;

    private JButton refresh;

    public PanelButtonEditor(JButton refresh)
    {
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
        super(new JTextField());
        this.refresh = refresh;
        // 设置点击几次激活编辑。
        this.setClickCountToStart(1);

        this.initButton();

        this.initPanel();

        // 添加按钮。
        this.panel.add(this.ackButton);
        this.panel.add(this.closeButton);
    }

    private void initButton()
    {
        this.ackButton = new JButton();

        // 设置按钮的大小及位置。
        this.ackButton.setBounds(0, 0, 50, 25);

        this.closeButton = new JButton();

        // 设置按钮的大小及位置。
        this.closeButton.setBounds(51, 0, 50, 25);

        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。
        this.ackButton.addActionListener(new ActionListener()
        {
            AppSettingsState settings = AppSettingsState.getInstance();
            String username = settings.username;
            String password = settings.password;
            HttpClient myClient = Forest.client(HttpClient.class);
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // 触发取消编辑的事件，不会调用tableModel的setValue方法。
                PanelButtonEditor.this.fireEditingCanceled();
                // 这里可以做其它操作。
                JSONObject v = JSONObject.parseObject(getCellEditorValue().toString());
                String res = myClient.ack(username,password,v.getString("id"));
                JSONObject r = JSONObject.parseObject(res);
                if(r.getInteger("code")==200){
                    NotificationGroup notificationGroup = new NotificationGroup("notificationGroup", NotificationDisplayType.BALLOON, true);
                    Notification notification = notificationGroup.createNotification("认领成功!", NotificationType.INFORMATION);
                    Notifications.Bus.notify(notification);
                    refresh.doClick();
                }
            }
        });

        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。
        this.closeButton.addActionListener(new ActionListener()
        {
            AppSettingsState settings = AppSettingsState.getInstance();
            String username = settings.username;
            String password = settings.password;
            HttpClient myClient = Forest.client(HttpClient.class);

            @Override
            public void actionPerformed(ActionEvent e)
            {
                // 触发取消编辑的事件，不会调用tableModel的setValue方法。
                PanelButtonEditor.this.fireEditingCanceled();

                // 这里可以做其它操作。
                JSONObject v = JSONObject.parseObject(getCellEditorValue().toString());
                String res = myClient.close(username,password,v.getString("id"));
                JSONObject r = JSONObject.parseObject(res);
                if(r.getInteger("code")==200){
                    NotificationGroup notificationGroup = new NotificationGroup("notificationGroup", NotificationDisplayType.BALLOON, true);
                    Notification notification = notificationGroup.createNotification("关闭成功!", NotificationType.INFORMATION);
                    Notifications.Bus.notify(notification);
                    refresh.doClick();
                }
            }
        });

    }

    private void initPanel()
    {
        this.panel = new JPanel();

        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
    }


    /**
     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        this.value = JSONObject.parseObject(value.toString());
        String status = this.value.getString("status");
        this.ackButton.setText("认领");
        this.closeButton.setText("关闭");
        if("CLOSED".equals(status)){
            this.closeButton.setEnabled(false);
            this.ackButton.setEnabled(false);
        }else if("ACK".equals(status)){
            this.closeButton.setEnabled(true);
            this.ackButton.setEnabled(false);
        }else {
            this.closeButton.setEnabled(true);
            this.ackButton.setEnabled(true);
        }
        return this.panel;
    }

    /**
     * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
     */
    @Override
    public Object getCellEditorValue()
    {
        return this.value;
    }
}
