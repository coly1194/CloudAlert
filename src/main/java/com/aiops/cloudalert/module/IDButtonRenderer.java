package com.aiops.cloudalert.module;

import com.alibaba.fastjson.JSONObject;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


public class IDButtonRenderer implements TableCellRenderer {
    private JPanel panel;
    private JLabel idButton;

    public IDButtonRenderer() {
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
    }

    private void initPanel() {
        this.panel = new JPanel();
        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean selected, boolean focus, int row, int column) {
        JSONObject rowData = JSONObject.parseObject(value.toString());
        this.idButton.setText(rowData.getString("id"));
        return this.panel;
    }
}
