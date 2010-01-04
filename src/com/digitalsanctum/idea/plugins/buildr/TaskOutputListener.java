package com.digitalsanctum.idea.plugins.buildr;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 10:04:26 PM
 */
public interface TaskOutputListener {
    void clearTaskOutput();

    void log(String msg);

    void error(String msg);
}
