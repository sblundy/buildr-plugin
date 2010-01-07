package com.digitalsanctum.idea.plugins.buildr.execution;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * User: steve
 * Date: Jan 4, 2010
 * Time: 11:36:31 PM
 */
public class BuildrRunConfigSettingEditor extends SettingsEditor<BuildrRunConfiguration> {
    private static final Logger LOG = Logger.getInstance(BuildrRunConfigSettingEditor.class.getName());
    private final BuildrRunConfigurationForm form = new BuildrRunConfigurationForm();
    @Override
    protected void resetEditorFrom(BuildrRunConfiguration config) {
        LOG.debug("in resetEditorForm:tasks=" + config.getTasks());
        form.setTasks(config.getTasks());
    }

    @Override
    protected void applyEditorTo(BuildrRunConfiguration config) throws ConfigurationException {
        LOG.debug("in applyEditorTo:tasks=" + form.getTasks());
        config.setTasks(form.getTasks());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return form.getPanel();
    }

    @Override
    protected void disposeEditor() {
    }
}
