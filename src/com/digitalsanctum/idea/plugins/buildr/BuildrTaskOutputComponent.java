package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.ui.TaskOutputPane;
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
 * Time: 9:18:36 PM
 */
public class BuildrTaskOutputComponent implements ProjectComponent, TaskOutputListener {
    private Project project;
    private ToolWindow buildrOutputWindow;

    private TaskOutputPane taskOutputPane;
    private ContentFactory contentFactory;

    public BuildrTaskOutputComponent(Project project) {
        this.project = project;
    }

    public void projectOpened() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        initBuildrOutputWindow(toolWindowManager);
    }

    public void projectClosed() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        toolWindowManager.unregisterToolWindow(BUILDR_OUTPUT_WINDOW_ID);
    }

    @NotNull
    public String getComponentName() {
        return BuildrComponents.BUILDR_OUTPUT_COMPONENT;
    }

    public void initComponent() {
        BuildrProjectComponent buildrProjectComponent = project.getComponent(BuildrProjectComponent.class);
        buildrProjectComponent.addExecutionListener(this);
    }

    public void disposeComponent() {
        BuildrProjectComponent buildrProjectComponent = project.getComponent(BuildrProjectComponent.class);
        buildrProjectComponent.removeExecutionListener(this);
    }

    public void clearTaskOutput() {
        this.taskOutputPane.clear();
    }

    private ContentFactory getContentFactory() {
        if (contentFactory == null) {
            contentFactory = ContentFactory.SERVICE.getInstance();
        }
        return contentFactory;
    }

    public void log(String msg) {
        this.taskOutputPane.log(msg);
        this.buildrOutputWindow.show(new Runnable() {
            public void run() {
            }
        });
    }

    public void error(String msg) {
        this.taskOutputPane.error(msg);
    }

    private void initBuildrOutputWindow(ToolWindowManager toolWindowManager) {
        if (buildrOutputWindow == null) {
            buildrOutputWindow = toolWindowManager.registerToolWindow(BUILDR_OUTPUT_WINDOW_ID, false,
                    ToolWindowAnchor.BOTTOM);
            buildrOutputWindow.setIcon(BUILDR_16);
            if (taskOutputPane == null) {
                taskOutputPane = new TaskOutputPane();
            }
            Content outputContent = getContentFactory().createContent(taskOutputPane.getPanel(),
                    BUILDR_OUTPUT_DISPLAY_NAME, false);
            buildrOutputWindow.getContentManager().addContent(outputContent);
        }
    }
}
