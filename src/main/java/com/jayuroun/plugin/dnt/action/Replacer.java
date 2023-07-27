package com.jayuroun.plugin.dnt.action;

import com.google.common.base.CaseFormat;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.ui.awt.RelativePoint;
import com.jayuroun.plugin.dnt.component.Selector;
import com.jayuroun.plugin.dnt.form.preferences.DntConfig;
import com.jayuroun.plugin.dnt.request.Auth;
import com.jayuroun.plugin.dnt.response.DntDto;
import com.jayuroun.plugin.dnt.response.DntResponse;
import com.jayuroun.plugin.dnt.service.RestTemplate;
import com.jayuroun.plugin.dnt.service.impl.DntRestTemplateImpl;
import com.jayuroun.plugin.dnt.service.impl.DntV2RestTemplateImpl;
import com.jayuroun.plugin.dnt.utils.CodeCase;
import com.jayuroun.plugin.dnt.utils.ConfigPrjReader;
import com.jayuroun.plugin.dnt.utils.MessageConverter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class Replacer extends AnAction {
    private SelectionModel selectionModel;

    private boolean ourLookupShown = true;


    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Language lang = event.getData(CommonDataKeys.PSI_FILE).getLanguage();
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        Project project = event.getRequiredData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        String projectName = project.getName();

        ConfigPrjReader configPrjReader = new ConfigPrjReader(project, document);
        projectName = configPrjReader.getProjectName();




        if (editor != null && editor.getProject() != null) {

            this.selectionModel = editor.getSelectionModel();
            String blockSelectedText = selectionModel.getSelectedText();
            String selectedText = StringUtils.isNotEmpty(blockSelectedText) ? blockSelectedText : getAutoSelectedText(selectionModel);
            String search = CodeCase.camelToSnake(selectedText);

            String apiType = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getApiType();
            String apiUrl = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getApiUrl();
            String clientId = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getClientId();
            String secretKey = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getSecretKey();
            String variableType = DntConfig.getInstance(event.getRequiredData(CommonDataKeys.PROJECT)).getVariableType();

            RestTemplate restTemplate;
            if ( apiType.equals("DNTV2 Server") ) {
                restTemplate = new DntV2RestTemplateImpl(apiUrl);
            } else {
                restTemplate = new DntRestTemplateImpl(apiUrl);
            }

            Auth auth = Auth.newJayurounInstance(clientId, secretKey);
            try {
                List<DntDto> domainList = restTemplate.dtnSearch(auth, projectName, search);

                if (domainList.size() == 0) {
                    JComponent jComponent = editor.getContentComponent();
                    Selector selector = new Selector(event);
                    JBPopupFactory.getInstance()
                        .createHtmlTextBalloonBuilder(
                            MessageConverter.applyTranslateStyle(search, "No data found."),
                            null,
                            Color.GRAY,
                            null)
                            .setFadeoutTime(7500)
                            .createBalloon()
                            .show(new RelativePoint(jComponent, selector.extractPoint()), Balloon.Position.below);
                } else {


                    LookupElement[] elements = domainList.stream().map(it -> {

                        // camel

                        // 포맷 1
                        // [주석]
                        // [데이터 타입] [변수명]

                        // 포맷 2
                        // [데이터 타입] [변수명] [주석]

                        // 주석 포맷
                        // [프로젝트 명] 설명

                        // 카멜
//                        String codeText = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, it.getCode());
                        String codeText = "";
                        if ( variableType.equals("camel") ) {
                            codeText  = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, it.getCode());
                        } else if ( variableType.equals("snake") ) {
                            codeText = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, it.getCode());
                        } else {
                            codeText = it.getCode();
                        }


                        return LookupElementBuilder.create(it, codeText)
                                .withTypeText(it.getName())
                                .withLookupString(codeText)
                                .withPresentableText(it.getCode())
                                .withCaseSensitivity(true)
                                .appendTailText(" -> " + codeText, true)
//                                .withItemTextForeground(Color.WHITE)
                                .withStrikeoutness(false)
//                                .withInsertHandler((context, item) -> {
//                                    Document insertDocument = context.getDocument();
//                                    int pos = context.getSelectionEndOffset();
//                                    document.insertString(pos, "innnnn");
//                                })
                                ;
                    }).toArray(LookupElement[]::new);


                    ApplicationManager.getApplication().invokeLater(() ->
                            Objects.requireNonNull(LookupManager.getInstance(editor.getProject()).showLookup(editor, elements))
//                                .addLookupListener(new LookupListener() {
//                                    @Override
//                                    public void itemSelected(LookupEvent e) {
//                                        LookupElement item = e.getItem();
//                                        Editor editor = e.getLookup().getEditor();
//                                        Document document = editor.getDocument();
//                                        OffsetMap offsetMap = new OffsetMap(editor.getDocument());
//                                        PsiFile psiFile = e.getLookup().getPsiFile();
//                                    }
//                                })
                    );
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private String getAutoSelectedText(SelectionModel selectionModel) {
        selectionModel.selectWordAtCaret(false);
        return selectionModel.getSelectedText();
    }

    private static InsertionContext createInsertionContext(LookupElement item,
                                                           PsiFile psiFile,
                                                           List<? extends LookupElement> elements,
                                                           Editor editor, final char completionChar) {
        final OffsetMap offsetMap = new OffsetMap(editor.getDocument());
        final InsertionContext context = new InsertionContext(offsetMap, completionChar, elements.toArray(new LookupElement[elements.size()]), psiFile, editor, false);
        context.setTailOffset(editor.getCaretModel().getOffset());
        offsetMap.addOffset(CompletionInitializationContext.START_OFFSET, context.getTailOffset() - item.getLookupString().length());
        offsetMap.addOffset(CompletionInitializationContext.SELECTION_END_OFFSET, context.getTailOffset());
        offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, context.getTailOffset());
        return context;
    }

}
