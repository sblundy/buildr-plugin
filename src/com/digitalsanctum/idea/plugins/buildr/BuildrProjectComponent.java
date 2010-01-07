package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.execution.BuildrConfigurationType;
import com.digitalsanctum.idea.plugins.buildr.execution.BuildrRunConfiguration;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.intellij.execution.*;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 8:37:27 AM
 */
public class BuildrProjectComponent implements ProjectComponent, Buildr {
    private static final Logger LOG = Logger.getInstance(BuildrProjectComponent.class.getName());

    private BuildrProject buildrProject;

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

    public void runTask(DataContext context, List<String> selectedTask) {
        LOG.debug("in runTask:" + selectedTask);

        BuildrConfigurationType type = ConfigurationTypeUtil.findConfigurationType(BuildrConfigurationType.class);

        final RunnerAndConfigurationSettings configSettings = RunManager.getInstance(buildrProject.getProject())
          .createRunConfiguration(StringUtils.join(selectedTask, ','), type.getMyConfigurationFactory());
        BuildrRunConfiguration runConfiguration = (BuildrRunConfiguration)configSettings.getConfiguration();
        runConfiguration.setTasks(selectedTask);
        ProgramRunner runner = RunnerRegistry.getInstance().findRunnerById(DefaultRunExecutor.EXECUTOR_ID);
        assert runner != null;
        ExecutionEnvironment env = new ExecutionEnvironment(runner, configSettings, context);
        Executor executor = DefaultRunExecutor.getRunExecutorInstance();

        try {
          runner.execute(executor, env, null);
        }
        catch (ExecutionException e) {
                LOG.error(e);
        }

    }
}
