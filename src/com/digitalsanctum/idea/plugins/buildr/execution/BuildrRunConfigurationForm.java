package com.digitalsanctum.idea.plugins.buildr.execution;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * User: steve
 * Date: Jan 4, 2010
 * Time: 11:38:24 PM
 */
public class BuildrRunConfigurationForm {
    private JTextField tasks;
    private JPanel panel;

    public JPanel getPanel() {
        return panel;
    }

    public List<String> getTasks() {
        return Arrays.asList(StringUtils.split(tasks.getText(), " "));
    }

    public void setTasks(List<String> tasks) {
        this.tasks.setText(StringUtils.join(tasks, " "));
    }
}
