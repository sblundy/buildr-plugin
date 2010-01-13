package com.digitalsanctum.idea.plugins.buildr.beforerun;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.digitalsanctum.idea.plugins.buildr.run.Runner;
import com.digitalsanctum.idea.plugins.buildr.settings.BuildrApplicationSettings;
import com.digitalsanctum.idea.plugins.buildr.ui.BuildrTaskListPanel;
import com.intellij.execution.BeforeRunTaskProvider;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Key;
import com.intellij.util.concurrency.Semaphore;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: sblundy
 * Date: Jan 7, 2010
 * Time: 11:41:18 PM
 */
public class BuildrBeforeRunTaskProvider extends BeforeRunTaskProvider<BuildrBeforeRunTask> {
    private static final Key<BuildrBeforeRunTask> TASK_ID = Key.create("Buildr.BeforeRunTask");
    private Process process;

    @Override
    public Key<BuildrBeforeRunTask> getId() {
        return TASK_ID;
    }

    @Override
    public String getDescription(RunConfiguration runConfiguration, BuildrBeforeRunTask task) {
        return null == task || task.getTasks().isEmpty() ? BuildrBundle.message("beforerun.message.emtpy") :
                BuildrBundle.message("beforerun.message.tasks", StringUtils.join(task.getTasks(), ", "));
    }

    @Override
    public boolean hasConfigurationButton() {
        return true;
    }

    @Override
    public BuildrBeforeRunTask createTask(RunConfiguration runConfiguration) {
        return new BuildrBeforeRunTask();
    }

    @Override
    public boolean configureTask(RunConfiguration runConfiguration, BuildrBeforeRunTask task) {
        BuildrBeforeRunDialog dialog = new BuildrBeforeRunDialog(runConfiguration.getProject(), task.getTasks());
        dialog.setTitle(BuildrBundle.message("beforerun.title"));

        dialog.show();

        if(!dialog.isOK()) {
            return false;
        } else {
            task.setTasks(dialog.panel.getTasks());
            return true;
        }
    }

    @Override
    public boolean executeTask(final DataContext context, RunConfiguration configuration,
                               final BuildrBeforeRunTask task) {
        final Semaphore targetDone = new Semaphore();
        final AtomicBoolean result = new AtomicBoolean(false);
        try {
            ApplicationManager.getApplication().invokeAndWait(new Runnable() {
                public void run() {
                    final Project project = PlatformDataKeys.PROJECT.getData(context);
                    if (project == null || project.isDisposed()) return;

                    final BuildrProjectComponent buildrProjectComponent =
                            project.getComponent(BuildrProjectComponent.class);

                    if (buildrProjectComponent == null) return;

                    targetDone.down();
                    new MyBackgroundable(project, task, buildrProjectComponent, result, targetDone).queue();
                }
            }, ModalityState.NON_MODAL);
        } catch (Exception e) {
            return false;
        }
        targetDone.waitFor();
        return result.get();
    }

    private static class BuildrBeforeRunDialog extends DialogWrapper {
        BuildrTaskListPanel panel;

        protected BuildrBeforeRunDialog(Project project, List<String> tasks) {
            super(project, false);
            this.panel = new BuildrTaskListPanel(project.getComponent(BuildrProjectComponent.class));
            this.panel.setTasks(tasks);
            init();
        }

        @Override
        protected JComponent createCenterPanel() {
            return panel.getPanel();
        }
    }

    private class MyBackgroundable extends Task.Backgroundable {
        private final BuildrBeforeRunTask task;
        private final AtomicBoolean result;
        private final Semaphore targetDone;
        private final String baseDirPath;

        public MyBackgroundable(Project project, BuildrBeforeRunTask task,
                                BuildrProjectComponent buildrProjectComponent, AtomicBoolean result,
                                Semaphore targetDone) {
            super(project, BuildrBundle.message("buildr.tasks.executing",
                    StringUtils.join(task.getTasks(), ",")), true);
            this.task = task;
            this.result = result;
            this.targetDone = targetDone;
            this.baseDirPath = buildrProjectComponent.getBuildrProject().getBaseDirPath();
        }

        public void run(@NotNull ProgressIndicator indicator) {
            final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
            final List<String> commands = new java.util.ArrayList<String>();
            commands.add(settings.buildrPath);
            commands.addAll(task.getTasks());

            try {
                process = Runner.createProcess(baseDirPath, commands.toArray(new String[commands.size()]));
                result.set(0 == process.waitFor());
            } catch (Throwable e) {
                result.set(false);
            } finally {
                targetDone.up();
            }
        }

        @Override
        public boolean shouldStartInBackground() {
            return true;
        }

        @Override
        public void processSentToBackground() {
        }

        @Override
        public void onCancel() {
            if(null != process) {
                process.destroy();
            }
        }
    }
}
