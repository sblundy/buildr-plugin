package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.ui.BuildrTasksPane;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import static com.digitalsanctum.idea.plugins.buildr.Buildr.*;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 9:13:34 PM
 */
public class BuildrTasksListComponent implements ProjectComponent {
    private Project project;
    private ToolWindow buildrToolWindow;
    private BuildrTasksPane buildrTasksPane;
    private ContentFactory contentFactory;

    public BuildrTasksListComponent(Project project) {
        this.project = project;
    }

    public void projectOpened() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(this.project);
        initBuildrToolWindow(toolWindowManager);
    }

    public void projectClosed() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        toolWindowManager.unregisterToolWindow(BUILDR_TOOL_WINDOW_ID);
    }

    @NotNull
    public String getComponentName() {
        return BuildrComponents.BUILDR_TASK_LIST_COMPONENT;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public void refreshTaskList() {
        this.buildrTasksPane.refreshTaskList();
    }

    public void runSelectedTask(DataContext context) {
        this.buildrTasksPane.runSelectedTask(context);
    }

    public boolean isTaskSelected() {
        return null != this.buildrTasksPane && this.buildrTasksPane.isTaskSelected();
    }

    private ContentFactory getContentFactory() {
        if (contentFactory == null) {
            contentFactory = ContentFactory.SERVICE.getInstance();
        }
        return contentFactory;
    }

    private void initBuildrToolWindow(ToolWindowManager toolWindowManager) {
        if (buildrToolWindow == null) {
            buildrToolWindow = toolWindowManager.registerToolWindow(BUILDR_TOOL_WINDOW_ID, false,
                    ToolWindowAnchor.RIGHT);
            buildrToolWindow.setIcon(BUILDR_16);

            if (buildrTasksPane == null) {
                buildrTasksPane = new BuildrTasksPane(this.project.getComponent(BuildrProjectComponent.class));
            }

            Content content = getContentFactory().createContent(buildrTasksPane.getTasksPanel(),
                    BUILDR_TASKS_DISPLAY_NAME, false);
            buildrToolWindow.getContentManager().addContent(content);
        }
    }
}
