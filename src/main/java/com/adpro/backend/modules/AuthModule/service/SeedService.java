package com.adpro.backend.modules.authmodule.service;


import com.adpro.backend.modules.authmodule.model.Customer;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeedService {
    @Autowired
    private UserService<Customer> customerService;
    
    public void generateCustomers(int count) {
        Faker faker = new Faker();
        for (int i = 0; i < count; i++) {
            String username = faker.name().username();
            String password = faker.internet().password();
            String email = faker.internet().emailAddress();
            String name = faker.name().fullName();
            String cellPhoneNumber = faker.numerify("##########");
            Customer customer = new Customer(username, password, email, name, cellPhoneNumber);
            customerService.addUser(customer);
        }
    }
}

