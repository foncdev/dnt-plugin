package com.jayuroun.plugin.dnt.form.preferences;

import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;


public class DntConfigurableGUI {
    private JPanel rootPanel;
    private JComboBox apiType;
    private JTextField secretKeyField;
    private JTextField clientIdField;
    private JTextField apiUrlField;
    private JLabel secretKeyTitle;
    private JLabel clientIdTitle;
    private JLabel apiUrlTitle;
    private JRadioButton variableNone;
    private JRadioButton variableSnake;
    private JRadioButton variableCamel;

    private DntConfig config;

    public DntConfigurableGUI() {
    }

    public void createUI(Project project) {
        config = DntConfig.getInstance(project);
        apiType.setSelectedItem(config.getApiType());
        secretKeyField.setText(config.getSecretKey());
        clientIdField.setText(config.getClientId());
        apiUrlField.setText(config.getApiUrl());

        if (config.getVariableType().equals("none")) {
            variableNone.setSelected(true);
            variableCamel.setSelected(false);
            variableSnake.setSelected(false);
        } else if (config.getVariableType().equals("camel")) {
            variableNone.setSelected(false);
            variableCamel.setSelected(true);
            variableSnake.setSelected(false);
        } else if (config.getVariableType().equals("snake")) {
            variableNone.setSelected(false);
            variableCamel.setSelected(false);
            variableSnake.setSelected(true);
        }
        setEnabled();
        apiType.addItemListener(e -> setEnabled());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void setEnabled() {
        if (apiType.getSelectedItem().equals("DNT Server")) {
            useDefault();
        } else if (apiType.getSelectedItem().equals("DNTV2 Server")) {
            useCustom();
        } else if (apiType.getSelectedItem().equals("Custom Server")) {
            useCustom();
        } else {
            useCustom();
        }
    }

    private void useDefault() {
        secretKeyField.setEnabled(true);
        clientIdField.setEditable(true);
        apiUrlField.setEnabled(false);
        apiUrlField.setText("https://jayutest.best:10030");
    }

    private void useCustom() {
        secretKeyField.setEnabled(true);
        clientIdField.setEditable(true);
        apiUrlField.setEnabled(true);
        secretKeyField.setText(config.getSecretKey());
        clientIdField.setText(config.getClientId());
        apiUrlField.setText(config.getApiUrl());
    }


    public void apply() {
        config.setApiType(apiType.getSelectedItem().toString());
        config.setSecretKey(secretKeyField.getText());
        config.setClientId(clientIdField.getText());
        config.setApiUrl(apiUrlField.getText());
        config.setVariableType(variableValue());
    }

    public boolean isModified() {

        if (isModifiedApiType()) {
            return true;
        }

        if (isModifiedSecretKey()) {
            return true;
        }

        if (isModifiedClientId()) {
            return true;
        }

        if (isModifiedAipUrl()) {
            return true;
        }

        if (isModifiedVariableType()) {
            return true;
        }

        return false;
    }

    private String variableValue() {

        if (variableNone.isSelected()) {
            return "none";
        } else if (variableCamel.isSelected()) {
            return "camel";
        } else if (variableSnake.isSelected()) {
            return "snake";
        }

        return "none";
    }

    private boolean isModifiedApiType() {
        return !StringUtils.equals(apiType.getSelectedItem().toString(), config.getApiType());
    }

    private boolean isModifiedSecretKey() {
        return !StringUtils.equals(secretKeyField.getText(), config.getSecretKey());
    }

    private boolean isModifiedClientId() {
        return !StringUtils.equals(clientIdField.getText(), config.getClientId());
    }

    private boolean isModifiedAipUrl() {
        return !StringUtils.equals(apiUrlField.getText(), config.getApiUrl());
    }

    private boolean isModifiedVariableType() {

        if (config.getVariableType().equals("none")) {
            return !variableNone.isSelected();
        } else if (config.getVariableType().equals("camel")) {
            return !variableCamel.isSelected();
        } else if (config.getVariableType().equals("snake")) {
            return !variableSnake.isSelected();
        }

        return true;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
