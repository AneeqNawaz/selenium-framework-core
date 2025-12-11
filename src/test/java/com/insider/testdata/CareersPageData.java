package com.insider.testdata;

import java.util.List;

public class CareersPageData {

    private LifeAtInsiderData lifeAtInsider;
    private TeamsSectionData teams;
    private LocationsSectionData locations;


    public LifeAtInsiderData getLifeAtInsider() {
        return lifeAtInsider;
    }

    public TeamsSectionData getTeams() {
        return teams;
    }

    public LocationsSectionData getLocations() {
        return locations;
    }

    // nested DTOs

    public static class LifeAtInsiderData {
        private String title;
        private String subtitle;
        private int expectedImageCount;

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public int getExpectedImageCount() {
            return expectedImageCount;
        }
    }

    public static class TeamsSectionData {
        private String title;
        private String subtitle;
        private List<TeamCardData> teams;

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public List<TeamCardData> getTeams() {
            return teams;
        }
    }

    public static class TeamCardData {
        private String name;
        private String description;
        private String openPositionsText;
        private String url;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getOpenPositionsText() {
            return openPositionsText;
        }

        public String getUrl() {
            return url;
        }
    }

    // --------- NEW Locations DTOs ---------

    public static class LocationsSectionData {
        private String title;
        private String subtitle;
        private int expectedLocationsCount;
        private List<LocationData> locationsList;

        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public int getExpectedLocationsCount() { return expectedLocationsCount; }
        public List<LocationData> getLocationsList() { return locationsList; }
    }
    public static class LocationData {
        private String name;
        private String address;
        private String email;
        private String mapsUrl;

        public String getName() { return name; }
        public String getAddress() { return address; }
        public String getEmail() { return email; }
        public String getMapsUrl() { return mapsUrl; }
    }

}
