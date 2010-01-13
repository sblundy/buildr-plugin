package com.digitalsanctum.idea.plugins.buildr.ui;

import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;

import javax.swing.*;
import java.util.List;

/**
* User: sblundy
* Date: Jan 8, 2010
* Time: 9:42:08 PM
*/
public class TaskListModel extends AbstractListModel {
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
