package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.digitalsanctum.idea.plugins.buildr.run.BuildrRunner;
import com.digitalsanctum.idea.plugins.buildr.run.Output;
import com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException;
import com.digitalsanctum.idea.plugins.buildr.lang.TextUtil;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * User: shane
 * Date: Feb 2, 2008
 * Time: 9:47:56 AM
 */
public class BuildrComponent implements ApplicationComponent, Buildr {

  private static final Logger LOG = Logger.getInstance(BuildrComponent.class.getName());

  public BuildrComponent() {
  }

  public void getBuildrVersion(@NotNull Project project) {

    BuildrProject bp = new BuildrProject(project);
    BuildrRunner buildrRunner = new BuildrRunner(bp, VERSION_ARG);
    Output output;
    try {
      output = buildrRunner.runBuildrCommand();
    } catch (BuildrPluginException e) {
      LOG.error(e);
      output = new Output(null, "Buildr not available!");
    }

    StringBuilder sb = new StringBuilder();
    if (!TextUtil.isEmpty(output.getStderr())) {
      sb.append(output.getStderr());
    } else {
      sb.append(output.getStdout());                                       
    }
    sb.append("\n'buildfile' present? ").append(bp.buildfilePresent());

    Messages.showMessageDialog(sb.toString(), "Buildr Version", BUILDR_32);
  }


  @NonNls
  @NotNull
  public String getComponentName() {
    return "BuildrComponent";
  }

  public void initComponent() {
    LOG.info("initComponent");
  }

  public void disposeComponent() {
    LOG.info("disposeComponent");
  }
}
