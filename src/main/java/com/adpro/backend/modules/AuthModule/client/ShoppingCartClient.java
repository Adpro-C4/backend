package com.adpro.backend.modules.authmodule.client;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShoppingCartClient {

    private static final String
     BASE_URL = "https://api-gateway-specialitystore.up.railway.app/purchase-service/shopping-cart";
    public ResponseEntity<Object> createShoppingCart(String userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestData = mapper.createObjectNode().put("userId", userId);
            // Convert JsonNode to JSON string
            String requestBody = mapper.writeValueAsString(requestData);
            // Create request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Object> response = restTemplate.postForEntity(BASE_URL + "/create", requestEntity, Object.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /* 
    public static void main(String[] args) {
        ShoppingCartClient client = new ShoppingCartClient();
        
        // Example JSON data
        String userId = "123";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode requestData = mapper.createObjectNode().put("userId", userId);
        
        // Call createShoppingCart method
        ResponseEntity<Object> response = client.createShoppingCart(requestData);
        System.out.println("Response: " + response);
    }*/
}

