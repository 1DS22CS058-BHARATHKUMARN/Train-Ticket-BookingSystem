//package org.ticketBooking.entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//import java.util.Map;
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Train {
//
//    @JsonProperty("train_id")
//    private String trainId;
//
//    @JsonProperty("train_no")
//    private String trainNo;
//
//    @JsonProperty("seats")
//    private List<List<Integer>> seats;
//
//    @JsonProperty("station_times")
//    private Map<String, String> stationTimes;
//
//    @JsonProperty("stations")
//    private List<String> stations;
//
//    public String getTrainId() {
//        return trainId;
//    }
//
//    public void setTrainId(String trainId) {
//        this.trainId = trainId;
//    }
//
//    public String getTrainNo() {
//        return trainNo;
//    }
//
//    public void setTrainNo(String trainNo) {
//        this.trainNo = trainNo;
//    }
//
//    public List<List<Integer>> getSeats() {
//        return seats;
//    }
//
//    public void setSeats(List<List<Integer>> seats) {
//        this.seats = seats;
//    }
//
//    public Map<String, String> getStationTimes() {
//        return stationTimes;
//    }
//
//    public void setStationTimes(Map<String, String> stationTimes) {
//        this.stationTimes = stationTimes;
//    }
//
//    public List<String> getStations() {
//        return stations;
//    }
//
//    public void setStations(List<String> stations) {
//        this.stations = stations;
//    }
//
//    public String getTrainInfo() {
//        return String.format("Train ID: %s Train No: %s", trainId, trainNo);
//    }
//}
// Train.java
package org.ticketBooking.entities;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Train {

    private String trainId;
    private String trainNo;

    @JsonProperty("seats")
    private List<List<Integer>> seats;
    private Map<String, String> stationTimes;
    private List<String> stations;

    public Train() {
    }

    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes, List<String> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public String getTrainInfo() {
        return String.format("Train ID: %s Train No: %s", trainId, trainNo);
    }
}
