package model;

public class Train {
    private int trainId;
    private String trainName;
    private String source;
    private String destination;
    private String departureTime;
    private int totalSeats;
    private int availableSeats;

    // constructor, getters, setters

    public Train(int trainId, String trainName, String source, String destination,
                 String departureTime, int totalSeats, int availableSeats) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // Add Getters only (if setters not required)
    public int getTrainId() { return trainId; }
    public String getTrainName() { return trainName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void display() {
        System.out.println(trainId + " | " + trainName + " | " + source + " ➝ " + destination + " | " + departureTime + " | Available: " + availableSeats);
    }


}
