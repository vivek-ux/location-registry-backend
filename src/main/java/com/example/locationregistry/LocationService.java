package com.example.locationregistry;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocationService {

    private final LocationRepository locationRepo;
    private final VisitRepository visitRepo;
    private final SupabaseStorageService storage;

    public LocationService(
            LocationRepository locationRepo,
            VisitRepository visitRepo,
            SupabaseStorageService storage
    ) {
        this.locationRepo = locationRepo;
        this.visitRepo = visitRepo;
        this.storage = storage;
    }

    // 🌍 Haversine distance in meters
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return R * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }

    /**
     * 📍 Save visit with photo
     * - Detects revisit (≤50m)
     * - Always saves photo
     * - Groups photos under same location
     */
    @Transactional
    public VisitResult saveVisitWithPhoto(
            Double latitude,
            Double longitude,
            MultipartFile file
    ) {

        // 1️⃣ Find if location already exists
        List<Location> allLocations = locationRepo.findAll();
        Location matchedLocation = null;

        for (Location loc : allLocations) {
            double dist = distance(
                    latitude,
                    longitude,
                    loc.getLatitude(),
                    loc.getLongitude()
            );

            if (dist <= 50) {
                matchedLocation = loc;
                break;
            }
        }

        boolean revisited = matchedLocation != null;

        // 2️⃣ If new place → create Location
        if (!revisited) {
            matchedLocation = new Location();
            matchedLocation.setLatitude(latitude);
            matchedLocation.setLongitude(longitude);
            locationRepo.save(matchedLocation);
        }

        // 3️⃣ Upload photo
        try {
            String photoUrl = storage.upload(file);

        // 4️⃣ Save Visit (ALWAYS saved)
        Visit visit = new Visit();
        visit.setPhotoUrl(photoUrl);
        visit.setLocation(matchedLocation);
        visitRepo.save(visit);

        // 5️⃣ Return clean response
        return new VisitResult(
                revisited,
                revisited
                        ? "You were already here! Photo saved."
                        : "New place saved!"
        );
        } catch(Exception e) {
            throw new RuntimeException("Photo upload failed", e);
        }
}
}
