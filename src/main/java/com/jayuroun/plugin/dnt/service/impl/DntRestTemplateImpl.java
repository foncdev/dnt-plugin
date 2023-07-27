package com.jayuroun.plugin.dnt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.jayuroun.plugin.dnt.request.Auth;
import com.jayuroun.plugin.dnt.response.*;
import com.jayuroun.plugin.dnt.service.RestTemplate;

public class DntRestTemplateImpl implements RestTemplate {

    private final String serverUrl;

    public DntRestTemplateImpl(String url) {
        this.serverUrl = url;
    }

    @Override
    public Optional<DomainResponse> requestTranslate(Auth auth, int page, int limit, String projectName, String lang, String search) {

        List<DntResponse.DomainDto> result = new ArrayList<>();

        WebTarget webTarget = createWebTarget(serverUrl + "/v1/domain/transactions")
                .queryParam("page","" + page)
                .queryParam("limit","" + limit)
                .queryParam("search","" + search)
                .queryParam("lang", lang)
                .queryParam("project",projectName);

        Invocation.Builder builder;
        builder = webTarget.request();
        builder.header(Auth.Jayuroun.HEADER_CLIENT_ID, auth.getJayuroun().getClientId());
        builder.header(Auth.Jayuroun.HEADER_CLIENT_SECRET, auth.getJayuroun().getSecretKey());
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.accept(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if(response.getStatus() != 200){
            return Optional.empty();
        }

        return Optional.of( response.readEntity(DntResponse.class) );
    }

    @Override
    public Optional<DomainResponse> requestSearch(Auth auth, int page, int limit, String lang, String search) {

        List<DntResponse.DomainDto> result = new ArrayList<>();

        WebTarget webTarget = createWebTarget(serverUrl + "/v1/domain").queryParam("page","" + page)
                .queryParam("limit","" + limit)
                .queryParam("search","" + search)
                .queryParam("lang",lang);

        Invocation.Builder builder;
        builder = webTarget.request();
        builder.header(Auth.Jayuroun.HEADER_CLIENT_ID, auth.getJayuroun().getClientId());
        builder.header(Auth.Jayuroun.HEADER_CLIENT_SECRET, auth.getJayuroun().getSecretKey());
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.accept(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if(response.getStatus() != 200){
            return Optional.empty();
        }

        return Optional.of( response.readEntity(DntResponse.class) );
    }

    @Override
    public Optional<DntModyResponse> requestModify(Auth auth, String project, String domain, String lang, String code,
                                                   String dataType, String description) {

        DntDto requestBody = DntDto.builder()
                .project(project)
                .name(domain)
                .code(code)
                .lang(lang)
                .dataType(dataType)
                .description(description)
                .hash("")
                .build();

        Response response = createWebTarget(serverUrl + "/v1/domain")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(Auth.Jayuroun.HEADER_CLIENT_ID, auth.getJayuroun().getClientId())
                .header(Auth.Jayuroun.HEADER_CLIENT_SECRET, auth.getJayuroun().getSecretKey())
                .header("charset", "UTF-8")
                .post(Entity.json(requestBody), Response.class);


        if(response.getStatus() != 200){
            return Optional.empty();
        }

        return Optional.of( response.readEntity(DntModyResponse.class) );
    }

    @Override
    public Optional<DntV2ModyResponse> requestModifyV2(Auth auth, String project, String domain, String lang, String code, String dataType, String description) {
        return Optional.empty();
    }
}
