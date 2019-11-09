package com.llwantedll.webhearts.models.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "role")
public class Role implements Serializable {

    @Id
    private ObjectId id;

    @Indexed(unique = true, sparse = true)
    private String key;

    public Role(String key) {
        this.key = key;
    }

    public Role() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
