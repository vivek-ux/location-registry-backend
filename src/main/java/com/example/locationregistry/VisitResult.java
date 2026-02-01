package com.example.locationregistry;

import java.time.LocalDateTime;

public class VisitResult {

    private boolean revisited;
    private String message;
    private LocalDateTime checkedAt;

    public VisitResult(boolean revisited, String message) {
        this.revisited = revisited;
        this.message = message;
        this.checkedAt = LocalDateTime.now();
    }

    public boolean isRevisited() {
        return revisited;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }
}
