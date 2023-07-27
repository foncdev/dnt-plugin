package com.jayuroun.plugin.dnt.service;

public interface LanguageChecker {
    String detect(String text);
    String exchange(String language);
}
