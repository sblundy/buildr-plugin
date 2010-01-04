package com.digitalsanctum.idea.plugins.buildr.actions;

import com.digitalsanctum.idea.plugins.buildr.BuildrTasksListComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 6:43:18 PM
 */
public class RefreshTaskListAction extends AbstractProjectAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        BuildrTasksListComponent bpc = findProjectComponent(event.getDataContext(), BuildrTasksListComponent.class);
        if (null != bpc) {
            bpc.refreshTaskList();
        }
    }
}
