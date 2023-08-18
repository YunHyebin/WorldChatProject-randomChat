package com.example.WorldChatProject.frdChat.entity;

import com.example.WorldChatProject.frdChat.dto.FrdChatMessageDTO;
import com.example.WorldChatProject.frdChat.dto.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrdChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long roomId;
    private String sender;
    private String message;
    private String createdAt;

    /* 파일 업로드 관련 변수 */
    private String s3DataUrl; // 파일 업로드 url
    private String fileName; // 파일이름
    private String fileDir; // s3 파일 경로

    public FrdChatMessageDTO EntityToDTO() {
        return FrdChatMessageDTO.builder()
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
