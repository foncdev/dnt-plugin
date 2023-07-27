package com.jayuroun.plugin.dnt.action;

import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

import com.jayuroun.plugin.dnt.utils.CodeCase;
import org.jetbrains.annotations.NotNull;

public class CamelSnakeReplacer extends AnAction {
    private SelectionModel selectionModel;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = event.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();

        if (editor != null && editor.getProject() != null) {

            this.selectionModel = editor.getSelectionModel();
            String blockSelectedText = selectionModel.getSelectedText();
            if (!blockSelectedText.isEmpty()) {
                String code;
                if (blockSelectedText.indexOf("_") >= 0) {
                    code = CodeCase.snakeToCamel(blockSelectedText);
                    // snake -> camel
                } else {
                    // camel -> snake
                    code = CodeCase.camelToSnake(blockSelectedText);
                }


                Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
                int start = primaryCaret.getSelectionStart();
                int end = primaryCaret.getSelectionEnd();
                // Replace the selection with a fixed string.
                // Must do this document change in a write action context.
                WriteCommandAction.runWriteCommandAction(project, () ->
                        document.replaceString(start, end, code)
                );
                // De-select the text range that was just replaced
                primaryCaret.removeSelection();
            }
        }
    }

    @Override
    public void update(@NotNull final AnActionEvent event) {
        final Project project = event.getProject();
        final Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabledAndVisible(
                project != null && editor != null && editor.getSelectionModel().hasSelection()
        );
    }
}
