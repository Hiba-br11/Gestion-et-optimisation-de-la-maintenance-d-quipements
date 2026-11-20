package tn.enetcom.maintenance.controller;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record IndisponibiliteRequest(@NotNull(message = "La date est obligatoire") LocalDate date, String motif){
}
