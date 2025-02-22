package com.example.WorldChatProject.frdChat.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.example.WorldChatProject.frdChat.entity.FrdChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrdChatMessageDTO {

    private Long id;
    private long roomId;
    private String sender;
    private String message;
    private String createdAt;
    private String userProfile;

    /* 파일 업로드 관련 변수 */
    private String s3DataUrl; // 파일 업로드 url
    private String fileName; // 파일이름
    private String fileDir; // s3 파일 경로

    public FrdChatMessage DTOToEntity() {
        return FrdChatMessage.builder()
                .id(this.id)
                .roomId(this.roomId)
                .sender(this.sender)
                .message(this.message)
                .createdAt(this.createdAt)
                .s3DataUrl(this.s3DataUrl)
                .fileName(this.fileName)
                .fileDir(this.fileDir)
                .build();
    }
}
