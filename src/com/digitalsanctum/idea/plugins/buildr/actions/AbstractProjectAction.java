package com.digitalsanctum.idea.plugins.buildr.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 10:33:11 PM
 */
public abstract class AbstractProjectAction extends AnAction {
    @Nullable
    protected <T> T findProjectComponent(DataContext ctxt, Class<T> type) {
        final Project project = (Project) ctxt.getData(DataConstantsEx.PROJECT);
        if (null != project) {
            return project.getComponent(type);
        } else {
            return null;
        }
    }
}
