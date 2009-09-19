package com.digitalsanctum.idea.plugins.buildr.settings;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.digitalsanctum.idea.plugins.buildr.BuildrComponents;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * User: shane
 * Date: Nov 22, 2008
 * Time: 10:49:49 AM
 */
public class BuildrSettings implements ApplicationComponent, Configurable {

  private BuildrSettingsPane form;

  /**
   * Unique name of this component. If there is another component with the same name or
   * name is null internal assertion will occur.
   *
   * @return the name of this component
   */
  @NotNull
  public String getComponentName() {
    return BuildrComponents.BUILDR_SETTINGS;
  }

  /**
   * Component should do initialization and communication with another components in this method.
   */
  public void initComponent() {
    loadRegisteredData();
  }

  /**
   * Component should dispose system resources or perform another cleanup in this method.
   */
  public void disposeComponent() {
  }

  /**
   * Returns the user-visible name of the settings component.
   *
   * @return the visible name of the component.
   */
  @Nls
  public String getDisplayName() {
    return BuildrBundle.message("settings.title");
  }

  /**
   * Returns the icon representing the settings component. Components
   * shown in the IDEA settings dialog have 32x32 icons.
   *
   * @return the icon for the component.
   */
  public Icon getIcon() {
    return Buildr.BUILDR_32;
  }

  /**
   * Returns the topic in the help file which is shown when help for the configurable
   * is requested.
   *
   * @return the help topic, or null if no help is available.
   */
  public String getHelpTopic() {
    return null;
  }

  /**
   * Returns the user interface component for editing the configuration.
   *
   * @return the component instance.
   */
  public JComponent createComponent() {
    if (form == null) {
      form = new BuildrSettingsPane();
    }
    return form.getPanel();
  }

  /**
   * Checks if the settings in the user interface component were modified by the user and
   * need to be saved.
   *
   * @return true if the settings were modified, false otherwise.
   */
  public boolean isModified() {
    return form == null || form.isModified();
  }

  /**
   * Store the settings from configurable to other components.
   */
  public void apply() throws ConfigurationException {
    if (form != null) {
      // Get data from form to component
      form.apply();
    }
  }

  /**
   * Load settings from other components to configurable.
   */
  public void reset() {
    if (form != null) {
      // Reset form data from component
      form.reset();
    }
  }

  /**
   * Disposes the Swing components used for displaying the configuration.
   */
  public void disposeUIResources() {
    form = null;
  }

  private void loadRegisteredData() {
// todo   
  }
}
