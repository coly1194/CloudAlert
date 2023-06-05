package com.aiops.cloudalert.ui;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class AiopsToolWindowFactory implements ToolWindowFactory {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 获取 Forest 全局配置对象
        ForestConfiguration configuration = Forest.config();
        // 连接超时时间，单位为毫秒
        configuration.setConnectTimeout(20000);
        // 数据读取超时时间，单位为毫秒
        configuration.setReadTimeout(20000);
        MainWindow mainWindow = new MainWindow(toolWindow);
        ContentFactory contentFactory = toolWindow.getContentManager().getFactory();
        Content content = contentFactory.createContent(mainWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
