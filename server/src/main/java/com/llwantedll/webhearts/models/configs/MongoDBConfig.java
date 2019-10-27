package com.llwantedll.webhearts.models.configs;

import com.llwantedll.webhearts.models.repositories.RoleRepository;
import com.llwantedll.webhearts.models.repositories.UserRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = {RoleRepository.class, UserRepository.class})
public class MongoDBConfig extends AbstractMongoConfiguration {

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    }

    @Override
    protected String getDatabaseName() {
        return "webhearts";
    }
}
