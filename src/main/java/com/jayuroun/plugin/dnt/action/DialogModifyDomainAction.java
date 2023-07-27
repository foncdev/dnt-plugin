package com.jayuroun.plugin.dnt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.jayuroun.plugin.dnt.utils.ConfigPrjReader;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.jayuroun.plugin.dnt.exception.EmptyAuthException;
import com.jayuroun.plugin.dnt.form.dialog.modifydomain.ModifyDomainDialog;
import com.jayuroun.plugin.dnt.form.preferences.DntConfig;
import com.jayuroun.plugin.dnt.request.Auth;
import com.jayuroun.plugin.dnt.service.LanguageChecker;
import com.jayuroun.plugin.dnt.service.impl.LanguageCheckerImpl;

public class DialogModifyDomainAction extends AnAction {

    private SelectionModel selectionModel;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Auth auth;
        ModifyDomainDialog dialog;

        // [code] // [description]
        // 프로젝트 명
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        Project project = event.getRequiredData(CommonDataKeys.PROJECT);

        String projectName = project.getName();
        String apiType = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getApiType();
        String apiUrl = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getApiUrl();
        String clientId = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getClientId();
        String secretKey = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getSecretKey();
        String domainName = "";
        String codeName = "";
        String description = "";


        if (editor != null && editor.getProject() != null) {
            Document document = editor.getDocument();
            ConfigPrjReader configPrjReader = new ConfigPrjReader(project, document);
            projectName = configPrjReader.getProjectName();


            this.selectionModel = editor.getSelectionModel();
            String blockSelectedText = selectionModel.getSelectedText();
            String search = StringUtils.isNotEmpty(blockSelectedText) ? blockSelectedText : getAutoSelectedText(selectionModel);

            LanguageChecker checker = new LanguageCheckerImpl();
            String[] words = search.split("\\s");
            boolean isComment = false;
            boolean isDomain = false;
            boolean isCode = false;

            for (String word : words) {
                if ( word.equals("//") ) {
                    isComment = true;
                    continue;
                }
                if ( isComment == true ) {
                    if ( description.length() > 0 ) {
                        description += " ";
                    }
                    description += word;
                } else if (isDomain == false && checker.detect(word).equals("ko")
                        || checker.detect(word).equals("ja")
                        || checker.detect(word).equals("zh-cn")
                        || checker.detect(word).equals("zh-tw")) {
                    isDomain = true;
                    domainName = word;

                } else if (isCode == false && checker.detect(word).equals("en") ) {
                    isCode = true;
                    codeName = camelToSnake(word);
                }
            }

            try {
                auth = Auth.newJayurounInstance(clientId, secretKey);
                dialog = new ModifyDomainDialog(auth, apiType, apiUrl, projectName, domainName, codeName, description);
            } catch (EmptyAuthException eae){
                dialog = new ModifyDomainDialog(eae);
            }

            dialog.onShowing(event);
        }

    }


    private String getAutoSelectedText(SelectionModel selectionModel) {
        selectionModel.selectWordAtCaret(false);
        return selectionModel.getSelectedText();
    }


    private String camelToSnake(String str)
    {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        str = str.replaceAll(regex, replacement).toLowerCase();
        return str;
    }
}
