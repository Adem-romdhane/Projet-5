package oc.safetyalerts.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonCityDTO {

    private String firstName;
    private String lastName;
    String city;
    String email;


}
