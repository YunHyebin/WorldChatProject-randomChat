package com.example.WorldChatProject.randomChat.service;

import com.example.WorldChatProject.randomChat.dto.RandomTranslateDTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public interface RandomTranslateService {

    //텍스트 번역
    String translate(RandomTranslateDTO randomTranslateDTO);

    //API에 post 요청 및 결과 반환
    String post(String apiUrl, Map<String, String>requestHeaders, RandomTranslateDTO randomTranslateDTO) throws IOException;

    //API URL로부터 HttpURlConnection객체 생성하고 반환
    HttpURLConnection connect(String apiUrl);

    //데이터 읽어 문자열로 반환
    String readBody(InputStream body);


}
