package com.codedifferently.phonebook.people.controllers;

import com.codedifferently.phonebook.people.exceptions.PersonException;
import com.codedifferently.phonebook.people.models.Person;
import com.codedifferently.phonebook.people.models.phoneNumber;
import com.codedifferently.phonebook.people.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.hamcrest.core.Is;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)


public class PersonControllerTest {
    @MockBean
    private PersonService mockPersonService;

    @Autowired
    private MockMvc mockMvc;

    private Person inputPerson;
    private Person mockResponsePerson1;
    private Person mockResponsePerson2;

    @BeforeEach
    public void setUp(){
        List<phoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new phoneNumber("Part 1"));
        phoneNumbers.add(new phoneNumber("Part 2"));
        inputPerson = new Person("Test Widget", phoneNumbers);

        mockResponsePerson1 = new Person("Test Widget 01", phoneNumbers);
        mockResponsePerson1.setId(1);

        mockResponsePerson2 = new Person("Test Widget 02", phoneNumbers);
        mockResponsePerson2.setId(2);
    }

    @Test
    @DisplayName("Person Post: /people - success")
    public void createPersonRequestSuccess() throws Exception {
        BDDMockito.doReturn(mockResponsePerson1).when(mockPersonService).create(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputPerson)))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Test Widget 01")));
    }

    @Test
    @DisplayName("GET /people/1 - Found")
    public void getPersonByIdTestSuccess() throws Exception{
        BDDMockito.doReturn(mockResponsePerson1).when(mockPersonService).getPersonById(1);

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Widget 01")));
    }

    @Test
    @DisplayName("GET /people/1 - Not Found")
    public void getPersonByIdTestFail() throws Exception {
        BDDMockito.doThrow(new PersonException("Not Found")).when(mockPersonService).getPersonById(1);
        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /people/1 - Success")
    public void putPersonTestNotSuccess() throws Exception{
        List<phoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new phoneNumber("Part 1"));
        phoneNumbers.add(new phoneNumber("Part 2"));
        Person expectedPersonUpdate = new Person("After Update Person", phoneNumbers);
        expectedPersonUpdate.setId(1);
        BDDMockito.doReturn(expectedPersonUpdate).when(mockPersonService).updatePerson(any(), any());
        mockMvc.perform(put("/people/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputPerson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("After Update Person")));
    }

    @Test
    @DisplayName("PUT /people/1 - Not Found")
    public void putPersonTestNotFound() throws Exception{
        BDDMockito.doThrow(new PersonException("Not Found")).when(mockPersonService).updatePerson(any(), any());
        mockMvc.perform(put("/people/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputPerson)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /people/1 - Success")
    public void deletePersonTestNotSuccess() throws Exception{
        BDDMockito.doReturn(true).when(mockPersonService).deletePerson(any());
        mockMvc.perform(delete("/people/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /people/1 - Not Found")
    public void deletePersonTestNotFound() throws Exception{
        BDDMockito.doThrow(new PersonException("Not Found")).when(mockPersonService).deletePerson(any());
        mockMvc.perform(delete("/people/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
