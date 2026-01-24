package com.example.spring_stomp_chat.chat.adapter.out.persistence;

import com.example.spring_stomp_chat.chat.domain.model.ChatRoomParticipant;
import com.example.spring_stomp_chat.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room_participants")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoomParticipantJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomJpaEntity chatRoom;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    // 마지막으로 읽은 시간
    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    // 현재 참여 상태 (나갔으면 false)
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // 나갔다가 다시 들어올 때를 위한 현재 참여 시작 시간
    @Column(name = "current_joined_at")
    private LocalDateTime currentJoinedAt;

    // 나간 시간
    @Column(name = "left_at")
    private LocalDateTime leftAt;
}
