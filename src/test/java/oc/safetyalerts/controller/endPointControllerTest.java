package oc.safetyalerts.controller;

import oc.safetyalerts.model.FireStations;
import oc.safetyalerts.model.MedicalRecords;
import oc.safetyalerts.model.Person;
import oc.safetyalerts.model.PersonDTO;
import oc.safetyalerts.service.FireStationsService;
import oc.safetyalerts.service.MedicalRecordsService;
import oc.safetyalerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
class endPointControllerTest {


    @Mock
    private FireStationsService fireStationsService;

    @Mock
    private PersonService personService;

    @Mock
    private MedicalRecordsService medicalRecordsService;

    @InjectMocks
    private endPointController endPointController;

    @Test
    public void testGetPeopleByFireStation() {
        // Mocking data
        int stationNumber = 1;
        List<FireStations> fireStations = new ArrayList<>();
        FireStations fireStation = new FireStations();
        fireStation.setAddress("123 Main St");
        fireStations.add(fireStation);

        List<String> addresses = new ArrayList<>();
        addresses.add("123 Main St");

        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setPhone("123-456-7890");
        persons.add(person);

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("1990-01-01");

        // Mocking service methods
        when(personService.findByAddressIn(addresses)).thenReturn(persons);
        when(medicalRecordsService.findByFirstNameAndLastName("John", "Doe")).thenReturn(medicalRecord);

        // Call the method
        List<PersonDTO> result = endPointController.getPeopleByFireStation(stationNumber);

        // Assertion
        assertEquals(1, result.size());
        PersonDTO personDTO = result.get(0);
        assertEquals("John", personDTO.getFirstName());
        assertEquals("Doe", personDTO.getLastName());
        assertEquals("123 Main St", personDTO.getAddress());
        assertEquals("123-456-7890", personDTO.getPhone());
        assertFalse(personDTO.isAdult());

        // Verify service method invocations
        verify(fireStationsService, times(1)).findByStationNumber(stationNumber);
        verify(personService, times(1)).findByAddressIn(addresses);
        verify(medicalRecordsService, times(1)).findByFirstNameAndLastName("John", "Doe");
    }
}