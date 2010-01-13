package com.digitalsanctum.idea.plugins.buildr.ui;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: shane
 * Date: Nov 26, 2008
 * Time: 7:09:45 AM
 */
public class BuildrTasksPane implements Buildr {
    private static final Logger LOG = Logger.getInstance(BuildrTasksPane.class.getName());
    private JPanel tasksPanel;
    private JTextField commandTextField;
    private JComponent toolbar;
    private JList taskList;

    private BuildrProjectComponent buildrProject;


    public BuildrTasksPane(BuildrProjectComponent buildrProject) {
        this.buildrProject = buildrProject;
        this.commandTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(KeyEvent.VK_ENTER == e.getKeyChar()) {
                    ActionManager.getInstance().tryToExecute(ActionManager.getInstance().getAction("runTask"), e,
                            commandTextField, null, true);
                }
            }
        });
    }

    public JPanel getTasksPanel() {
        return tasksPanel;
    }

    private JList getTaskList() {
        final JList taskList = new JList(new TaskListModel(buildrProject.getBuildrProject().getAvailableTasks()));

        final MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                LOG.debug("in mouseClicked:" + e.getClickCount());
                if (e.getClickCount() == 2) {
                    buildrProject.runTask(DataManager.getInstance().getDataContext(),
                            Arrays.asList((String) taskList.getSelectedValue()));
                }
                LOG.debug("end mouseClicked:" + e.getClickCount());
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

    public boolean isTaskSelected() {
        return StringUtils.isNotBlank(this.commandTextField.getText());
    }

    public List<String> getCommand() {
        return Arrays.asList(StringUtils.split(this.commandTextField.getText(), " "));
    }
}
