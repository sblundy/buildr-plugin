package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.execution.BuildrCommandLineState;
import com.digitalsanctum.idea.plugins.buildr.execution.BuildrConfigurationType;
import com.digitalsanctum.idea.plugins.buildr.execution.BuildrRunConfiguration;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.intellij.execution.*;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
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

        final BuildrConfigurationType type = ConfigurationTypeUtil.findConfigurationType(BuildrConfigurationType.class);
        final RunnerAndConfigurationSettings configSettings = RunManager.getInstance(buildrProject.getProject())
                .createRunConfiguration(StringUtils.join(selectedTask, ','), type.getMyConfigurationFactory());

        final BuildrRunConfiguration runConfiguration = (BuildrRunConfiguration) configSettings.getConfiguration();
        runConfiguration.setTasks(selectedTask);
        final ProgramRunner runner = RunnerRegistry.getInstance().findRunnerById(DefaultRunExecutor.EXECUTOR_ID);

        assert runner != null;

        final ExecutionEnvironment env = new ExecutionEnvironment(
                new MyRunProfile(buildrProject.getProject(), selectedTask),
                configSettings.getRunnerSettings(runner),
                configSettings.getConfigurationSettings(runner), context);

        try {
            runner.execute(DefaultRunExecutor.getRunExecutorInstance(), env, null);
        } catch (ExecutionException e) {
            LOG.error(e);
        }

    }

    private static class MyRunProfile implements RunProfile {
        private final Project project;
        private final List<String> tasks;

        public MyRunProfile(Project project, List<String> tasks) {
            this.project = project;
            this.tasks = tasks;
        }

        public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
            final BuildrCommandLineState state = new BuildrCommandLineState(environment, tasks);
            state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(this.project));
            return state;
        }

        public String getName() {
            return StringUtils.join(tasks, ", ");
        }

        public Icon getIcon() {
            return Buildr.BUILDR_16;
        }

        public void checkConfiguration() throws RuntimeConfigurationException {
        }
    }
}
