package com.codedifferently.phonebook.people.services;

import com.codedifferently.phonebook.people.exceptions.PersonException;
import com.codedifferently.phonebook.people.models.Person;
import com.codedifferently.phonebook.people.models.phoneNumber;
import com.codedifferently.phonebook.people.repos.PersonRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)

public class PersonServiceImplTest {
    @MockBean
    private PersonRepo mockPersonRepo;

    @Autowired
    private PersonService personService;

    private Person inputPerson;

    private Person mockResponsePerson1;

    private Person mockResponsePerson2;

    @BeforeEach
    public void setUp() {
        List<phoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new phoneNumber("(302) 123-4567"));
        phoneNumbers.add(new phoneNumber("(302) 765-4321"));
        inputPerson = new Person("Test Person", phoneNumbers);

        mockResponsePerson1 = new Person("Test Person 01", phoneNumbers);
        mockResponsePerson1.setId(1);

        mockResponsePerson2 = new Person("Test Person 02", phoneNumbers);
        mockResponsePerson2.setId(2);
    }

    @Test
    @DisplayName("Person Service: Create Person - Success")
    public void createPersonTestSuccess(){
        BDDMockito.doReturn(mockResponsePerson1).when(mockPersonRepo).save(ArgumentMatchers.any());
        Person returnedPerson = personService.create(inputPerson);
        Assertions.assertNotNull(returnedPerson, "Person should not be null");
        Assertions.assertEquals(returnedPerson.getId(),1 );
    }

    @Test
    @DisplayName("Person Service: Get Person by Id - Success")
    public void getPersonByIdTestSuccess() throws PersonException {
        BDDMockito.doReturn(Optional.of(mockResponsePerson1)).when(mockPersonRepo).findById(1);
        Person foundPerson = personService.getPersonById(1);
        Assertions.assertEquals(mockResponsePerson1.toString(), foundPerson.toString());
    }

    @Test
    @DisplayName("Person Service: Get Person by Id - Fail")
    public void getPersonByIdTestFailed() {
        BDDMockito.doReturn(Optional.empty()).when(mockPersonRepo).findById(1);
        Assertions.assertThrows(PersonException.class, () -> {
            personService.getPersonById(1);
        });
    }

    @Test
    @DisplayName("Person Service: Get All People - Success")
    public void getAllPeopleTestSuccess() {
        List<Person> people = new ArrayList<>();
        people.add(mockResponsePerson1);
        people.add(mockResponsePerson2);

        BDDMockito.doReturn(people).when(mockPersonRepo).findAll();

        List<Person> responsePeople = personService.getAllPeople();
        Assertions.assertIterableEquals(people, responsePeople);
    }

    @Test
    @DisplayName("Person Service: Update Person - Success")
    public void updatePersonTestSuccess() throws PersonException {
        List<phoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new phoneNumber("(302) 123-4567"));
        phoneNumbers.add(new phoneNumber("(302) 765-4321"));
        Person expectedPersonUpdate = new Person("After Update Person", phoneNumbers);

        BDDMockito.doReturn(Optional.of(mockResponsePerson1)).when(mockPersonRepo).findById(1);
        BDDMockito.doReturn(expectedPersonUpdate).when(mockPersonRepo).save(ArgumentMatchers.any());

        Person actualPerson = personService.updatePerson(1, expectedPersonUpdate);
        Assertions.assertEquals(expectedPersonUpdate.toString(), actualPerson.toString());
    }

    @Test
    @DisplayName("Person Service: Update Person - Fail")
    public void updatePersonTestFail()  {
        List<phoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new phoneNumber("(302) 123-4567"));
        phoneNumbers.add(new phoneNumber("(302) 765-4321"));
        Person expectedPersonUpdate = new Person("After Update Person", phoneNumbers);
        BDDMockito.doReturn(Optional.empty()).when(mockPersonRepo).findById(1);
        Assertions.assertThrows(PersonException.class, ()-> {
            personService.updatePerson(1, expectedPersonUpdate);
        });
    }

    @Test
    @DisplayName("Person Service: Delete Person - Success")
    public void deletePersonTestSuccess() throws PersonException {
        BDDMockito.doReturn(Optional.of(mockResponsePerson1)).when(mockPersonRepo).findById(1);
        Boolean actualResponse = personService.deletePerson(1);
        Assertions.assertTrue(actualResponse);
    }

    @Test
    @DisplayName("Person Service: Delete Person - Fail")
    public void deletePersonTestFail()  {
        BDDMockito.doReturn(Optional.empty()).when(mockPersonRepo).findById(1);
        Assertions.assertThrows(PersonException.class, ()-> {
            personService.deletePerson(1);
        });
    }
}
