package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.intellij.execution.configurations.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * User: steve
 * Date: Jan 4, 2010
 * Time: 10:25:35 PM
 */
public class BuildrConfigurationType implements ConfigurationType {
    private final MyConfigurationFactory myConfigurationFactory = new MyConfigurationFactory();

    public String getDisplayName() {
        return "BuildR";
    }

    public String getConfigurationTypeDescription() {
        return "BuildR";
    }

    public Icon getIcon() {
        return Buildr.BUILDR_16;
    }

    @NotNull
    public String getId() {
        return getDisplayName();
    }

    public MyConfigurationFactory getMyConfigurationFactory() {
        return myConfigurationFactory;
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] {
                myConfigurationFactory
        };
    }

    class MyConfigurationFactory extends ConfigurationFactory {
        protected MyConfigurationFactory() {
            super(BuildrConfigurationType.this);
        }

        @Override
        public RunConfiguration createTemplateConfiguration(Project project) {
            return new BuildrRunConfiguration(this, project);
        }

    }

}
