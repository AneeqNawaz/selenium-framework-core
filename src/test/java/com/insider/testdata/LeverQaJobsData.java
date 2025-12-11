package com.insider.testdata;

import java.util.List;

public class LeverQaJobsData {

    private String listingUrl;
    private String postingGroupTitle;
    private String departmentContains;
    private String workTypeContains;
    private int expectedInitialJobsCount;
    private List<LocationFlow> locationFlows;

    public String getListingUrl() {
        return listingUrl;
    }

    public String getPostingGroupTitle() {
        return postingGroupTitle;
    }

    public String getDepartmentContains() {
        return departmentContains;
    }

    public String getWorkTypeContains() {
        return workTypeContains;
    }

    public int getExpectedInitialJobsCount() {
        return expectedInitialJobsCount;
    }

    public List<LocationFlow> getLocationFlows() {
        return locationFlows;
    }

    public static class LocationFlow {
        private String name;
        private String locationFilterText;
        private String locationShouldContain;

        public String getName() {
            return name;
        }

        public String getLocationFilterText() {
            return locationFilterText;
        }

        public String getLocationShouldContain() {
            return locationShouldContain;
        }
    }
}
