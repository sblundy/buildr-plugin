package com.digitalsanctum.idea.plugins.buildr.settings;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: shane
 * Date: Nov 22, 2008
 * Time: 10:55:31 AM
 */
public class BuildrSettingsPane {
  private JPanel generatedPanel;
  private JTabbedPane settingsTabbedPane;

  private List<UnnamedConfigurable> configurableList = new ArrayList<UnnamedConfigurable>();

  public JComponent getPanel() {
    settingsTabbedPane.setBackground(generatedPanel.getBackground());

    final GeneralSettingsTab generalTab = new GeneralSettingsTab();
    configurableList.add(generalTab);
    settingsTabbedPane.addTab(BuildrBundle.message("settings.plugin.general.tab.title"),
        generalTab.getContentPanel());

    /*settingsTabbedPane.addTab(RBundle.message("settings.register.shortcut.tab.title"),
   new RegisteredActionNamesPanel().getContentPanel());
settingsTabbedPane.addTab(RBundle.message("settings.plugin.about.tab.title"),
   new AboutPluginPane().getContentPanel());*/
    return generatedPanel;
  }

  public void apply() throws ConfigurationException {
    for (UnnamedConfigurable configurable : configurableList) {
      configurable.apply();
    }
  }

  public void reset() {
    for (UnnamedConfigurable configurable : configurableList) {
      configurable.reset();
    }
  }

  public boolean isModified() {
    for (UnnamedConfigurable configurable : configurableList) {
      if (configurable.isModified()) {
        return true;
      }
    }
    return false;
  }

}
