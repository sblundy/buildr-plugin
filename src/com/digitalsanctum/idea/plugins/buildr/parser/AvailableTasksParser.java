package com.digitalsanctum.idea.plugins.buildr.parser;

import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.intellij.openapi.diagnostic.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 10:28:14 AM
 */
public class AvailableTasksParser {

  private static final Logger log = Logger.getInstance(AvailableTasksParser.class.getName());

  private static String test = "  artifacts             # Download all artifacts\n" +
      "  build                 # Build the project\n" +
      "  buildr:freeze         # Freeze the Buildfile so it always uses Buildr version 1.3.3\n" +
      "  buildr:unfreeze       # Unfreeze the Buildfile to use the latest version of Buildr\n" +
      "  clean                 # Clean files generated during a build\n" +
      "  compile               # Compile all projects\n" +
      "  default               # The default task is build\n" +
      "  deploy                # deploy\n" +
      "  eclipse               # Generate Eclipse artifacts for all projects\n" +
      "  help:projects         # List all projects defined by this buildfile\n" +
      "  help:tasks            # List all tasks available from this buildfile\n" +
      "  idea                  # Generate Idea artifacts for all projects\n" +
      "  idea7x                # Generate Idea 7.x artifacts for all projects\n" +
      "  install               # Install packages created by the project\n" +
      "  javadoc               # Create the Javadocs for this project\n" +
      "  junit:report          # Generate JUnit tests report in reports/junit\n" +
      "  package               # Create packages\n" +
      "  release               # Make a release\n" +
      "  test                  # Run all tests\n" +
      "  uninstall             # Remove previously installed packages\n" +
      "  upload                # Upload packages created by the project";


  public static void main(String[] args) {
    parseTasks(test);
  }

  public static List<BuildrTask> parseTasks(String tasksOutput) {

    if (tasksOutput == null || tasksOutput.length() == 0) throw new IllegalArgumentException("tasksOutput was null!");

    List<BuildrTask> tasks = new ArrayList<BuildrTask>();
    String newline = System.getProperty("line.separator");
    String[] taskLine = tasksOutput.split(newline);
    for (String s : taskLine) {
      String[] nameDesc = s.split("#");
      if (nameDesc.length == 2) {
        BuildrTask task = new BuildrTask(nameDesc[0].trim(), nameDesc[1].trim());
        tasks.add(task);
      }
    }

    return tasks;
  }

}
