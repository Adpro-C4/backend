package com.adpro.backend.modules.authmodule.controller;


import com.adpro.backend.modules.authmodule.model.Customer;
import com.adpro.backend.modules.authmodule.service.UserService;
import com.adpro.backend.modules.commonmodule.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private UserService<Customer> customerService;
    @GetMapping("/customer/{id}")
    public ResponseEntity<Object> getCustomer(@PathVariable("id") String id) {
        Customer customer = customerService.getUserById(Long.valueOf(id));
        if(customer == null){
            return ResponseHandler.generateResponse("user tidak ditemukan!", HttpStatus.NOT_FOUND,
                    new HashMap<String, Object>());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("user_detail", customer );
        data.put("message", "Berhasil mengambil data user");
        return ResponseHandler.generateResponse(id, HttpStatus.ACCEPTED, data);
    }
}
