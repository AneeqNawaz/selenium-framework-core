package com.insider.testdata;

import java.util.List;

public class HomePageData {

    private String homeUrl;
    private String title;
    private Footer footer;

    public String getHomeUrl() {
        return homeUrl;
    }

    public String getTitle() {
        return title;
    }

    public Footer getFooter() {
        return footer;
    }

    public static class Footer {
        private List<String> companyLinks;
        private List<String> resources;

        public List<String> getCompanyLinks() {
            return companyLinks;
        }

        public List<String> getResources() {
            return resources;
        }
    }
}
