package com.digitalsanctum.idea.plugins.buildr.actions;

import com.digitalsanctum.idea.plugins.buildr.BuildrTasksListComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 8:17:46 PM
 */
public class RunTaskAction extends AbstractProjectAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        BuildrTasksListComponent bpc = findProjectComponent(event.getDataContext(), BuildrTasksListComponent.class);
        if (null != bpc) {
            bpc.runSelectedTask();
        }
    }

    @Override
    public void update(AnActionEvent event) {
        final BuildrTasksListComponent bpc = findProjectComponent(event.getDataContext(), BuildrTasksListComponent.class);
        event.getPresentation().setEnabled(null != bpc && bpc.isTaskSelected());
    }

}
