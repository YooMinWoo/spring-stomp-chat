package com.example.spring_stomp_chat.chat.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageJpaEntity, Long> {

    @Query("""
            select m from ChatMessageJpaEntity m
            join fetch m.chatRoom r
            where m.chatRoom in :chatRooms
            order by m.createdTime desc
            """)
    List<ChatMessageJpaEntity> findAllByChatRooms(@Param("chatRooms") List<ChatRoomJpaEntity> chatRooms);

    @Query("""
    select m from ChatMessageJpaEntity m
    where m.id in (
        select max(m2.id)
        from ChatMessageJpaEntity m2
        where m2.chatRoom.id in :roomIds
        group by m2.chatRoom.id
    )
    """)
    List<ChatMessageJpaEntity> findLatestMessagesByRoomIds(@Param("roomIds") List<Long> roomIds);
}
