package oc.safetyalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import oc.safetyalerts.model.FireStations;
import oc.safetyalerts.model.MedicalRecords;
import oc.safetyalerts.model.Person;
import oc.safetyalerts.service.dto.*;
import oc.safetyalerts.service.mapper.ChildAlertMapper;
import oc.safetyalerts.service.mapper.PersonFireAddressMapper;
import oc.safetyalerts.service.mapper.PersonInfoMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonRepository implements IPersonRepository {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonData jsonData;

    private final FireStationsRepository fireStationsRepository;
    private final PersonFireAddressMapper mapper2;

    private final ChildAlertMapper childAlertMapper;
    private final PersonInfoMapper mapper;

    List<Person> personList = new ArrayList<>();


    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        return null;
    }


    @Override
    public List<Person> findAll() {
        return jsonData.getPersons();
    }

    @Override
    public Person save(Person person) {
        personList.add(person);
        return person;
    }


    @Override
    public void delete(Person person) {
        personList.remove(person);
    }

    @Override
    public List<Person> findByStationNumber(int stationNumber) {

        List<Person> personByStationNumber = new ArrayList<>();
        List<FireStations> fireStations = jsonData.getFirestations();
        List<Person> people = jsonData.getPersons();
        List<FireStations> stations = fireStations.stream()
                .filter(station -> station.getStation() == stationNumber)
                .collect(Collectors.toList());

        for (Person person : people) {
            for (FireStations fireStations1 : stations) {
                if (person.getAddress().equals(fireStations1.getAddress())) {
                    personByStationNumber.add(person);
                }
            }
        }
        return personByStationNumber;
    }

    @Override
    public List<String> findPhoneByStationNumber(int stationNumber) {
        List<String> phoneNumbers = new ArrayList<>();
        List<FireStations> fireStations = jsonData.getFirestations();
        List<Person> persons = jsonData.getPersons();

        List<FireStations> stations = fireStations.stream()
                .filter(station -> station.getStation() == stationNumber)
                .collect(Collectors.toList());
        for (Person person : persons) {
            for (FireStations fireStation : stations) {
                if (person.getAddress().equals(fireStation.getAddress())) {
                    phoneNumbers.add(person.getPhone());
                }
            }
        }
        return phoneNumbers;
    }



    public List<ChildAlertDTO> getChildAlert(String address) {
        List<ChildAlertDTO> childAlertDTOList = new ArrayList<>();
        List<Person> persons = jsonData.getPersons();
        List<MedicalRecords> medicalRecords = jsonData.getMedicalRecords();

        List<Person> peopleFinded = persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .collect(Collectors.toList());

        for (Person person : peopleFinded) {
            for (MedicalRecords medicalRecord : medicalRecords) {
                if (person.getFirstName().equals(medicalRecord.getFirstName())
                        && person.getLastName().equals(medicalRecord.getLastName())) {
                    ChildAlertDTO childAlertDTO = childAlertMapper.toDto(medicalRecord);
                    List<MedicalRecords> medicalRecordsList = medicalRecords.stream().filter(mr ->
                                    person.getLastName().equals(mr.getLastName()))
                            .collect(Collectors.toList());
                    childAlertDTO.setHouseMembers(medicalRecordsList);
                    if (childAlertDTO.getAge() <= 18) {
                        childAlertDTOList.add(childAlertDTO);
                    }

                }
            }
        }

        return childAlertDTOList;
    }


    private List<String> getHouseholdMembers(Person child, List<Person> persons) {
        List<String> householdMembers = new ArrayList<>();
        persons.stream()
                .filter(person -> !person.equals(child) && person.getAddress().equals(child.getAddress()))
                .forEach(person -> householdMembers.add(person.getLastName()));
        return householdMembers;
    }


    @Override
    public List<PersonInfoDTO> findPersonInfoByFirstAndLastName(String firstName, String lastName) {
        List<Person> people = jsonData.getPersons();
        List<MedicalRecords> medicalRecords = jsonData.getMedicalRecords();
        List<PersonInfoDTO> personInfoDTOs = new ArrayList<>();

        for (Person person : people) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                for (MedicalRecords medicalRecord : medicalRecords) {
                    if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                        PersonInfoDTO personInfoDTO = mapper.toDto(person, medicalRecord);
                        personInfoDTOs.add(personInfoDTO);
                    }
                }
            }
        }

        return personInfoDTOs;
    }

    @Override
    public List<PersonFireAddressDTO> getPeopleByAddress(String address) {
        List<Person> people = jsonData.getPersons();
        List<FireStations> fireStations = jsonData.getFirestations();
        List<MedicalRecords> medicalRecords = jsonData.getMedicalRecords();
        List<PersonFireAddressDTO> personFireAddressDTOS = new ArrayList<>();

        for (Person person : people) {
            if (person.getAddress().equals(address)) {
                for (FireStations fireStations1 : fireStations) {
                    if (fireStations1.getAddress().equals(address)) {
                        for (MedicalRecords medicalRecord : medicalRecords) {
                            if (medicalRecord.getFirstName().equals(person.getFirstName()) && medicalRecord.getLastName().equals(person.getLastName())) {
                                PersonFireAddressDTO personFireAddressDTO = mapper2.toDto(person, medicalRecord, fireStations1);
                                personFireAddressDTOS.add(personFireAddressDTO);
                                break; // Sortir de la boucle si le dossier médical est trouvé
                            }
                        }
                    }
                }
            }
        }
        return personFireAddressDTOS;
    }


    public List<String> getEmailsByCity(String city) {
        List<String> emails = new ArrayList<>();
        List<Person> persons = jsonData.getPersons();

        for (Person person : persons) {
            if (person.getCity().equalsIgnoreCase(city)) {
                emails.add(person.getEmail());
            }
        }

        return emails;
    }


    @Override
    public List<FloodDTO> getFloodStations(List<Integer> stationNumbers) {
        List<FloodDTO> floodDTOs = new ArrayList<>();
        System.out.println(" ligne 293" + stationNumbers.get(0));
        for (int stationNumber : stationNumbers) {
            System.out.println(" ligne 293" + stationNumber);
            List<FireStations> fireStations = fireStationsRepository.findByStationNumber(stationNumber);

            for (FireStations fireStation : fireStations) {
                String address = fireStation.getAddress();
                List<MedicalRecords> medicalrecords = jsonData.getMedicalRecords();
                List<Person> residents = jsonData.getPersons();
                for (Person resident : residents) {
                    if (address.equals(resident.getAddress()))
                        for (MedicalRecords medicalRecords1 : medicalrecords) {
                            FloodDTO dto = new FloodDTO();
                            dto.setAddress(address);
                            PersonInfoMedicalDTO personInfoMedicalDTO = new PersonInfoMedicalDTO();
                            personInfoMedicalDTO.setMedicalRecords(medicalRecords1);
                            personInfoMedicalDTO.setPhone(resident.getPhone());
                            dto.setPersonInfoMedicalDTOS(List.of(personInfoMedicalDTO));
                            floodDTOs.add(dto);
                        }
                }
            }
        }
        return floodDTOs;
    }
}


