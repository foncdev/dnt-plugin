package com.jayuroun.plugin.dnt.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class DntV2Response implements DomainResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("code")
    private int code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("data")
    private List<DomainDto> items;

    @JsonProperty("errors")
    private List<Object> errors;

    @Override
    public List<DntDto> getDomains() {
        return items.stream().map( it -> {
            return  DntDto.builder()
                    .project(it.getProject())
                    .name(it.getName())
                    .code(it.getCode())
                    .dataType(it.getDataType())
                    .description(it.getDescription())
                    .hash(it.getHash())
                    .build();
        }).collect(Collectors.toList());
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
