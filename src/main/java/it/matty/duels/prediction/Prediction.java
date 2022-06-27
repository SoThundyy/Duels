package it.matty.duels.prediction;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Prediction {
    private final UUID whoBet;
    private final BigDecimal money;
}
