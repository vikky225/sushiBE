package com.example.SushiTrainProblem;

import java.io.*;
import java.time.*;
import java.util.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

@Service
@RequiredArgsConstructor
public class SushiTrainService {

  public int calculateTrayCount(MultipartFile file) {
    // Read the contents of the uploaded file
    List<String> lines = readFileContent(file);

    // Parse the lines and calculate the tray count
    int trayCount = 0;
    int shelfLife = getShelfLife();

    for (String line : lines) {
      String[] parts = line.split(",");
      if (parts.length >= 2) {
        String dateTime = parts[0] + " " + parts[1];
        int scannedIn = Integer.parseInt(parts[2]);
        int scannedOut = Integer.parseInt(parts[3]);

        // Apply adjustment 1
        if (shelfLife > 0 && minutesPassed(dateTime) >= shelfLife) {
          int previousScannedOut = getScannedOut(lines, dateTime);
          if (scannedIn - previousScannedOut < 0) {
            int adjustment = Math.abs(scannedIn - previousScannedOut);
            trayCount += adjustment;
          }
        }

        trayCount += scannedIn - scannedOut;
      } else {
        throw new RuntimeException("Invalid line format: " + line);
      }
    }

    return trayCount;
  }

  private List<String> readFileContent(MultipartFile file) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read the file: " + e.getMessage());
    }
    return lines;
  }

  private int minutesPassed(String dateTimeString) {
    LocalTime currentTime = LocalTime.now();
    LocalTime dateTime = LocalTime.parse(dateTimeString.split(" ")[1]);
    int minutes =
        (currentTime.getHour() - dateTime.getHour()) * 60
            + (currentTime.getMinute() - dateTime.getMinute());
    return Math.max(minutes, 0);
  }

  private int getScannedOut(List<String> lines, String dateTime) {
    int scannedOut = 0;
    for (String line : lines) {
      String[] parts = line.split(",");
      String lineDateTime = parts[0] + " " + parts[1];
      int scannedOutValue = Integer.parseInt(parts[3]);
      if (lineDateTime.equals(dateTime)) {
        scannedOut = scannedOutValue;
      } else if (minutesPassed(lineDateTime) > minutesPassed(dateTime)) {
        break;
      }
    }
    return scannedOut;
  }

  private int getShelfLife() {
    LocalTime currentTime = LocalTime.now();
    if (currentTime.isAfter(LocalTime.of(16, 0))) {
      return 90; // 1.5 hours
    } else {
      return 180; // 3 hours
    }
  }
}
