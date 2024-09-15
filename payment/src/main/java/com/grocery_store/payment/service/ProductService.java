package com.grocery_store.payment.service;

import com.grocery_store.payment.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductService {

    @Value("${wiremock.url}")
    private String productApiUrl;

    private final RestTemplate restTemplate;

    public ProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<Product> getProducts() {
        String url = productApiUrl;
        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {});
        return response.getBody();
    }

    public Product getProductById(String id) {
        String url = productApiUrl + "/" + id;
        return restTemplate.getForObject(url, Product.class);
    }
}
