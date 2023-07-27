package com.jayuroun.plugin.dnt.service;

import java.util.List;

public interface Proposer {

    List<String> propose(String text, LanguageChecker languageChecker);
}
