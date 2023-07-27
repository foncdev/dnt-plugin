package com.jayuroun.plugin.dnt.form.preferences;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DntConfigurable implements SearchableConfigurable {

    private DntConfigurableGUI gui;

    @SuppressWarnings("FieldCanBeLocal")
    private final Project project;

    public DntConfigurable(@NotNull Project project) {
        this.project = project;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        gui = new DntConfigurableGUI();
        gui.createUI(project);
        return gui.getRootPanel();
    }

    @Override
    public void disposeUIResources() {
        gui = null;
    }

    @NotNull
    @Override
    public String getId() {
        return "preference.DntConfigurable";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "DNT Configurable";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.DntConfigurable";
    }

    @Override
    public boolean isModified() {
        return gui.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        gui.apply();
    }
}
