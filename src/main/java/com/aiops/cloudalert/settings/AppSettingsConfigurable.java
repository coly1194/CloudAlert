// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.aiops.cloudalert.settings;

import com.intellij.openapi.options.Configurable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements Configurable {

  private AppSettingsComponent mySettingsComponent;

  // A default constructor with no arguments is required because this implementation
  // is registered as an applicationConfigurable EP

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Cloud Alert";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return mySettingsComponent.getPreferredFocusedComponent();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    mySettingsComponent = new AppSettingsComponent();
    return mySettingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    AppSettingsState settings = AppSettingsState.getInstance();
    boolean modified = (!mySettingsComponent.getUserNameText().equals(settings.username)&&!mySettingsComponent.getUserNameText().isBlank());
    modified |= ((mySettingsComponent.getPasswordText() != settings.password)&&!mySettingsComponent.getPasswordText().isBlank());
    return modified;
  }

  @Override
  public void apply() {
    AppSettingsState settings = AppSettingsState.getInstance();
    settings.username = mySettingsComponent.getUserNameText();
    settings.password = mySettingsComponent.getPasswordText();
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
    mySettingsComponent.setUserNameText(settings.username);
    mySettingsComponent.setPasswordText(settings.password);
  }

  @Override
  public void disposeUIResources() {
    mySettingsComponent = null;
  }

}
