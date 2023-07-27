package com.jayuroun.plugin.dnt.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.CaseFormat;

import com.jayuroun.plugin.dnt.service.LanguageChecker;
import com.jayuroun.plugin.dnt.service.Proposer;


public class ProposerImpl implements Proposer {

    public List<String> propose(String text, LanguageChecker languageChecker) {
        if(isEnglish(text, languageChecker)){
            return createPropositions(text.replaceAll(" ", "_").toLowerCase());
        }

        return Collections.singletonList(text);
    }

    private boolean isEnglish(String text, LanguageChecker languageChecker) {
        return languageChecker.detect(text).equals("en");
    }

    private List<String> createPropositions(String text){
        return Arrays.asList(
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, text),
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, text),
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, text),
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, text)
        );
    }

}
