package com.codedifferently.phonebook.people.services;

import com.codedifferently.phonebook.people.exceptions.PersonException;
import com.codedifferently.phonebook.people.models.Person;

import java.util.List;

public interface PersonService {
    Person create(Person person);
    Person getPersonById(Integer id) throws PersonException;
    List<Person> findByLastName(String lastName) throws PersonException;
    List<Person> getAllPeople();
    Person updatePerson(Integer id, Person person) throws PersonException;
    Boolean deletePerson(Integer id) throws PersonException;
}
