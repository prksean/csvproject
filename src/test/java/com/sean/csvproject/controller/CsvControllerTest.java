package com.sean.csvproject.controller;

import com.sean.csvproject.model.ResponseMessage;
import com.sean.csvproject.service.CsvService;
import com.sean.csvproject.util.CsvUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalToObject;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CsvControllerTest {
    @MockBean
    CsvService csvService;

    @Autowired
    private MockMvc mockMvc;

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
    public void csvUploaderTestOk() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("file", "test.csv", "text/csv", "1,firstname,lastname,email@test.com".getBytes());

        when(CsvUtil.isCsvType(csvFile)).thenReturn(true);
        doReturn(new ResponseMessage()).when(csvService).persistPeople(csvFile);

        mockMvc.perform(multipart("/csv/upload").file(csvFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalToObject("File uploaded successfully: " + csvFile.getOriginalFilename())));
    }


    @Test
    public void csvUploaderTestBad() throws Exception {
        MockMultipartFile txtFile = new MockMultipartFile("file", "test.txt", "text/txt", "test text".getBytes());

        when(CsvUtil.isCsvType(txtFile)).thenReturn(false);

        mockMvc.perform(multipart("/csv/upload").file(txtFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalToObject("Uploaded file is not CSV: " + txtFile.getOriginalFilename())));
    }

    @Test
    public void csvUploaderTestException() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("file", "test.csv", "text/csv", "1,firstname,lastname,email@test.com".getBytes());

        when(CsvUtil.isCsvType(csvFile)).thenReturn(true);

        doThrow(new Exception()).when(csvService).persistPeople(csvFile);

        mockMvc.perform(multipart("/csv/upload").file(csvFile))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.message", equalToObject("File upload failed: " + csvFile.getOriginalFilename())));
    }
}
