package com.example.locationregistry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {

    private final LocationService locationService;

    public TestController(LocationService locationService) {
        this.locationService = locationService;
    }

    // ✅ Health check
    @GetMapping("/test")
    public String test() {
        return "Backend is working ✅";
    }

    /**
     * 📍 Save visit with photo
     * Multipart form-data:
     * - lat
     * - lon
     * - file
     */
    @PostMapping(value = "/visit", consumes = "multipart/form-data")
    public VisitResult saveVisitWithPhoto(
            @RequestParam("lat") Double lat,
            @RequestParam("lon") Double lon,
            @RequestParam("file") MultipartFile file
    ) {
        return locationService.saveVisitWithPhoto(lat, lon, file);
    }
}
