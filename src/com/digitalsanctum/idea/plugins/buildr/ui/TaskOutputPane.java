package com.digitalsanctum.idea.plugins.buildr.ui;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * User: shane
 * Date: Feb 5, 2008
 * Time: 12:02:20 AM
 */
public class TaskOutputPane {

  private static final String CONTENT_TYPE = "text/html";
  private static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");

  private JPanel mainPanel;
  private JEditorPane outputPane;
  private JButton clearButton;

  public TaskOutputPane() {

    final Color bgColor = EditorColorsManager.getInstance().getGlobalScheme().getColor(EditorColors.READONLY_BACKGROUND_COLOR);
    outputPane.setBackground(bgColor != null ? bgColor : Color.WHITE);
    outputPane.setEditable(false);
    outputPane.setContentType(CONTENT_TYPE);

    clearButton.setEnabled(true);
    clearButton.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        clear();
      }
    });
    
  }

  private void createUIComponents() {
    outputPane = new JEditorPane();
  }

  public JComponent getPanel() {
    return mainPanel;
  }

  private void clear() {
    outputPane.setText("");
  }

  private void disableControls() {
    clearButton.setEnabled(false);
  }

  private void enableControls() {
    clearButton.setEnabled(true);
  }

  public void log(String msg) {
    disableControls();
    StringBuilder sb = new StringBuilder();
    sb.append("<pre>");
    sb.append(msg);
    sb.append("\nTask Completed! ").append(getTimeStamp());
    sb.append("</pre>");    
    outputPane.setForeground(Color.DARK_GRAY);
    outputPane.setText(sb.toString());
    enableControls();
  }

  public void error(String msg) {
    disableControls();
    StringBuilder sb = new StringBuilder();
    sb.append("<pre>");
    sb.append(msg);
    sb.append("</pre>");
    outputPane.setForeground(Color.RED);
    outputPane.setText(sb.toString());
    enableControls();
  }

  private String getTimeStamp() {
    return formatter.format(new Date());
  }


}
