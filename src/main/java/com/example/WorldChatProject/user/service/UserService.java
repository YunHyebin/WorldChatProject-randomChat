package com.example.WorldChatProject.user.service;

import com.example.WorldChatProject.user.common.FileUtils;
import com.example.WorldChatProject.user.dto.ResponseDTO;
import com.example.WorldChatProject.user.dto.UserDTO;
import com.example.WorldChatProject.user.entity.User;
import com.example.WorldChatProject.user.repository.UserRepository;
import com.example.WorldChatProject.user.security.auth.PrincipalDetails;
import com.example.WorldChatProject.user.security.entity.RefreshToken;
import com.example.WorldChatProject.user.security.jwt.JwtProperties;
import com.example.WorldChatProject.user.security.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Value("${file.path}")
    String attachPath;
    private final FileUtils fileUtils;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void UserLogout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        log.info("로그아웃 시작");
        session.invalidate();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //principal 안에는 유저의 정보가 담겨있음
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        if (authentication != null) {
            //리프레쉬 토큰 초기화 후 로그아웃
            RefreshToken refreshToken = refreshTokenRepository.findByKeyId(principal.getUsername()).get();
            refreshToken.setRefreshToken("");
            refreshTokenRepository.save(refreshToken);
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            log.info("로그아웃 성공");
        }
    }

    public void UserJoin(User user) {
        user.setUserPwd(bCryptPasswordEncoder.encode(user.getUserPwd()));
        //user 권한 부여
        user.setUserRoles("ROLE_USER");
        userRepository.save(user);
    }

    public ResponseEntity<String> UserIdCheck(String userName) {
        if (userRepository.findByUserName(userName).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body("fail");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
    }

    public ResponseEntity<String> UserMessageChange(String userMessage, String userName) {
        User user = userRepository.findByUserName(userName).get();
        user.setUserMessage(userMessage);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    public ResponseEntity<String> UserPwdCheck(String userPwd, String userName) {
        User user = userRepository.findByUserName(userName).get();
        if (bCryptPasswordEncoder.matches(userPwd, user.getUserPwd())) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("fail");
        }
    }

    public ResponseEntity<String> UserPwdSave(String NewUserPwd, String userName) {
        User user = userRepository.findByUserName(userName).get();
        user.setUserPwd(bCryptPasswordEncoder.encode(NewUserPwd));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    public ResponseEntity<String> UserUploadImage(MultipartFile imageFile, String userName) {
        User user = userRepository.findByUserName(userName).get();
        log.info("프로필 변경 진입");
        try {
            File directory = new File(attachPath);

            if (!directory.exists()) {
                directory.mkdir();
            }

            User userProfileSave = new User();
            userProfileSave = fileUtils.parseFileInfo(imageFile, attachPath, "userProfile/");

            user.setUserProfileName(userProfileSave.getUserProfileName());
            user.setUserProfileOrigin(userProfileSave.getUserProfileOrigin());
            user.setUserProfilePath(userProfileSave.getUserProfilePath());

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body("image upload");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("image upload fail");
        }
    }


    public ResponseEntity<String> UserNickNameCheck(String userNickName) {
        if (userRepository.findByUserNickName(userNickName).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body("fail");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
    }


    public User findById(long userId) {
        Optional<User> checkUser = userRepository.findById(userId);
        if(checkUser.isEmpty()) {
            return null;
        } else {
            return checkUser.get();
        }
    }



    public ResponseEntity<?> UserFriendsList(String userName) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();
        try {

            List<User> userList = userRepository.findAll();

            List<UserDTO> userDTOList = new ArrayList<>();

            for(User user : userList) {
                userDTOList.add(user.EntityToDTO());
            }

            responseDTO.setItems(userDTOList);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            log.info(responseDTO.getItems().get(0).getUserNickName());
            log.info(responseDTO.getItems().get(1).getUserNickName());

            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
