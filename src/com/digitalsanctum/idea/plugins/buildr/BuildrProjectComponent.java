package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.digitalsanctum.idea.plugins.buildr.run.BuildrRunner;
import com.digitalsanctum.idea.plugins.buildr.run.Output;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 8:37:27 AM
 */
public class BuildrProjectComponent implements ProjectComponent, Buildr {
    private BuildrProject buildrProject;
    private final List<TaskOutputListener> taskOutputListeners =
            new java.util.ArrayList<TaskOutputListener>();

    public BuildrProjectComponent(Project project) {
        this.buildrProject = new BuildrProject(project);
    }

    public void projectOpened() {
    }


    public void projectClosed() {
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

    public BuildrProject getBuildrProject() {
        return this.buildrProject;
    }

    public void addExecutionListener(TaskOutputListener l) {
        this.taskOutputListeners.add(l);
    }

    public void removeExecutionListener(TaskOutputListener listener) {
        this.taskOutputListeners.remove(listener);
    }

    public void runTask(String selectedTask) {
        final BuildrRunner br = new BuildrRunner(buildrProject, selectedTask);
        try {
            final Output output = br.runBuildrCommand();
            if (br.hasErrors()) {
                logAndShow(output.getStderr());
            } else {
                logAndShow(output.getStdout());
            }
        } catch (BuildrPluginException e1) {
            e1.printStackTrace();
        }
    }

    private void logAndShow(String message) {
        for (TaskOutputListener listener : taskOutputListeners) {
            listener.log(message);
        }
    }
}
