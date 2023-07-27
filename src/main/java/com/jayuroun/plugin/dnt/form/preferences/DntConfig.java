package com.jayuroun.plugin.dnt.form.preferences;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;


@NoArgsConstructor
@State(
        name="DntConfig",
        storages = {
                @Storage("DntConfig.xml")}
)
public class DntConfig implements PersistentStateComponent<DntConfig> {

    private String apiType = "DNT Server";
    private String apiUrl = "https://jayutest.best:10030";
    private String secretKey = "";
    private String clientId = "";
    private String variableType = "none";

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Nullable
    @Override
    public DntConfig getState() {
        return this;
    }

    @Override
    public void loadState(DntConfig state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static DntConfig getInstance(Project project){
        return ServiceManager.getService(project, DntConfig.class);
    }
}
