package com.jayuroun.plugin.dnt.utils;

import org.jetbrains.annotations.NotNull;

public class MessageConverter {
    private static final String CAMEL_CASE_REGEX = "([a-z])([A-Z]+)";

    public static String convert(String target) {
        return target
                .replaceAll(CAMEL_CASE_REGEX, "$1 $2")
                .replaceAll("_", " ");
    }

    private static final String XML_REGEX = "(<.*\">)|(</.*>)";

    public static String removeXmlTag(String target){
        return target.replaceAll(XML_REGEX, "");
    }

    @NotNull
    public static String applyTranslateStyle(String text, String translatedText) {
        //DarkOrange
        String title = "<p style='text-align:left;color:#FF8C00;font-size:16px'><strong>" + text + "</strong></p>";
        String content = "<p style='color:#F8F8FF;font-size:12px'>" + translatedText + "</p>";
        return title+content;
    }

    public static String applyExceptionStyle(String message) {
        return "<p style='text-align:left;color:#FF8C00;font-size:14px'><strong>" + message + "</strong></p>";
    }
}
