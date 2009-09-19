package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.digitalsanctum.idea.plugins.buildr.run.BuildrRunner;
import com.digitalsanctum.idea.plugins.buildr.ui.BuildrTasksPane;
import com.digitalsanctum.idea.plugins.buildr.ui.TaskOutputPane;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 8:37:27 AM
 */
public class BuildrProjectComponent implements ProjectComponent, Buildr {

    private static final Logger LOG = Logger.getInstance(BuildrProjectComponent.class.getName());

    private ToolWindow buildrToolWindow;
    private ToolWindow buildrOutputWindow;

    private BuildrTasksPane buildrTasksPane;
    private TaskOutputPane taskOutputPane;

    private ContentFactory contentFactory;

    private BuildrProject buildrProject;

    public BuildrProjectComponent(Project project) {
        this.buildrProject = new BuildrProject(project);
    }

    public void projectOpened() {

        // verify environment
        BuildrRunner br = new BuildrRunner(this.buildrProject);

        if (br.isBuildrCommandAvailable()) {
            if (buildrProject.isValidBuildrProject()) {
                initBuildrWindows();
            } else {
                if (LOG.isDebugEnabled()) LOG.debug("No buildfile present...skipping Buildr setup");
            }
        } else {
            LOG.error("Buildr script not found!");
        }
    }

    private ContentFactory getContentFactory() {
        if (contentFactory == null) {
            contentFactory = ContentFactory.SERVICE.getInstance();
        }
        return contentFactory;
    }


    private void initBuildrWindows() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(buildrProject.getProject());
        initBuildrOutputWindow(toolWindowManager);
        initBuildrToolWindow(toolWindowManager);
    }

    private void initBuildrToolWindow(ToolWindowManager toolWindowManager) {
        if (buildrToolWindow == null) {
            buildrToolWindow = toolWindowManager.registerToolWindow(BUILDR_TOOL_WINDOW_ID, false, ToolWindowAnchor.RIGHT);
            buildrToolWindow.setIcon(BUILDR_16);

            if (buildrTasksPane == null) {
                buildrTasksPane = new BuildrTasksPane(buildrProject, taskOutputPane);
            }

            Content content = getContentFactory().createContent(buildrTasksPane.getTasksPanel(), "Tasks", false);
            buildrToolWindow.getContentManager().addContent(content);
        }
    }

    private void initBuildrOutputWindow(ToolWindowManager toolWindowManager) {
        if (buildrOutputWindow == null) {
            buildrOutputWindow = toolWindowManager.registerToolWindow(BUILDR_OUTPUT_WINDOW_ID, false, ToolWindowAnchor.BOTTOM);
            buildrOutputWindow.setIcon(BUILDR_16);
            if (taskOutputPane == null) {
                taskOutputPane = new TaskOutputPane();
            }
            Content outputContent = getContentFactory().createContent(taskOutputPane.getPanel(), "BuildrOutput", false);
            buildrOutputWindow.getContentManager().addContent(outputContent);
        }
    }


    private void unregisterBuildrToolWindows() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(buildrProject.getProject());
        toolWindowManager.unregisterToolWindow(BUILDR_TOOL_WINDOW_ID);
        toolWindowManager.unregisterToolWindow(BUILDR_OUTPUT_WINDOW_ID);
    }


    public void projectClosed() {
        unregisterBuildrToolWindows();
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return BuildrComponents.BUILDR_PROJECT_COMPONENT;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }
}
