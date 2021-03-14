package com.sean.csvproject.service;

import com.opencsv.exceptions.CsvValidationException;
import com.sean.csvproject.entity.PeopleEntity;
import com.sean.csvproject.model.ResponseMessage;
import com.sean.csvproject.util.CsvUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CsvServiceTest {
    @MockBean
    CsvService csvService;

    private static MockedStatic<CsvUtil> csvUtil;

    @BeforeClass
    public static void beforeClass() {
        csvUtil = Mockito.mockStatic(CsvUtil.class);
    }
    @AfterClass
    public static void afterClass() {
        csvUtil.close();
    }

    @Test
    public void persistPeopleTest() throws IOException, CsvValidationException {
        List<PeopleEntity> inputPeopleList = new ArrayList<>();
        PeopleEntity mockInput = new PeopleEntity();
        mockInput.setId(1);
        mockInput.setFirstname("firstname");
        mockInput.setLastname("lastname");
        mockInput.setEmail("email@test.com");
        inputPeopleList.add(mockInput);

        MockMultipartFile csvFile = new MockMultipartFile("file", "test.csv", "text/csv", "1,firstname,lastname,email@test.com".getBytes());

        when(CsvUtil.parseCsvToPeople(csvFile)).thenReturn(inputPeopleList);

        doReturn(new ResponseMessage()).when(csvService).persistPeopleData(inputPeopleList);
    }
}
