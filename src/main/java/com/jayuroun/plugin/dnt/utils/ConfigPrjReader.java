package com.jayuroun.plugin.dnt.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;

import java.io.*;
import java.util.Properties;

public class ConfigPrjReader {
    private Properties properties = null;
    private final Project project;

    public ConfigPrjReader(Project project, Document document) {
        this.project = project;
        properties = new Properties();
        try {
            File file = new File(project.getPresentableUrl(), ".dntconfig");
            if (file.exists()) {
                BufferedReader reader;
                reader = new BufferedReader(new FileReader(file.getPath()));
                properties.load(reader);
                reader.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public String getProjectName() {
        if ( properties == null ) return project.getName();
        return  properties.getProperty("projectName") != null ?
                properties.getProperty("projectName") : project.getName();
    }

}
