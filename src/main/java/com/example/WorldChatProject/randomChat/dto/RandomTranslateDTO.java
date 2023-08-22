package com.example.WorldChatProject.randomChat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class RandomTranslateDTO {
    private String text;
    private String targetCode;
    private String sourceCode;
    private static final Map<String, String> nation;

    static {
        nation = new HashMap<>();
        nation.put("KR", "ko");
        nation.put("US", "en");
        nation.put("JP", "ja");
        nation.put("CN", "zh-CN");
        nation.put("VI", "vi");
        nation.put("TH", "th");
        nation.put("ID", "id");
        nation.put("FR", "fr");
        nation.put("ES", "es");
        nation.put("RU", "ru");
        nation.put("DE", "de");
        nation.put("IT", "it");
    }

    public String toSourceCode(String nationality) {
        String targetCode = nation.getOrDefault(nationality, "Unknown");
        return targetCode;
    }
}
