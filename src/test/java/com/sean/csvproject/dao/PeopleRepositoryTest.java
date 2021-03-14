package com.sean.csvproject.dao;

import com.sean.csvproject.entity.PeopleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PeopleRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    PeopleRepository peopleRepository;

    @Test
    public void whenFindByIdReturnPerson() {
        PeopleEntity person = new PeopleEntity(1, "Sihyun", "Park", "test@email.com");
        testEntityManager.persist(person);
        testEntityManager.flush();

        PeopleEntity foundRecord = peopleRepository.getOne(person.getId());

        assertEquals(foundRecord.getId(), person.getId());
        assertEquals(foundRecord.getFirstname(), person.getFirstname());
        assertEquals(foundRecord.getLastname(), person.getLastname());
        assertEquals(foundRecord.getEmail(), person.getEmail());
    }
}
