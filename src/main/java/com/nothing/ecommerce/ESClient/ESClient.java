package com.nothing.ecommerce.ESClient;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.nothing.ecommerce.config.HttpClientConfigImpl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Component
public class ESClient {

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        // Create a RestClientBuilder with your Elasticsearch host and port.
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "https"));

        // Set up custom HttpClientConfigCallback to load SSL certificate and
        // truststore.
        RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback = new HttpClientConfigImpl();
        builder.setHttpClientConfigCallback(httpClientConfigCallback);

        // Create a ElasticsearchClient with the RestClient.
        RestClient restClient = builder.build();

        // Create a ElasticsearchClientTransport with the RestClient and
        // JacksonJsonpMapper.
        RestClientTransport restClientTransport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // Create and return the ElasticsearchClient.
        return new ElasticsearchClient(restClientTransport);
    }
}
