package com.jayuroun.plugin.dnt.form.dialog.modifydomain;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jayuroun.plugin.dnt.exception.EmptyAuthException;
import com.jayuroun.plugin.dnt.request.Auth;
import com.jayuroun.plugin.dnt.response.DntModyResponse;
import com.jayuroun.plugin.dnt.response.DntV2ModyResponse;
import com.jayuroun.plugin.dnt.service.RestTemplate;
import com.jayuroun.plugin.dnt.service.impl.DntRestTemplateImpl;
import com.jayuroun.plugin.dnt.service.impl.DntV2RestTemplateImpl;
import com.jayuroun.plugin.dnt.service.impl.LanguageCheckerImpl;
import com.vladsch.flexmark.util.html.ui.Color;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ModifyDomainDialog extends JDialog {
    private JPanel contentPane;
    private JLabel projectNameLabel;
    private JLabel domainNameLabel;
    private JLabel abbreviationLabel;
    private JLabel codeLabel;
    private JLabel descriptionLabel;
    private JLabel messageLabel;
    private JTextField projectNameTextField;
    private JTextField domainNameTextField;
    private JTextField abbreviationTextField;
    private JTextField codeTextField;
    private JTextArea descriptionTextArea;
    private JButton btnApply;
    private JButton btnClone;


    private static final String TITLE = "Domain Information";

    private String apiUrl;
    private Auth auth;
    private String apiType;


    private String projectName = "";
    private String dominName = "";

    private String codeName = "";

    private String description = "";

    private String lang = "";

    private LanguageCheckerImpl checker;

    public ModifyDomainDialog(Auth auth, String apiType, String apiUrl,  String projectName, String domainName, String codeName, String description) {
        this.auth = auth;
        this.projectName = projectName;
        this.dominName = domainName;
        this.codeName = codeName;
        this.description = description;
        this.apiUrl = apiUrl;
        this.apiType = apiType;

        btnClone.addActionListener(e -> onCancel());
        btnApply.addActionListener(e -> onApply());

        this.checker = new LanguageCheckerImpl();

        init();
    }

    public ModifyDomainDialog(EmptyAuthException exception){
        init();
//        translatedTextLabel.setText(eae.getMessage());
    }

    public void onShowing(AnActionEvent e){
        this.pack();
        this.setSize(450, 550);
        this.setTitle(TITLE);
        applyColor();
        this.setVisible(true);
    }

    private void applyColor(){

        messageLabel.setForeground(Color.pink);

//        if(UIUtil.isUnderDarcula()){
//            Color dracula = new Color(43,43,43);
//            translatedPane.setBackground(dracula);
//            queryTextField.setBackground(dracula);
//            queryTextField.setForeground(Color.WHITE);
//            translatedTextLabel.setForeground(Color.WHITE);
//        } else {
//            Color lowBlue = new Color(48, 99, 191);
//            translatedPane.setBackground(Color.WHITE);
//            queryTextField.setBackground(Color.WHITE);
//            queryTextField.setForeground(lowBlue);
//            translatedTextLabel.setForeground(lowBlue);
//        }

    }

    private void init() {

        setContentPane(contentPane);
        setModal(true);

        projectNameTextField.setText(this.projectName);
        domainNameTextField.setText(this.dominName);
        codeTextField.setText(this.codeName);
        descriptionTextArea.setText(this.description);


        getRootPane().setDefaultButton(btnClone);
//
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void update() {
        this.projectName = projectNameTextField.getText();
        this.dominName = domainNameTextField.getText();
        this.codeName = codeTextField.getText();
        this.description = descriptionTextArea.getText();
        this.lang = this.checker.detect(this.dominName);
    }

    private void onCancel() {
        dispose();
    }

    private void onApply() {

        update();
        RestTemplate restTemplate;
        if ( apiType.equals("DNTV2 Server") ) {
            restTemplate = new DntV2RestTemplateImpl(apiUrl);
            try {
                DntV2ModyResponse dntModyResponse = restTemplate.requestModifyV2(auth, projectName, dominName,
                        lang, codeName, "none", description).orElseThrow( () -> new EmptyAuthException("") );

                if (dntModyResponse.isSuccess()) {
                    dispose();
                } else {
                    messageLabel.setText(dntModyResponse.getMsg());
                }
            } catch ( Exception ex ) {
                messageLabel.setText("알 수 없는 오류가 발생했습니다.");
            }
        } else {
            restTemplate = new DntRestTemplateImpl(apiUrl);
            try {
                DntModyResponse dntModyResponse = restTemplate.requestModify(auth, projectName, dominName,
                        lang, codeName, "none", description).orElseThrow( () -> new EmptyAuthException("") );

                if (dntModyResponse.isSuccess()) {
                    dispose();
                } else {
                    messageLabel.setText(dntModyResponse.getMsg());
                }
            } catch ( Exception ex ) {
                messageLabel.setText("알 수 없는 오류가 발생했습니다.");
            }
        }
    }
}
