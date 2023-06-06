package com.example.SushiTrainProblem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:3000")
public class SushiTrainProblemApplication {

  public static void main(String[] args) {
    SpringApplication.run(SushiTrainProblemApplication.class, args);
  }
}
