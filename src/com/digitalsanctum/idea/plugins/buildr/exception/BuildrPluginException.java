package com.digitalsanctum.idea.plugins.buildr.exception;

/**
 * User: shane
 * Date: Feb 11, 2008
 * Time: 8:37:51 PM
 */
public class BuildrPluginException extends Exception {

  public BuildrPluginException() {
    super();
  }

  public BuildrPluginException(String message) {
    super(message);
  }

  public BuildrPluginException(String message, Throwable cause) {
    super(message, cause);
  }

  public BuildrPluginException(Throwable cause) {
    super(cause);
  }
}
