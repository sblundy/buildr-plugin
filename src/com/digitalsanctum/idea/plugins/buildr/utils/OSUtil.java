package com.digitalsanctum.idea.plugins.buildr.utils;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.digitalsanctum.idea.plugins.buildr.lang.TextUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.StringTokenizer;

/**
 * User: witbsha
 * Date: Feb 4, 2008
 * Time: 10:55:41 AM
 */
public class OSUtil {

  private static final Logger LOG = Logger.getInstance(OSUtil.class.getName());

  private static final String JRUBY_CLASSPATH_NAME = "CLASSPATH";
  private static final String UNIX_PATH_NAME = "PATH";
  private static final String WINDOWS_PATH_NAME = "Path";


  public static boolean isWindows() {
    return SystemInfo.isWindows;
  }

  @Nullable
  public static String getPATHenvVariableName() {
    if (SystemInfo.isWindows) {
      return WINDOWS_PATH_NAME;
    }
    if (SystemInfo.isUnix) {
      return UNIX_PATH_NAME;
    }
    LOG.error(BuildrBundle.message("os.not.supported"));
    return null;
  }

  @Nullable
  public static String getJRubyCLASSPATHenvVariableName() {
    if (SystemInfo.isWindows || SystemInfo.isUnix) {
      return JRUBY_CLASSPATH_NAME;
    }
    LOG.error(BuildrBundle.message("os.not.supported"));
    return null;
  }

  public static String appendToPATHenvVariable(@Nullable final String path,
                                               @NotNull final String additionalPath) {
    final String pathValue;
    if (TextUtil.isEmpty(path)) {
      pathValue = additionalPath;
    } else {
      pathValue = path + File.pathSeparatorChar + additionalPath;
    }
    return VirtualFileUtil.convertToOSDependedPath(pathValue);
  }

  public static String getIdeaSystemPath() {
    return System.getenv().get(OSUtil.getPATHenvVariableName());
  }


  /**
   * Finds executable by name in standart path environment
   *
   * @param exeName executable name, gem for example
   * @return path if found
   */
  @Nullable
  public static String findExecutableByName(@NotNull final String exeName) {
    final String path = getIdeaSystemPath();
    final VirtualFileManager manager = VirtualFileManager.getInstance();
    if (path != null) {
      final StringTokenizer st = new StringTokenizer(path, File.pathSeparator);

      //tokens - are paths with system-dependent slashes
      while (st.hasMoreTokens()) {
        final String s = VirtualFileUtil.convertToVFSPathAndNormalizeSlashes(st.nextToken());
        final String possible_path = s + VirtualFileUtil.VFS_PATH_SEPARATOR + exeName;
        if (manager.findFileByUrl(VirtualFileUtil.constructLocalUrl(possible_path)) != null) {
          return possible_path;
        }
      }
    }
    return null;
  }

}
