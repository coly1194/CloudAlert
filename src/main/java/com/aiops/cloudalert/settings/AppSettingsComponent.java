// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.aiops.cloudalert.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

  private final JPanel myMainPanel;
  private final JBTextField userNameText = new JBTextField();
  private final JBTextField passwordText = new JBTextField();
//  private final JBCheckBox myIdeaUserStatus = new JBCheckBox("Do you use IntelliJ IDEA? ");

  public AppSettingsComponent() {
    myMainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(new JBLabel("Enter eamil: "), userNameText, 1, false)
            .addLabeledComponent(new JBLabel("Enter access_token: "), passwordText, 1, false)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
  }

  public JPanel getPanel() {
    return myMainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return userNameText;
  }

  @NotNull
  public String getUserNameText() {
    return userNameText.getText();
  }

  public void setUserNameText(@NotNull String newText) {
    userNameText.setText(newText);
  }
  @NotNull
  public String getPasswordText() {
    return passwordText.getText();
  }

  public void setPasswordText(@NotNull String newText) {
    passwordText.setText(newText);
  }

//  public boolean getIdeaUserStatus() {
//    return myIdeaUserStatus.isSelected();
//  }
//
//  public void setIdeaUserStatus(boolean newStatus) {
//    myIdeaUserStatus.setSelected(newStatus);
//  }

}
