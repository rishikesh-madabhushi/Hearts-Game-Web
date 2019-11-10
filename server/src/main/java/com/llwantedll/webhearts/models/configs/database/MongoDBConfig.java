package com.llwantedll.webhearts.models.configs.database;

import com.llwantedll.webhearts.models.configs.ConfigurationData;
import com.llwantedll.webhearts.models.repositories.GameRoomRepository;
import com.llwantedll.webhearts.models.repositories.RoleRepository;
import com.llwantedll.webhearts.models.repositories.UserRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses =
        {RoleRepository.class, UserRepository.class, GameRoomRepository.class})
public class MongoDBConfig extends AbstractMongoConfiguration {

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(ConfigurationData.DATABASE_URL));
    }

    @Override
    protected String getDatabaseName() {
        return ConfigurationData.WEBHEARTS_DATABASE;
    }
}
