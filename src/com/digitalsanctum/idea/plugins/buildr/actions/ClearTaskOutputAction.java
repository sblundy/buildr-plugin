package com.digitalsanctum.idea.plugins.buildr.actions;

import com.digitalsanctum.idea.plugins.buildr.BuildrTaskOutputComponent;
import com.digitalsanctum.idea.plugins.buildr.TaskOutputListener;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 9:08:10 PM
 */
public class ClearTaskOutputAction extends AbstractProjectAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        TaskOutputListener bpc = findProjectComponent(event.getDataContext(), BuildrTaskOutputComponent.class);
        if (null != bpc) {
            bpc.clearTaskOutput();
        }
    }
}
