package com.adpro.backend.modules.authmodule.controller;
import com.adpro.backend.modules.authmodule.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeedController {

    @Autowired
    private SeedService seedService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @GetMapping("/seed/customers")
    public String seedCustomers() {
        int totalCustomers = 20000;
        int batchSize = 20;
        int totalBatches = totalCustomers / batchSize;

        for (int i = 0; i < totalBatches; i++) {
            threadPoolTaskExecutor.execute(() -> {
                seedService.generateCustomers(batchSize);
                System.out.println(threadPoolTaskExecutor.getActiveCount());
            });
        }

        return "Seed customers successfully.";
    }
}
