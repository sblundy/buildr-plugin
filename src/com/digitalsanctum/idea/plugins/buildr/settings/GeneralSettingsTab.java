package com.digitalsanctum.idea.plugins.buildr.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.util.Comparing;

import javax.swing.*;
import java.awt.*;

/**
 * User: shane
 * Date: Nov 22, 2008
 * Time: 11:03:42 AM
 */
public class GeneralSettingsTab implements UnnamedConfigurable {
  private JPanel myContentPane;
  private JTextPane buildrPath;

  public Component getContentPanel() {
    return myContentPane;
  }

  public JComponent createComponent() {
    //N/A
    return null;
  }

  /**
   * Checks if the settings in the user interface component were modified by the user and
   * need to be saved.
   *
   * @return true if the settings were modified, false otherwise.
   */
  public boolean isModified() {
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    return !Comparing.equal(buildrPath.getText().trim(), settings.buildrPath);
  }

  /**
   * Store the settings from configurable to other components.
   */
  public void apply() throws ConfigurationException {
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    settings.buildrPath = buildrPath.getText().trim();
  }

  //
  /**
   * Load settings from other components to configurable.
   */
  public void reset() {
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    buildrPath.setText(settings.buildrPath);
  }

  /**
   * Disposes the Swing components used for displaying the configuration.
   */
  public void disposeUIResources() {
    // do nothing
  }
}
