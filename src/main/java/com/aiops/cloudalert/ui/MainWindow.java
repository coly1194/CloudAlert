package com.aiops.cloudalert.ui;

import com.aiops.cloudalert.module.PanelButtonEditor;
import com.aiops.cloudalert.module.PanelButtonRenderer;
import com.aiops.cloudalert.settings.AppSettingsState;
import com.aiops.cloudalert.util.HttpClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.Forest;
import com.dtflys.forest.exceptions.ForestNetworkException;
import com.intellij.notification.*;
import com.intellij.openapi.wm.ToolWindow;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MainWindow {
    private static final Log log = LogFactory.getLog(MainWindow.class);
    // 获取通知组管理器
    private NotificationGroupManager manager = NotificationGroupManager.getInstance();

    // 获取注册的通知组
    private NotificationGroup balloon = manager.getNotificationGroup("com.aiops.notification.balloon");
    private JPanel mainPanel;
    private JTable table;
    private JButton refresh;
    private JComboBox statusButton;
    private JComboBox labelButton;
    private DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "标题", "内容", "触发时间", "级别", "状态", "操作"});
    private String[] statusLabel = {"ALL", "ACTIVE", "ACK", "CLOSED"};
    private String[] label = {"我的告警", "所有告警", "未分配告警"};

    private JLabel pageLabel;

    public MainWindow(ToolWindow toolWindow) {
        statusButton.setModel(new DefaultComboBoxModel(statusLabel));
        statusButton.setSelectedItem("ALL");
        labelButton.setModel(new DefaultComboBoxModel(label));
        labelButton.setSelectedItem("我的告警");
        addRows();
        statusButton.addActionListener(e -> addRows());
        labelButton.addActionListener(e -> addRows());
        refresh.addActionListener(e -> addRows());
    }

    public void addRows() {
        defaultTableModel.setRowCount(0);
        AppSettingsState settings = AppSettingsState.getInstance();
        String username = settings.username;
        String password = settings.password;
        HttpClient myClient = Forest.client(HttpClient.class);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("order", " create_time desc");
                String st = statusButton.getSelectedItem().toString();
                if (StringUtils.isNotBlank(st) && !"ALL".equals(st)) {
                    params.put("status", st);
                }
                String la = labelButton.getSelectedItem().toString();
                {
                    if ("我的告警".equals(la)) {
                        params.put("who", "mine");
                    } else if ("未分配告警".equals(la)) {
                        params.put("who", "un");
                    }
                }
                String result = myClient.getAlerts(username, password, 20, 1, params);
                JSONObject jsonObject = JSON.parseObject(result);
                if(jsonObject.getInteger("code")==200) {
                    String pageText = jsonObject.getString("totalCount") + "条告警";
                    pageLabel.setText(pageText);
                    JSONArray alerts = jsonObject.getJSONObject("data").getJSONArray("records");
                    // 添加
                    for (int i = 0; i < alerts.size(); i++) {
                        JSONObject a = alerts.getJSONObject(i);
                        defaultTableModel.addRow(new Object[]{a.getString("id"), a.getString("alarmName"), a.getString("alarmContent"), getDate(a.getLong("createTime")), getPriorityStr(a.getString("priority")), a.getString("status"), a});
                    }
                    table.setModel(defaultTableModel);
                    table.getColumnModel().getColumn(6).setCellEditor(new PanelButtonEditor(refresh));
                    table.getColumnModel().getColumn(6).setCellRenderer(new PanelButtonRenderer());
                }
            } catch (ForestNetworkException e1){
                Notification notification = balloon.createNotification("apiKey检查不通过,请前往Settins->Tools->Cloud Alert配置！", NotificationType.WARNING);
                Notifications.Bus.notify(notification);
            } catch (Exception e) {
                log.error("window error:", e);
            }
        }else {
            Notification notification = balloon.createNotification("未配置邮箱和apiKEY,请前往Settins->Tools->Cloud Alert配置！", NotificationType.WARNING);
            Notifications.Bus.notify(notification);
        }
    }

    public JPanel getContent() {
        return mainPanel;
    }

    private String getPriorityStr(String p) {
        switch (p) {
            case "1":
                return "提醒";
            case "2":
                return "警告";
            case "3":
                return "严重";
            case "4":
                return "通知";
            case "5":
                return "致命";
            default:
                return p;
        }
    }

    private String getDate(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(time);
    }
}
