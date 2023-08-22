package com.example.WorldChatProject.randomChat.service.impl;

import com.example.WorldChatProject.randomChat.configuration.RandomTranslateConfiguration;
import com.example.WorldChatProject.randomChat.dto.RandomTranslateDTO;
import com.example.WorldChatProject.randomChat.service.RandomTranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class RandomTranslateServiceImpl implements RandomTranslateService {
    private final RandomTranslateConfiguration randomTranslateConfiguration;

    //텍스트를 번역하고 post 메소드 호출하여 번역결과가 responseBody에 담김
    @Override
    public String translate(RandomTranslateDTO randomTranslateDTO) {
        String translatedText;
        String client_id = randomTranslateConfiguration.getCLIENT_ID();
        String client_secret = randomTranslateConfiguration.getCLIENT_SECRET();
        String api_url = randomTranslateConfiguration.getPAPAGO_API_URL();
        String text = randomTranslateDTO.getText();

        try{
            translatedText = URLEncoder.encode(text, "UTF-8");
            randomTranslateDTO.setText(translatedText);
        } catch (UnsupportedEncodingException e) {
            log.error("encoding failure: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", client_id);
        requestHeaders.put("X-Naver-Client_Secret", client_secret);

        String responseBody = post(api_url, requestHeaders, randomTranslateDTO);
        return responseBody;
    }

    //API에 POST 요청 전송하고 반환
    //readBody 메소드로부터 응답 데이터 받아 반환
    @Override
    public String post(String apiUrl, Map<String, String> requestHeaders, RandomTranslateDTO randomTranslateDTO) {
        String encodedText = randomTranslateDTO.getText();
        String sourceCode = randomTranslateDTO.getSourceCode();
        String targetCode = randomTranslateDTO.getTargetCode();
        HttpURLConnection connection = connect(apiUrl);
        String postParams = "source=" + sourceCode +"&target=" + targetCode + encodedText;
        try{
            connection.setRequestMethod("POST");
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(connection.getInputStream());
            }else {
                return readBody(connection.getErrorStream());
            }
        }catch(IOException e) {
            throw new RuntimeException("API 요청 응답 실패", e);
        }finally {
            connection.disconnect();
        }
    }

    //API URL로부터 HttpURLConnection 객체 생성하고 반환
    //URL 잘못되거나 연결 과정에 문제 생기면 예외 발생
    @Override
    public HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    //InputStream으로부터 데이터 읽어 문자열로 반환
    //읽은 데이터를 StringBuilder에 저장하고 줄 단위로 처리
    @Override
    public String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
