package org.ticketBooking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticketBooking.entities.Train;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {
    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();

    // ✅ Points to actual file system path, not classpath resource
    private static final String TRAIN_DB_PATH = "app/src/main/resources/localDB/trains.json";

    public TrainService() throws IOException {
        File file = new File(TRAIN_DB_PATH);
        if (!file.exists()) throw new FileNotFoundException("trains.json not found at " + TRAIN_DB_PATH);
        trainList = objectMapper.readValue(file, new TypeReference<List<Train>>() {});
        System.out.println("✅ Trains loaded: " + trainList.size());
        System.out.println("➡️ First train seats: " + trainList.get(0).getSeats());
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    public void addTrain(Train newTrain) {
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            updateTrain(newTrain);
        } else {
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            addTrain(updatedTrain);
        }
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TRAIN_DB_PATH), trainList);
            System.out.println("✅ Trains saved to file.");
        } catch (IOException e) {
            System.out.println("❌ Failed to save trains.json");
            e.printStackTrace();
        }
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        int srcIdx = stations.indexOf(source.toLowerCase());
        int dstIdx = stations.indexOf(destination.toLowerCase());
        return srcIdx != -1 && dstIdx != -1 && srcIdx < dstIdx;
    }

    public List<Train> getAllTrains() {
        return trainList;
    }
}
