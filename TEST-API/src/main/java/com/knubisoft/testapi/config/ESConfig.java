package com.knubisoft.testapi.config;

import com.knubisoft.testapi.util.converter.LocalDateToLongConverter;
import com.knubisoft.testapi.util.converter.LongToLocalDateConverter;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Arrays;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.knubisoft.testapi.repository.es")
@ConditionalOnExpression("${es.enabled:true}")
public class ESConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.data.es.url}")
    private String esUrl;
    @Value("${spring.data.es.username}")
    private String username;
    @Value("${spring.data.es.password}")
    private String password;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(esUrl)
                .withBasicAuth(username, password)
                .build();

        return RestClients.create(configuration).rest();
    }

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(Arrays.asList(
                new LongToLocalDateConverter(),
                new LocalDateToLongConverter()));
    }
}
