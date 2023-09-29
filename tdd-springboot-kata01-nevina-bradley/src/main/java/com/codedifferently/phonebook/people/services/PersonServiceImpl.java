package com.codedifferently.phonebook.people.services;

import com.codedifferently.phonebook.people.exceptions.PersonException;
import com.codedifferently.phonebook.people.models.Person;
import com.codedifferently.phonebook.people.repos.PersonRepo;
import com.codedifferently.phonebook.widgets.exceptions.WidgetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService{
    private static Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private PersonRepo personRepo;

    @Autowired
    public PersonServiceImpl(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public Person create(Person person) {
        Person savedPerson = personRepo.save(person);
        return savedPerson;
    }

    @Override
    public Person getPersonById(Integer id) throws PersonException {
        Optional<Person> personOptional = personRepo.findById(id);
        if (personOptional.isEmpty()) {
            logger.error("Person with id {} does not exist", id);
            throw new PersonException("Person not found");
        }

        return personOptional.get();
    }

    @Override
    public List<Person> getPersonById(String lastName) {
        return (List) personRepo.findByLastName(lastName);
    }

    @Override
    public List<Person> getAllPeople() {
        return (List) personRepo.findAll();
    }

    @Override
    public Person updatePerson(Integer id, Person person) throws PersonException {
    Optional<Person> personOptional = personRepo.findById(id);
    if (personOptional.isEmpty()) {
        throw new PersonException("Person does not exist, cannot update");
    }

    Person savedPerson = personOptional.get();
    savedPerson.setPhoneNumbers(person.getPhoneNumbers());
    savedPerson.setFirstName(person.getFirstName());
    savedPerson.setLastName(person.getLastName());

    return personRepo.save(savedPerson);
    }

    @Override
    public Boolean deletePerson(Integer id) throws PersonException {
        Optional<Person> personOptional = personRepo.findById(id);
        if (personOptional.isEmpty()) {
            throw new PersonException("Person does not exist, cannot delete");
        }

        Person personToDelete = personOptional.get();
        personRepo.delete(personToDelete);
        return true;
    }
}
