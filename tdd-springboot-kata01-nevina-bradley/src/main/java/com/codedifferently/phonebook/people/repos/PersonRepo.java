package com.codedifferently.phonebook.people.repos;

import com.codedifferently.phonebook.people.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepo extends JpaRepository<Person, Integer>{
    List<Person> findByLastName(String lastName);
}
