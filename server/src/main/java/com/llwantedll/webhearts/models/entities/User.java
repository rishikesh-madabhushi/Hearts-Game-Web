package com.llwantedll.webhearts.models.entities;

import com.llwantedll.webhearts.models.configs.ConfigurationData;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
public class User implements Serializable {

    @Id
    private ObjectId id;

    @Indexed(unique = true, sparse = true)
    private String login;

    private String password;

    @DBRef(lazy = true)
    private List<Role> roles;

    @Indexed(unique = true, sparse = true)
    private String email;

    @DBRef(lazy = true)
    private List<GameRoom> gameRooms;

    public User() {
        roles = new ArrayList<>();
        gameRooms = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setRole(Role role) {
        this.roles.add(role);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<GameRoom> getGameRooms() {
        return gameRooms;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRooms.add(gameRoom);
    }

    public void setGameRooms(List<GameRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }
}
