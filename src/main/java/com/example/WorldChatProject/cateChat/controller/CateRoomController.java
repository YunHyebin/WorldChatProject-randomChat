package com.example.WorldChatProject.cateChat.controller;

import com.example.WorldChatProject.cateChat.dto.CateRoomDTO;
import com.example.WorldChatProject.cateChat.entity.CateChat;
import com.example.WorldChatProject.cateChat.entity.CateRoom;
import com.example.WorldChatProject.cateChat.service.CateChatService;
import com.example.WorldChatProject.cateChat.service.CateRoomService;
import com.example.WorldChatProject.cateChat.service.CateUserListService;
import com.example.WorldChatProject.user.entity.User;
import com.example.WorldChatProject.user.security.auth.PrincipalDetails;
import com.example.WorldChatProject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Slf4j
public class CateRoomController {

    private final CateRoomService cateRoomService;
    private final CateChatService cateChatService;
    private final CateUserListService cateUserListService;

    @GetMapping("/cateChat/roomList/{category}")
    private ResponseEntity<?> cateChatRoomList(@PathVariable String category) {
        log.info(category);
        return cateRoomService.getAllCateChatRoom(category);
    }

    //채팅방 만들기
    @PostMapping("/cateChat/createCateRoom")
    //@ModelAttribute CateRoom cateRoom은
    //HTTP POST 요청으로 전송된 데이터를 CateRoom 객체에 바인딩하는 역할
    public ResponseEntity createCateRoom(@RequestBody CateRoom cateRoom) {
        log.info(cateRoom.getCateName());
        log.info(cateRoom.getInterest().name());
        return cateRoomService.createCateRoom(cateRoom);
    }

    @GetMapping("/cateChat/enter")
    public ResponseEntity<?> checkIfCateChatIsFull(@RequestParam String cateId, Authentication authentication) {
        log.info(cateId);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        CateRoom cateRoom = cateRoomService.getChatRoom(Long.parseLong(cateId));

        List<CateChat> cateChatList = cateChatService.getMessagesByCateRoom(Long.parseLong(cateId));

        cateUserListService.save(Long.parseLong(cateId), principal.getUsername());

        List<String> userList = cateUserListService.findAllUserNamesByCateId(Long.parseLong(cateId));

        boolean isFull = cateRoom.getMaxUserCnt() <= cateRoom.getCateUserCnt();

        // 값들을 담을 Map 객체 생성
        Map<String, Object> responseResult = new HashMap<>();
        responseResult.put("cateChatList", cateChatList);
        responseResult.put("userList", userList);
        responseResult.put("cateRoom", cateRoom);
        responseResult.put("isFull", isFull);

        return ResponseEntity.ok(responseResult);
    }
}