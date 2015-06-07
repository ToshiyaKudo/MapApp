package com.example.kudotoshiya.mapapp;

/**
 * Created by kudo.toshiya on 2015/06/07.
 */
public class GoogleGeoCodeResponse {

    public String status;
    public results[] results;

    public GoogleGeoCodeResponse() {
    }

    public class results {
        public String formatted_address;
        public geometry geometry;
        public String[] types;
        public address_component[] address_components;
    }

    public class geometry {
        public bounds bounds;
        public String location_type;
        public location location;
        public bounds viewport;
    }

    public class bounds {

        public location northeast;
        public location southwest;
    }

    public class location {
        public Double lat;
        public Double lng;
    }

    public class address_component {
        public String long_name;
        public String short_name;
        public String[] types;
    }
}