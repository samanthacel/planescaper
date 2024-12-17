package com.example.planescaper.unsplash;

public class UnsplashPhoto {
    private String id;
    private UnsplashUrls urls;

    public static class UnsplashUrls {
        private String regular;

        public String getRegular() {
            return regular;
        }
    }

    public String getId() {
        return id;
    }

    public UnsplashUrls getUrls() {
        return urls;
    }
}
