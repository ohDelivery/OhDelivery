package com.ohdelivery.service.delivery.infrastructure.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class DirectionsResponse {


    private int code;
    private String currentDateTime;
    private String message;
    private Route route;

    public int getDistance() {
        return this.getRoute().getTraoptimal().get(0).getSummary().getDistance();
    }

    public int getDuration() {
        return this.getRoute().getTraoptimal().get(0).getSummary().getDuration() / 1000;
    }

    // getters and setters

    @Getter
    public static class Route {

        private List<Traoptimal> traoptimal;

        // getters and setters

        @Getter
        public static class Traoptimal {

            private List<Guide> guide;
            private List<List<Double>> path;
            private List<Section> section;
            private Summary summary;

            // getters and setters

            public static class Guide {

                private int distance;
                private int duration;
                private String instructions;
                private int pointIndex;
                private int type;

                // getters and setters
            }

            public static class Section {

                private int congestion;
                private int distance;
                private String name;
                private int pointCount;
                private int pointIndex;
                private int speed;

                // getters and setters
            }

            @Getter
            public static class Summary {

                private List<List<Double>> bbox;
                private String departureTime;
                private int distance;
                private int duration;
                private int fuelPrice;
                private Goal goal;
                private Start start;
                private int taxiFare;
                private int tollFare;

                // getters and setters

                public static class Goal {

                    private int dir;
                    private List<Double> location;

                    // getters and setters
                }

                public static class Start {

                    private List<Double> location;

                    // getters and setters
                }
            }
        }
    }
}
