package com.digitalsanctum.idea.plugins.buildr;

import com.intellij.CommonBundle;
import com.intellij.reference.SoftReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.util.ResourceBundle;

/**
 * User: shane
 * Date: Nov 22, 2008
 * Time: 10:15:21 AM
 */
public class BuildrBundle {
  private static Reference<ResourceBundle> ourBundle;

  @NonNls
  private static final String BUNDLE = "com.digitalsanctum.idea.plugins.buildr.BuildrBundle";

  public static String message(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
    return CommonBundle.message(getBundle(), key, params);
  }

  private static ResourceBundle getBundle() {
    ResourceBundle bundle = null;
    if (ourBundle != null) bundle = ourBundle.get();
    if (bundle == null) {
      bundle = ResourceBundle.getBundle(BUNDLE);
      ourBundle = new SoftReference<ResourceBundle>(bundle);
    }
    return bundle;
  }
}
