package com.patika.paycore.enums;

import java.util.stream.Stream;

public enum ApplicationStatus {
    REJECTED(0),
    CONFIRMED(1);


    private final int status;

    ApplicationStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static ApplicationStatus getByStatus(int status) {
        return Stream.of(ApplicationStatus.values())
                .filter(applicationStatus -> applicationStatus.getStatus() == status)
                .findFirst()
                .orElse(REJECTED);

    }
}
