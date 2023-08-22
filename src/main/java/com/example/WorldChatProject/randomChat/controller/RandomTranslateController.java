package com.example.WorldChatProject.randomChat.controller;

import com.example.WorldChatProject.randomChat.dto.RandomTranslateDTO;
import com.example.WorldChatProject.randomChat.service.RandomTranslateService;
import com.example.WorldChatProject.user.dto.UserDTO;
import com.example.WorldChatProject.user.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/randomTranslate")
@Slf4j
@RequiredArgsConstructor
public class RandomTranslateController {
    private final RandomTranslateService translateService;

    @PostMapping("/translate")
    public String translate(@RequestBody RandomTranslateDTO randomTranslateDTO, Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        UserDTO userDTO = principal.getUser();
        String nation = userDTO.getUserNationality();
        String sourceCode = randomTranslateDTO.toSourceCode(nation);
        randomTranslateDTO.setSourceCode(sourceCode);
        return translateService.translate(randomTranslateDTO);
    }

    @PostMapping("detect")
    public String detect(@RequestBody RandomTranslateDTO randomTranslateDTO) {

    }


}
