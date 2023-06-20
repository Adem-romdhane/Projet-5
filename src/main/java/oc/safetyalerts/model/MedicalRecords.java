package oc.safetyalerts.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Data
@NoArgsConstructor // constructeur sans argument
@AllArgsConstructor // constructeur avec argument
@ToString // methode to string
public class MedicalRecords {


    private Long id;
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;



}
