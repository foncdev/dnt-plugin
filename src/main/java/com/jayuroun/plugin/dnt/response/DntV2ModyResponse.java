package com.jayuroun.plugin.dnt.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class DntV2ModyResponse implements ModifyResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("code")
    private int code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("data")
    private DomainDto items;

    @JsonProperty("errors")
    private List<Object> errors;


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
    public static class ErrorDto {
        @JsonProperty("name")
        private String name;
    }

        @Data
    @NoArgsConstructor
    public static class DomainDto {
        @JsonProperty("id")
        private String id;

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

        @JsonProperty("data_type")
        private String dataType;

        @JsonProperty("description")
        private String description;

        @JsonProperty("used_count")
        private int usedCount;

        @JsonProperty("hash")
        private String hash;
    }
}
