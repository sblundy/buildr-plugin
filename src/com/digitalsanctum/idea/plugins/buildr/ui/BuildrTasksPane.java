package com.digitalsanctum.idea.plugins.buildr.ui;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.digitalsanctum.idea.plugins.buildr.run.BuildrRunner;
import com.digitalsanctum.idea.plugins.buildr.run.Output;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: shane
 * Date: Nov 26, 2008
 * Time: 7:09:45 AM
 */
public class BuildrTasksPane implements Buildr {
    private JPanel tasksPanel;
    private JScrollPane taskScrollPane;
    private JTextField commandTextField;
    private JLabel commandLabel;
    private JList taskList;

    private BuildrProject buildrProject;
    private TaskOutputPane taskOutputPane;


    public BuildrTasksPane(BuildrProject buildrProject, TaskOutputPane taskOutputPane) {
        this.buildrProject = buildrProject;
        this.taskOutputPane = taskOutputPane;
        this.taskList = getTaskList();
    }

    public JPanel getTasksPanel() {
        return tasksPanel;
    }

    private JList getTaskList() {
        List<BuildrTask> bTasks = buildrProject.getAvailableTasks();
        if (bTasks.isEmpty()) return null;

        final String[] tasks = new String[bTasks.size()];
        int i = 0;
        for (BuildrTask buildrTask : bTasks) {
            tasks[i++] = buildrTask.getName();
        }

        final JList taskList = new JList(tasks);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = taskList.locationToIndex(e.getPoint());
                    BuildrRunner br = new BuildrRunner(buildrProject, tasks[index]);
                    Output output = null;
                    try {
                        output = br.runBuildrCommand();
                    } catch (BuildrPluginException e1) {
                        e1.printStackTrace();
                    }
                    if (br.hasErrors()) {
                        logAndShow(output.getStderr());
                    } else {
                        logAndShow(output.getStdout());
                    }
                }
            }
        };
        taskList.addMouseListener(mouseListener);
        return taskList;
    }

    private void logAndShow(String message) {
        taskOutputPane.log(message);
        ToolWindowManager twm = ToolWindowManager.getInstance(buildrProject.getProject());
        ToolWindow buildrOutputWindow = twm.getToolWindow(BUILDR_OUTPUT_WINDOW_ID);
        buildrOutputWindow.show(new Runnable() {
            public void run() {
            }
        });
    }

    private void createUIComponents() {
        tasksPanel = new JPanel(new BorderLayout());
        if (taskList != null) {
            taskScrollPane = new JScrollPane(taskList);
        } else {
            taskScrollPane = new JScrollPane();
        }
        tasksPanel.setBackground(UIUtil.getTreeTextBackground());
        tasksPanel.add(taskScrollPane);
    }
}
