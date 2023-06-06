package com.example.SushiTrainProblem;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

@RestController
@RequiredArgsConstructor
public class SushiTrainController {

  private final SushiTrainService sushiTrainService;

  @PostMapping("/calculate")
  public int calculateTrayCount(@RequestParam("file") MultipartFile file) {
    return sushiTrainService.calculateTrayCount(file);
  }
}
