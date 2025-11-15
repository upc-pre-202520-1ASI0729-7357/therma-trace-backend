package com.thermatrace.thermatracebackend.medicines.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ImageUrl {
    private final String url;

    public ImageUrl() {
        this.url = "";
    }

    public ImageUrl(String url) {
        if (url != null && !url.trim().isEmpty()) {
            if (!isValidUrl(url)) {
                throw new IllegalArgumentException("Invalid URL format");
            }
        }
        this.url = url == null ? "" : url.trim();
    }

    private boolean isValidUrl(String url) {
        try {
            java.net.URI.create(url).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return url;
    }
}
