package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.digitalsanctum.idea.plugins.buildr.run.Runner;
import com.digitalsanctum.idea.plugins.buildr.settings.BuildrApplicationSettings;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.util.List;

/**
 * User: steve
 * Date: Jan 5, 2010
 * Time: 9:15:45 PM
 */
public class BuildrCommandLineState extends CommandLineState {
    private static final Logger LOG = Logger.getInstance(BuildrCommandLineState.class.getName());
    private BuildrProject buildrProject;
    private List<String> tasks;

    public BuildrCommandLineState(ExecutionEnvironment environment, List<String> tasks) {
        super(environment);
        this.tasks = tasks;
        final Project project = (Project) environment.getDataContext().getData(DataConstantsEx.PROJECT);
        assert project != null;
        this.buildrProject = project.getComponent(BuildrProjectComponent.class).getBuildrProject();
    }

    @Override
    protected OSProcessHandler startProcess() throws ExecutionException {
        final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();

        final GeneralCommandLine cmdLine = Runner.createAndSetupCmdLine(null, buildrProject.getBaseDirPath(),
                settings.buildrPath, tasks.toArray(new String[tasks.size()]));
        LOG.debug("cmdLine=" + cmdLine.getCommandLineString());
        return new OSProcessHandler(cmdLine.createProcess(), cmdLine.getCommandLineString());
    }
}
