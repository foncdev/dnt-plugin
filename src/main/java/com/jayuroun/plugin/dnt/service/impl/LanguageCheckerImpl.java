package com.jayuroun.plugin.dnt.service.impl;

import java.io.IOException;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import com.jayuroun.plugin.dnt.service.LanguageChecker;


public class LanguageCheckerImpl implements LanguageChecker {

    private LanguageDetector languageDetector;
    private TextObjectFactory textObjectFactory;

    public LanguageCheckerImpl() {
        try {
            languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(new LanguageProfileReader().readAllBuiltIn())
                    .build();
        } catch (IOException e) {

        }
        textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
    }

    @Override
    public String detect(String text) {
        //query:
        TextObject textObject = textObjectFactory.forText(text);
        Optional<LdLocale> lang = languageDetector.detect(textObject);

        if(lang.isPresent()){
            return lang.get().getLanguage();
        }

        return "en";
    }

    @Override
    public String exchange(String language) {

        if("en".equals(language)){
            return "ko";
        } else if("ko".equals(language)){
            return "en";
        }

        return "ko";
    }
}