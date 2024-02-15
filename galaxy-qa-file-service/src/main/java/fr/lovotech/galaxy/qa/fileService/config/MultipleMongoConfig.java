package fr.lovotech.galaxy.qa.fileService.config;

import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MultipleMongoProperties.class)
public class MultipleMongoConfig {

    private final MultipleMongoProperties mongoProperties;

    @Primary
    @Bean(name = "defaultDataSource")
    public GridFsTemplate defaultGridFsTemplate(MongoConverter mongoConverter) throws Exception {
        return new GridFsTemplate(defaultFactory(),mongoConverter);
    }

    @Bean
    @Primary
    public MongoDbFactory defaultFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongoProperties.getDefaultDataSource().getHost(), mongoProperties.getDefaultDataSource().getPort()),
                mongoProperties.getDefaultDataSource().getDatabase());
    }
}
