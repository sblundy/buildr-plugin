package com.digitalsanctum.idea.plugins.buildr.beforerun;

import com.intellij.execution.BeforeRunTask;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * User: sblundy
 * Date: Jan 7, 2010
 * Time: 11:40:33 PM
 */
public class BuildrBeforeRunTask extends BeforeRunTask {
    @NotNull
    private List<String> tasks;

    public BuildrBeforeRunTask() {
        this.tasks = new java.util.ArrayList<String>();
    }

    public BuildrBeforeRunTask(@NotNull List<String> tasks) {
        this.tasks = tasks;
    }

    public void setTasks(@NotNull List<String> tasks) {
        this.tasks = tasks;
    }

    @NotNull
    public List<String> getTasks() {
        return tasks;
    }

    @Override
    public void writeExternal(Element element) {
        super.writeExternal(element);
        if (!tasks.isEmpty()) element.setAttribute("tasks", StringUtils.join(this.tasks, " "));
    }

    @Override
    public void readExternal(Element element) {
        super.readExternal(element);
        if(null == element.getAttributeValue("tasks")) {
            this.tasks = new java.util.ArrayList<String>();
        } else {
            this.tasks = new java.util.ArrayList<String>(
                    Arrays.asList(StringUtils.split(element.getAttributeValue("tasks"), " ")));
        }
    }
}
