package com.jayuroun.plugin.dnt.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DntDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("project")
    private String project;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("domain")
    private String name;

    @JsonProperty("code")
    private String code;

    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("used_count")
    private int usedCount;

    @JsonProperty("description")
    private String description;
    private String hash;
}
