package com.example.locationregistry;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;




@RestController
public class TestController {

     private final LocationService locationService;
    private final SupabaseStorageService supabaseStorageService;

    public TestController(
            LocationService locationService,
            SupabaseStorageService supabaseStorageService
    ) {
        this.locationService = locationService;
        this.supabaseStorageService = supabaseStorageService;
    }

    @GetMapping("/test")
    public String test() {
        return "It works";
    }

    @PostMapping("/save-location")
    public VisitResult saveLocationPost(@RequestBody LocationVisit visit) {
        return locationService.saveVisit(visit);
    }

    @GetMapping("/all-visits")
    public List<LocationVisit> getAllVisits() {
        return locationService.getAllVisits();
    }

   @PostMapping("/visit")
    public VisitResult saveVisitWithPhoto(
        @RequestParam Double lat,
        @RequestParam Double lon,
        @RequestParam("file") MultipartFile file
        ) {
            return locationService.saveVisitWithPhoto(lat, lon, file);
        }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        System.out.println(">>> Upload method hit");
        System.out.println(">>> File name: " + file.getOriginalFilename());
        System.out.println(">>> File size: " + file.getSize());

        try {
            return supabaseStorageService.upload(file);
        }catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
}





