
package com.example.locationregistry;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;


   @Service
    public class LocationService {

    private final LocationVisitRepository repository;
    private final SupabaseStorageService supabaseStorageService;

    public LocationService(LocationVisitRepository repository,
                            SupabaseStorageService supabaseStorageService
    ) {
        this.repository = repository;
        this.supabaseStorageService = supabaseStorageService;
    }

    private double distanceInMeters(
        double lat1, double lon1,
        double lat2, double lon2
    ) {
    final int R = 6371000; // Earth radius in meters

    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
}


    public VisitResult saveVisit(LocationVisit visit) {

        List<LocationVisit> allVisits = repository.findAll();

        for (LocationVisit oldVisit : allVisits) {
            double distance = distanceInMeters(
                visit.getLatitude(),
                visit.getLongitude(),
                oldVisit.getLatitude(),
                oldVisit.getLongitude()
            );

            if (distance <= 50) {
                repository.save(visit);
                return new VisitResult(
                true,
                "You were already here!"
            );
            }
        }

        repository.save(visit);
        return new VisitResult(
        false,
        "New place saved!"
    );
    }

    public List<LocationVisit> getAllVisits() {
        return repository.findAll();
    }

    @Transactional
    public VisitResult saveVisitWithPhoto(
        Double lat,
        Double lon,
        MultipartFile file
    ) {try{
        String photoUrl = supabaseStorageService.upload(file);

        LocationVisit visit = new LocationVisit();
        visit.setLatitude(lat);
        visit.setLongitude(lon);
        visit.setPhotoUrl(photoUrl);

        return saveVisit(visit);
    }catch(Exception e){
        throw new RuntimeException(e);
    }
        
    }

}