package com.aiops.cloudalert.module;

import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class PanelButtonRenderer implements TableCellRenderer {
    private JPanel panel;
    private JButton ackButton;
    private JButton closeButton;

    public PanelButtonRenderer() {
        this.initButton();

        this.initPanel();

        // 添加按钮。
        this.panel.add(this.ackButton);
        this.panel.add(this.closeButton);
    }

    private void initButton() {
        this.ackButton = new JButton();

        // 设置按钮的大小及位置。
        this.ackButton.setBounds(0, 0, 50, 25);

        this.closeButton = new JButton();

        // 设置按钮的大小及位置。
        this.closeButton.setBounds(51, 0, 50, 25);
    }

    private void initPanel() {
        this.panel = new JPanel();

        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean selected, boolean focus, int row, int column) {
        JSONObject rowData = JSONObject.parseObject(value.toString());
        String status = rowData.getString("status");
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
}
