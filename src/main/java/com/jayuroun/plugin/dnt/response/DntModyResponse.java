package com.jayuroun.plugin.dnt.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.stream.Collectors;


@Getter
@Setter
public class DntModyResponse implements ModifyResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("code")
    private int code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("data")
    private DomainDto items;

    @Override
    public DntDto getDomains() {

        if ( items == null ) {
            return  DntDto.builder()
                    .project("")
                    .name("")
                    .code("")
                    .dataType("")
                    .description("")
                    .hash("")
                    .build();
        }

        return  DntDto.builder()
                .project(items.getProject())
                .name(items.getName())
                .code(items.getCode())
                .dataType(items.getDataType())
                .description(items.getDescription())
                .hash(items.getHash())
                .build();
    }


    @Data
    @NoArgsConstructor
    public static class DomainDto {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("project")
        private String project;
        @JsonProperty("domain")
        private String name;

        @JsonProperty("code")
        private String code;

        @JsonProperty("abbreviation")
        private String abbreviation;

        @JsonProperty("lang")
        private String lang;

        @JsonProperty("dataType")
        private String dataType;

        @JsonProperty("description")
        private String description;

        @JsonProperty("usedCount")
        private int usedCount;

        @JsonProperty("hash")
        private String hash;
    }
}
