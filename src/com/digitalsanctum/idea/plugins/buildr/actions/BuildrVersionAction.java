package com.digitalsanctum.idea.plugins.buildr.actions;

import com.digitalsanctum.idea.plugins.buildr.BuildrComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

/**
 * User: shane
 * Date: Feb 2, 2008
 * Time: 12:49:47 PM
 */
public class BuildrVersionAction extends AnAction {

  private static final Logger LOG = Logger.getInstance(BuildrVersionAction.class.getName());

  public void actionPerformed(AnActionEvent e) {
    if (LOG.isDebugEnabled()) LOG.debug("BuildrVersionAction::actionPerformed");

    Application application = ApplicationManager.getApplication();
    BuildrComponent bc = application.getComponent(BuildrComponent.class);

    Project p = DataKeys.PROJECT.getData(e.getDataContext());

    if (p != null) {      
      bc.getBuildrVersion(p);
    }

  }
}
