package com.example.spring_stomp_chat.chat.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomParticipantJpaRepository extends JpaRepository<ChatRoomParticipantJpaEntity, Long> {

    @Query("""
            select p from ChatRoomParticipantJpaEntity p
            join fetch p.chatRoom r
            where p.participantId = :participantId
            and p.isActive = true
            order by r.lastMessageAt desc
            """)
    List<ChatRoomParticipantJpaEntity> findByParticipantId(@Param("participantId") Long participantId);

    @Query("""
            select p from ChatRoomParticipantJpaEntity p
            join fetch p.chatRoom r
            where p.participantId != :participantId
            and p.chatRoom.id in :roomIds
            """)
    List<ChatRoomParticipantJpaEntity> findAllByChatRoomIdIn(@Param("roomIds") List<Long> roomIds,
                                                             @Param("participantId") Long participantId);

    List<ChatRoomParticipantJpaEntity> findByChatRoomId(Long chatRoomId);
}
