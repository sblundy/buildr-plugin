package com.digitalsanctum.idea.plugins.buildr.run;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.concurrency.Semaphore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * User: shane
 * Date: Feb 2, 2008
 * Time: 6:08:10 PM
 */
public class ExecutionHelper {
  private static Logger LOG = Logger.getInstance(ExecutionHelper.class.getName());


  public static void showErrors(@NotNull final Project myProject,
                                @NotNull final List<Exception> exceptionList,
                                @NotNull final String tabDisplayName,
                                @Nullable final VirtualFile file) {
    if (ApplicationManager.getApplication().isUnitTestMode() && !exceptionList.isEmpty()) {
      throw new RuntimeException(exceptionList.get(0));
    }
  }


  public static void executeExternalProcess(@Nullable final Project myProject,
                                            @NotNull final ProcessHandler processHandler,
                                            @NotNull final Runner.ExecutionMode mode) {
    final String title = mode.getTitle() != null
        ? mode.getTitle()
        : "Running Buildr task...please wait";
    assert title != null;

    final Runnable process;
    if (mode.cancelable()) {
// here     
      process = createCancableExecutionProcess(processHandler);
    } else {
      process = new Runnable() {
        public void run() {
          processHandler.waitFor();
        }
      };

    }
    if (mode.withModalProgress()) {
      ProgressManager.getInstance().runProcessWithProgressSynchronously(process, title, mode.cancelable(), myProject);
    } else if (mode.inBackGround()) {
      final Task task = new Task.Backgroundable(myProject, title, mode.cancelable()) {
        public void run(final ProgressIndicator indicator) {
          process.run();
        }
      };
      ProgressManager.getInstance().run(task);
    } else {
      final String title2 = mode.getTitle2();
      final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
      if (indicator != null && title2 != null) {
        indicator.setText2(title2);
      }
      process.run();
    }
  }

  private static Runnable createCancableExecutionProcess(final ProcessHandler processHandler) {
    return new Runnable() {
      private ProgressIndicator myProgressIndicator;
      private Semaphore mySemaphore = new Semaphore();

      private Runnable myWaitThread = new Runnable() {
        public void run() {
          processHandler.waitFor();
          mySemaphore.up();
        }
      };

      private Runnable myCancelListener = new Runnable() {
        public void run() {
          for (; ;) {
            if (myProgressIndicator != null && (myProgressIndicator.isCanceled() || !myProgressIndicator.isRunning())) {
              processHandler.destroyProcess();
              mySemaphore.up();
              break;
            } else if (myProgressIndicator == null) {
              return;
            }
            try {
              synchronized (this) {
                wait(1000);
              }
            }
            catch (InterruptedException e) {
              //Do nothing
            }
          }
        }
      };

      public void run() {
        myProgressIndicator = ProgressManager.getInstance().getProgressIndicator();
        if (myProgressIndicator != null) {
          myProgressIndicator.setText("Running Buildr task...please wait");
        }

        ApplicationManager.getApplication().executeOnPooledThread(myWaitThread);
        ApplicationManager.getApplication().executeOnPooledThread(myCancelListener);

        mySemaphore.down();
        mySemaphore.waitFor();
      }
    };
  }
}
