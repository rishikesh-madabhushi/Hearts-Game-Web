package com.llwantedll.webhearts.models.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Document(collection = "game_room")
public class GameRoom {
    @Id
    private ObjectId id;

    @Indexed(unique = true, sparse = true)
    private String name;

    private String status;

    @DBRef(lazy = true)
    private List<User> users;

    private Object gameData;

    private String password;

    private int maxPlayers;

    private int minPlayers;

    private Date startedDate;

    public GameRoom() {
        users = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User> getUsers() {
        return users;
    }

    public void removeUser(User user) {
        String givenUserId = user.getId().toString();

        Optional<User> first = this.users
                .stream()
                .filter(e -> e.getId().toString().equals(givenUserId))
                .findFirst();

        first.ifPresent(userFromSet -> this.users.remove(userFromSet));
    }

    public void setUser(User user) {
        this.users.add(user);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Object getGameData() {
        return gameData;
    }

    public void setGameData(Object gameData) {
        this.gameData = gameData;
    }
}
