package com.aiops.cloudalert.module;

import com.aiops.cloudalert.settings.AppSettingsState;
import com.aiops.cloudalert.ui.CloseDialog;
import com.aiops.cloudalert.util.HttpClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.Forest;
import com.intellij.notification.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IDButtonEditor extends DefaultCellEditor {
    // 获取通知组管理器
    private NotificationGroupManager manager = NotificationGroupManager.getInstance();

    // 获取注册的通知组
    private NotificationGroup balloon = manager.getNotificationGroup("com.aiops.notification.balloon");

    private JPanel panel;
    private JLabel idButton;
    private JSONObject value;

    private JButton refresh;
    private JSONArray apps;

    public IDButtonEditor(JButton refresh,JSONArray apps) {
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
        super(new JTextField());
        this.refresh = refresh;
        this.apps = apps;
        // 设置点击几次激活编辑。
        this.setClickCountToStart(1);

        this.initButton();

        this.initPanel();

        // 添加按钮。
        this.panel.add(this.idButton);
    }

    private void initButton() {
        this.idButton = new JLabel();
        this.idButton.setBounds(0, 0, 100, 25);
        Color color = new Color(45, 131, 218);
        this.idButton.setForeground(color);

        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。
        this.idButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JSONObject v = JSONObject.parseObject(getCellEditorValue().toString());
                CloseDialog dialog = new CloseDialog(v,refresh,false,apps);
                dialog.setTitle(v.getString("id"));
                dialog.setValue(v);
                dialog.setLocationByPlatform(true);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

    }

    private void initPanel() {
        this.panel = new JPanel();

        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
    }


    /**
     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = JSONObject.parseObject(value.toString());
        this.idButton.setText(this.value.getString("id"));
        return this.panel;
    }

    /**
     * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
     */
    @Override
    public Object getCellEditorValue() {
        return this.value;
    }
}
