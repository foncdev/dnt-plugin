package com.jayuroun.plugin.dnt.service;

import com.jayuroun.plugin.dnt.request.Auth;
import com.jayuroun.plugin.dnt.response.DntDto;
import com.jayuroun.plugin.dnt.response.DntModyResponse;
import com.jayuroun.plugin.dnt.response.DntV2ModyResponse;
import com.jayuroun.plugin.dnt.response.DomainResponse;
import com.jayuroun.plugin.dnt.service.impl.LanguageCheckerImpl;
import org.glassfish.jersey.client.ClientConfig;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;


public interface RestTemplate {

    Optional<DomainResponse> requestTranslate(Auth auth, int page, int limit,
                                              String projectName, String lang, String search);
    Optional<DomainResponse> requestSearch(Auth auth, int page, int limit, String lang, String search);
    Optional<DntModyResponse> requestModify(Auth auth, String project, String domain, String lang,
                                            String code, String dataType, String description);


    Optional<DntV2ModyResponse> requestModifyV2(Auth auth, String project, String domain, String lang,
                                                String code, String dataType, String description);


    default WebTarget createWebTarget(String url) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        return client.target(url);
    }

    default List<DntDto> domainSearch(Auth auth, String search) throws UnsupportedEncodingException {
        int page = 0;
        int limit = 20;
        LanguageChecker checker = new LanguageCheckerImpl();
        String lang = checker.detect(search);

        try {
            return requestSearch(auth, page, limit, lang, search)
                    .map(DomainResponse::getDomains)
                    .orElse(new ArrayList<>());
        } catch ( Exception ex ) {

        }

        return new ArrayList<>();
    }

    default List<DntDto> dtnSearch(Auth auth, String projectName, String search) throws UnsupportedEncodingException {

        int page = 0;
        int limit = 5;

        LanguageChecker checker = new LanguageCheckerImpl();
        String lang = checker.detect(search);
        try {
            return requestTranslate(auth, page, limit, projectName, lang, search)
                    .map(DomainResponse::getDomains)
                    .orElse(new ArrayList<>());
        } catch ( Exception ex ) {

        }

        List<DntDto> results = new ArrayList<>();
        results.add(DntDto.builder()
                .project(projectName)
                .name(search)
                .code(search)
                .dataType("")
                .build());
        return new ArrayList<>();
    }
}
