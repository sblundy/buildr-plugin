package com.digitalsanctum.idea.plugins.buildr.run;

/**
 * User: shane
 * Date: Feb 2, 2008
 * Time: 3:27:08 PM
 */

import com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException;
import com.digitalsanctum.idea.plugins.buildr.lang.TextUtil;
import com.digitalsanctum.idea.plugins.buildr.utils.OSUtil;
import com.digitalsanctum.idea.plugins.buildr.utils.VirtualFileUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Runner {
  private static final Logger LOG = Logger.getInstance(Runner.class.getName());

  /**
   * Returns output after execution.
   *
   * @param workingDir working directory
   * @param command    Command to execute @return Output object
   * @return Output
   * @throws com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException
   *
   */
  @NotNull
  public static Output runInPath(@Nullable final String workingDir,
                                 @NotNull final String... command) throws BuildrPluginException {
    return runInPathInternal(workingDir, null, null, false, new Runner.SameThreadMode(false), command);
  }

  public static Output runInPathAndShowErrors(@Nullable final String workingDir,
                                              @Nullable Project project,
                                              @NotNull final ExecutionMode mode,
                                              final boolean showStdErrErrors, @Nullable final String errorTitle,
                                              @NotNull final String... command) throws BuildrPluginException {
    return runInPathInternal(workingDir, project, errorTitle, showStdErrErrors, mode, command);
  }

  /**
   * Returns output after execution.
   *
   * @param workingDir working directory
   * @param project    Project
   * @param errorTitle Title for Message tab
   * @param showErrors If true, all data from stderr will be shown as errors in Message tab. In this case project must be not null!
   * @param mode       Execution mode
   * @param command    Command to execute @return Output object
   * @return Output       Output
   * @throws com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException
   *
   */
  @NotNull
  private static Output runInPathInternal(@Nullable final String workingDir,
                                          @Nullable Project project,
                                          @Nullable final String errorTitle,
                                          final boolean showErrors,
                                          @NotNull final ExecutionMode mode,
                                          @NotNull final String... command) throws BuildrPluginException {
// executing
    final StringBuilder out = new StringBuilder();
    final StringBuilder err = new StringBuilder();
    final Process process = createProcess(workingDir, command);
    final OSProcessHandler osProcessHandler = new OSProcessHandler(process, TextUtil.concat(command));
    osProcessHandler.addProcessListener(new OutputListener(out, err));
    osProcessHandler.startNotify();

    ExecutionHelper.executeExternalProcess(project, osProcessHandler, mode);

    final Output output = new Output(out.toString(), err.toString());

    if (showErrors && !TextUtil.isEmpty(output.getStderr())) {
      assert project != null;
      final String tabName = errorTitle != null
          ? errorTitle
          : "unknown error";

      final List<Exception> errorList = new LinkedList<Exception>();
      //noinspection ThrowableInstanceNeverThrown
      errorList.add(new Exception(output.getStderr()));
      ExecutionHelper.showErrors(project, errorList, tabName, null);
    }

    return output;
  }


  /**
   * Returns output after execution.
   *
   * @param command Command to execute
   * @return Output object
   * @throws com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException
   *
   */
  @NotNull
  public static Output run(@NotNull final String... command) throws BuildrPluginException {
    return runInPath(null, command);
  }

  /**
   * Creates add by command and working directory
   *
   * @param command    add command line
   * @param workingDir add working directory or null, if no special needed
   * @return add
   * @throws com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException
   *
   */
  @Nullable
  public static Process createProcess(@Nullable final String workingDir,
                                      @NotNull final String... command) throws BuildrPluginException {
    Process process = null;

    final String[] arguments;
    if (command.length > 1) {
      arguments = new String[command.length - 1];
      System.arraycopy(command, 1, arguments, 0, command.length - 1);
    } else {
      arguments = new String[0];
    }

    final GeneralCommandLine cmdLine = createAndSetupCmdLine(null, workingDir, command[0], arguments);
    try {

      process = cmdLine.createProcess();

    } catch (ExecutionException e) {
      LOG.error("Unable to setup Buildr command line", e);
    } catch (Exception e) {
      LOG.error("Unable to setup Buildr command line", e);
    }

    if (process == null) {
      throw new BuildrPluginException("Buildr must be installed and added to your PATH");
    }

    return process;
  }

  @NotNull
  public static Output execute(@NotNull final GeneralCommandLine cmdLine) throws ExecutionException {
    final StringBuilder out = new StringBuilder();
    final StringBuilder err = new StringBuilder();

    final Process process = cmdLine.createProcess();
    final ProcessHandler osProcessHandler = new OSProcessHandler(process, cmdLine.getCommandLineString());

    osProcessHandler.addProcessListener(new OutputListener(out, err));
    osProcessHandler.startNotify();
    osProcessHandler.waitFor();

    return new Output(out.toString(), err.toString());
  }


  /**
   * Creates process builder and setups it's commandLine, working directory, enviroment variables
   *
   * @param additionalLoadPath Additional load path
   * @param workingDir         Process working dir
   * @param executablePath     Path to executable file
   * @param arguments          Process commandLine
   * @return process builder
   */
  public static GeneralCommandLine createAndSetupCmdLine(@Nullable final String additionalLoadPath,
                                                         @Nullable final String workingDir,
                                                         @NotNull final String executablePath,
                                                         @NotNull final String... arguments) {
    final GeneralCommandLine cmdLine = new GeneralCommandLine();

    cmdLine.setExePath(VirtualFileUtil.convertToOSDependedPath(executablePath));
    if (workingDir != null) {
      cmdLine.setWorkDirectory(VirtualFileUtil.convertToOSDependedPath(workingDir));
    }
    cmdLine.addParameters(arguments);

    //Plugin env variables
    final Map<String, String> env = getSystemEnv();

    //PATH
    final String PATH_KEY = OSUtil.getPATHenvVariableName();
    final String path = env.get(PATH_KEY);
    //Additional Extention
    if (!TextUtil.isEmpty(additionalLoadPath)) {
      //noinspection ConstantConditions
      env.put(PATH_KEY, OSUtil.appendToPATHenvVariable(path, additionalLoadPath));
    }
    cmdLine.setEnvParams(env);

    return cmdLine;
  }

  public static Map<String, String> getSystemEnv() {
    return new HashMap<String, String>(System.getenv());
  }

  public static class OutputListener implements ProcessListener {
    private final StringBuilder out;
    private final StringBuilder err;

    public OutputListener(@NotNull final StringBuilder out, @NotNull final StringBuilder err) {
      this.out = out;
      this.err = err;
    }

    public void startNotified(ProcessEvent event) {
    }

    public void processTerminated(ProcessEvent event) {
    }

    public void processWillTerminate(ProcessEvent event, boolean willBeDestroyed) {
    }

    public void onTextAvailable(ProcessEvent event, Key outputType) {
      if (outputType == ProcessOutputTypes.STDOUT) {
        out.append(event.getText());
      }
      if (outputType == ProcessOutputTypes.STDERR) {
        err.append(event.getText());
      }
    }
  }

  public static abstract class ExecutionMode {
    private boolean myCancelable;
    private String myTitle;
    private String myTitle2;
    private boolean myRunWithModal;
    private boolean myRunInBG;

    public ExecutionMode(final boolean cancelable,
                         @Nullable final String title,
                         @Nullable final String title2, final boolean runInBG, final boolean runWithModal) {
      myCancelable = cancelable;
      myTitle = title;
      myTitle2 = title2;
      myRunInBG = runInBG;
      myRunWithModal = runWithModal;
    }

    @Nullable
    public String getTitle() {
      return myTitle;
    }

    @Nullable
    public String getTitle2() {
      return myTitle2;
    }

    public boolean cancelable() {
      return myCancelable;
    }

    public boolean inBackGround() {
      return myRunInBG;
    }

    public boolean withModalProgress() {
      return myRunWithModal;
    }
  }

  /**
   * Process will be run in back ground mode
   */
  public static class BackGroundMode extends ExecutionMode {

    public BackGroundMode(final boolean cancelable,
                          @Nullable final String title) {
      super(cancelable, title, null, true, false);
    }

    public BackGroundMode(@Nullable final String title) {
      this(true, title);
    }
  }

  /**
   * Process will be run in modal dialog
   */
  public static class ModalProgressMode extends ExecutionMode {

    public ModalProgressMode(final boolean cancelable,
                             @Nullable final String title) {
      super(cancelable, title, null, false, true);
    }

    public ModalProgressMode(@Nullable final String title) {
      this(true, title);
    }
  }

  /**
   * Process will be run in the same thread.
   */
  public static class SameThreadMode extends ExecutionMode {
    public SameThreadMode(final boolean cancelable,
                          @Nullable final String title2) {
      super(cancelable, null, title2, false, false);
    }

    public SameThreadMode(@Nullable final String title2) {
      this(true, title2);
    }

    public SameThreadMode(final boolean cancelable) {
      this(cancelable, null);
    }

    public SameThreadMode() {
      this(true);
    }
  }
}