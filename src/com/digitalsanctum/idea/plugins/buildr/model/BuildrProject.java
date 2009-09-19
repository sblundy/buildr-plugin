package com.digitalsanctum.idea.plugins.buildr.model;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException;
import com.digitalsanctum.idea.plugins.buildr.parser.AvailableTasksParser;
import com.digitalsanctum.idea.plugins.buildr.run.BuildrRunner;
import com.digitalsanctum.idea.plugins.buildr.run.Output;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;
import java.util.ArrayList;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 9:26:52 AM
 */
public class BuildrProject implements Buildr {

  private static final Logger LOG = Logger.getInstance(BuildrProject.class.getName());

  private Project project;
  private ProjectRootManager myProjectRootManager;
  private List<BuildrError> errors = new ArrayList<BuildrError>();

  public BuildrProject(Project project) {
    this.project = project;
  }

  public Project getProject() {
    return project;
  }

  public boolean hasErrors() {
      return !errors.isEmpty();
  }

  public void addError(BuildrError buildrError) {
      errors.add(buildrError);
  }

  public String getProjectJdkName() {
    if (myProjectRootManager == null) {
      myProjectRootManager = ProjectRootManager.getInstance(project);
    }
    return myProjectRootManager.getProjectJdkName();
  }

  public boolean isRubyProjectJdk() {
    return getProjectJdkName().indexOf("Ruby") != -1;
  }

  public String getBaseDirPath() {
    VirtualFile baseDir = project.getBaseDir();
    if (baseDir != null) {
      return baseDir.getPath();
    }
    throw new RuntimeException("Project baseDir was null!");
  }

  public boolean isValidBuildrProject() {
    return !isRubyProjectJdk() && buildfilePresent();
  }

  public boolean availableTasksPresent() {
    List availTasks = getAvailableTasks();
    return availTasks != null && !availTasks.isEmpty();
  }

  public boolean buildfilePresent() {
    VirtualFile vf = project.getBaseDir();
    boolean present = false;
    if (vf != null) {
      for (String buildfile : BUILDFILES) {
        if (vf.findChild(buildfile) != null) {
          present = true;
          break;
        }
      }
    }
    return present;
  }

  public List<BuildrTask> getAvailableTasks() {

    BuildrRunner buildrRunner = new BuildrRunner(this, HELP_TASKS_ARG);
    Output output;
    try {
      output = buildrRunner.runBuildrCommand();
    } catch (BuildrPluginException e) {
      e.printStackTrace();      
      return null;
    }



    List<BuildrTask> tasks = new ArrayList<BuildrTask>();
    if (buildrRunner.hasErrors()) {
      System.err.println(output.getStderr());
      addError(new BuildrError(output.getStderr()));
    } else {      
      tasks = AvailableTasksParser.parseTasks(output.getStdout());
    }

    return tasks;
  }
}
