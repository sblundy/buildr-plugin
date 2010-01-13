package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
* User: steve
* Date: Jan 4, 2010
* Time: 11:35:29 PM
*/
public class BuildrRunConfiguration extends RunConfigurationBase implements ModuleRunConfiguration {
    public static final Logger LOG = Logger.getInstance(BuildrRunConfiguration.class.getName());
    private List<String> tasks = new java.util.ArrayList<String>();
    private static final String TASKS_PARENT_ELEMENT_NAME = "tasks";
    private static final String TASKS_TASK_ELEMENT_NAME = "value";

    public BuildrRunConfiguration(BuildrConfigurationType.MyConfigurationFactory myConfigurationFactory, Project project) {
        super(project, myConfigurationFactory, "");
    }

    public BuildrRunConfigSettingEditor getConfigurationEditor() {
        return new BuildrRunConfigSettingEditor(getProject().getComponent(BuildrProjectComponent.class));
    }

    public JDOMExternalizable createRunnerSettings(ConfigurationInfoProvider provider) {
        return null;
    }

    @SuppressWarnings("deprecated")
    public SettingsEditor<JDOMExternalizable> getRunnerSettingsEditor(ProgramRunner runner) {
        return null;
    }

    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment)
            throws ExecutionException {
        final BuildrCommandLineState state = new BuildrCommandLineState(environment, tasks);
        state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject()));
        return state;
    }

    public void checkConfiguration() throws RuntimeConfigurationException {
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    @NotNull
    public Module[] getModules() {
        return Module.EMPTY_ARRAY;
    }

    @Override
     public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);

        Element tasks = element.getChild(TASKS_PARENT_ELEMENT_NAME);
        if(null == tasks) {
            tasks = new Element(TASKS_PARENT_ELEMENT_NAME);
            element.addContent(tasks);
        } else {
            tasks.removeContent();
        }

        for (String task : this.tasks) {
            final Element value = new Element(TASKS_TASK_ELEMENT_NAME);
            value.setText(task);
            tasks.addContent(value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        this.tasks = new java.util.ArrayList<String>();

        final Element tasks = element.getChild(TASKS_PARENT_ELEMENT_NAME);
        if(null != tasks) {
            for (Element value : (List<Element>) tasks.getChildren(TASKS_TASK_ELEMENT_NAME)) {
                this.tasks.add(value.getText());
            }
        }
    }
}
