package com.jayuroun.plugin.dnt.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.jayuroun.plugin.dnt.component.Selector;
import com.jayuroun.plugin.dnt.form.preferences.DntConfig;
import com.jayuroun.plugin.dnt.request.Auth;
import com.jayuroun.plugin.dnt.response.DntDto;
import com.jayuroun.plugin.dnt.service.RestTemplate;
import com.jayuroun.plugin.dnt.service.impl.DntRestTemplateImpl;
import com.jayuroun.plugin.dnt.service.impl.DntV2RestTemplateImpl;
import com.jayuroun.plugin.dnt.utils.CodeCase;
import com.jayuroun.plugin.dnt.utils.MessageConverter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Translator extends AnAction {

    private SelectionModel selectionModel;

    private String porjectColor = "#F1C40F";
    private String domainColor = "#F7DC6F";
    private String codeColor = "#F8F9F9";
    private String descriptionColor = "#D5DBDB";

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        String apiType = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getApiType();
        String apiUrl = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getApiUrl();
        String clientId = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getClientId();
        String secretKey = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getSecretKey();

        if (editor != null && editor.getProject() != null) {
            this.selectionModel = editor.getSelectionModel();
            String blockSelectedText = selectionModel.getSelectedText();
            String selectedText = StringUtils.isNotEmpty(blockSelectedText) ? blockSelectedText : getAutoSelectedText(selectionModel);
            String search = CodeCase.camelToSnake(selectedText);

            RestTemplate restTemplate;
            if ( apiType.equals("DNTV2 Server") ) {
                restTemplate = new DntV2RestTemplateImpl(apiUrl);
            } else {
                restTemplate = new DntRestTemplateImpl(apiUrl);
            }
            Auth auth = Auth.newJayurounInstance(clientId, secretKey);

            try {
                List<DntDto> domainList = restTemplate.domainSearch(auth, search);
                StringBuilder sb = new StringBuilder();
                sb.append("<br/>");

                for (DntDto item: domainList) {
                    String dataType = (item.getDataType() != null && !item.getDataType().isEmpty()) ?
                            " (<b>" + item.getDataType() + "<b>)" : "";
                    String codeHtml = String.format("%s %s", item.getCode(), dataType);

                    String descriptionHtml = (item.getDescription() != null && !item.getDescription().isEmpty()) ?
                            String.format("<span style=\"color:%s;\">Description : %s <br/>",
                                    descriptionColor, item.getDescription() ) : "";

                    String lineText = String.format("<b style=\"color:%s;\">Porject Id : %s </b><br/>" +
                                    "<span style=\"color:%s;\">Domain : %s <br/>" +
                                    "<span style=\"color:%s;\">Code : %s <br/>" + "%s",
                            porjectColor, item.getProject(), domainColor, item.getName(),
                            codeColor, codeHtml, descriptionHtml);
                    sb.append(lineText).append("<br/><br/>");
                }

                JComponent jComponent = editor.getContentComponent();
                Selector selector = new Selector(event);
                JBPopupFactory.getInstance()
                        .createHtmlTextBalloonBuilder(
                                MessageConverter.applyTranslateStyle("[" + search + "]", sb.toString()),
                                null,
                                Color.GRAY,
                                null)
                        .setFadeoutTime(7500)
                        .createBalloon()
                        .show(new RelativePoint(jComponent, selector.extractPoint()), Balloon.Position.below);

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

    }


    private String getAutoSelectedText(SelectionModel selectionModel) {
        selectionModel.selectWordAtCaret(false);
        return selectionModel.getSelectedText();
    }

}
