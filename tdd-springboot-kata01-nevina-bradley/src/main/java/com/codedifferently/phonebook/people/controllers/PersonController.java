package com.codedifferently.phonebook.people.controllers;

import com.codedifferently.phonebook.people.exceptions.PersonException;
import com.codedifferently.phonebook.people.models.Person;
import com.codedifferently.phonebook.people.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @PostMapping("")
    public ResponseEntity<Person> createPersonRequest(@RequestBody Person person){
        Person savedPerson = personService.create(person);
        ResponseEntity response = new ResponseEntity(savedPerson, HttpStatus.CREATED);
        return response;
    }

    @GetMapping("")
    public ResponseEntity<List<Person>> getAllPeople(){
        List<Person> people = personService.getAllPeople();
        ResponseEntity<List<Person>> response = new ResponseEntity<>(people, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByLastName(@PathVariable String lastName){
        try {
            Person person = personService.findByLastName(lastName);
            ResponseEntity<?> response = new ResponseEntity<>(person, HttpStatus.OK);
            return response;
        } catch (PersonException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Integer id, @RequestBody Person person){
        try{
            Person updatedPerson = personService.updatePerson(id, person);
            ResponseEntity response = new ResponseEntity(updatedPerson,HttpStatus.OK);
            return response;
        } catch (PersonException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Integer id){
        try{
            personService.deletePerson(id);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (PersonException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
