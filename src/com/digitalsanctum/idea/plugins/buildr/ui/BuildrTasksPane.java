package com.digitalsanctum.idea.plugins.buildr.ui;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: shane
 * Date: Nov 26, 2008
 * Time: 7:09:45 AM
 */
public class BuildrTasksPane implements Buildr {
    private JPanel tasksPanel;
    @SuppressWarnings("unused")
    private JComponent toolbar;
    private JList taskList;

    private BuildrProjectComponent buildrProject;


    public BuildrTasksPane(BuildrProjectComponent buildrProject) {
        this.buildrProject = buildrProject;
    }

    public JPanel getTasksPanel() {
        return tasksPanel;
    }

    private JList getTaskList() {
        final List<BuildrTask> bTasks = buildrProject.getBuildrProject().getAvailableTasks();

        final JList taskList = new JList(new TaskListModel(bTasks));

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = taskList.locationToIndex(e.getPoint());
                    final String selectedTask = (String) taskList.getModel().getElementAt(index);
                    buildrProject.runTask(selectedTask);
                }
            }
        };
        taskList.addMouseListener(mouseListener);
        return taskList;
    }

    private void createUIComponents() {
        this.taskList = getTaskList();

        final DefaultActionGroup taskPaneToolbar =
                ((DefaultActionGroup) ActionManager.getInstance().getAction("taskPaneToolbar"));
        this.toolbar = ActionManager.getInstance().createActionToolbar("Buildr", taskPaneToolbar, true).getComponent();
    }

    public void refreshTaskList() {
        if (null != this.taskList) {
            final List<BuildrTask> bTasks = buildrProject.getBuildrProject().getAvailableTasks();
            if (null == bTasks) {
                this.taskList.setModel(new TaskListModel(Collections.<BuildrTask>emptyList()));
            } else {
                this.taskList.setModel(new TaskListModel(bTasks));
            }
        }
    }

    public void runSelectedTask() {
        if (this.taskList.getSelectedIndex() >= 0) {
            buildrProject.runTask((String) this.taskList.getSelectedValue());
        }
    }

    public boolean isTaskSelected() {
        return this.taskList.getSelectedIndex() >= 0;
    }

    private class TaskListModel extends AbstractListModel {
        final List<String> tasks = new java.util.ArrayList<String>();

        public TaskListModel(List<BuildrTask> bTasks) {
            for (BuildrTask buildrTask : bTasks) {
                tasks.add(buildrTask.getName());
            }
        }

        public int getSize() {
            return tasks.size();
        }

        public Object getElementAt(int index) {
            return tasks.get(index);
        }
    }
}
