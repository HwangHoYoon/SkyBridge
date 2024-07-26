package com.skybridge.sky.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StudyPlanWrapper {

    private Section intro;
    private Section outro;
    private Map<String, Plan> plans = new HashMap<>();

    // Getters and setters

    @Data
    public static class Section {
        private List<String> contents;
    }

    @Data
    public static class Plan {
        private List<String> date;
        private String title;
        private List<String> contents;
    }

    @JsonProperty("intro")
    public Section getIntro() {
        return intro;
    }

    @JsonProperty("outro")
    public Section getOutro() {
        return outro;
    }

    @JsonAnySetter
    public void setPlan(String key, Plan plan) {
        this.plans.put(key, plan);
    }
}
