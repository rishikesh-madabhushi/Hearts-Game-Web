package com.llwantedll.webhearts.models.repositories;

import com.llwantedll.webhearts.models.entities.GameRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRoomRepository extends MongoRepository<GameRoom, String> {
    GameRoom getByName(String name);

    long countByName(String name);

    @Query("{ 'status' : ?0 }")
    List<GameRoom> getOpenRooms(String status, Pageable pageable);

    @Query(value = "{ 'status' : ?0 }", count = true)
    long getOpenRoomsCount(String status);
}
