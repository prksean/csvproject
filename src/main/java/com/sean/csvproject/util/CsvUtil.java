package com.sean.csvproject.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sean.csvproject.entity.PeopleEntity;
import com.sean.csvproject.service.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    public static String FILE_TYPE = "text/csv";

    /**
     * **** Check if the file type is 'text/csv' *****
     *
     * @param file | file whose type to be checked
     * @return boolean | returns true if 'text/csv' else false
     * @author Sihyun Park
     */
    public static boolean isCsvType(MultipartFile file) {
        logger.info("CHECK if file type is text/csv............");
        return FILE_TYPE.equals(file.getContentType());
    }

    /**
     * **** Read csv file and parse as a list of PeopleEntity *****
     *
     * @param file | csv file to be parsed
     * @return List<PeopleEntity> | returns a list of PeopleEntity
     * @author Sihyun Park
     */
    public static List<PeopleEntity> parseCsvToPeople(MultipartFile file) throws IOException, CsvValidationException {
        logger.info("START parsing csv file to PeopleEntity list............");
        //try with resources.
        try (InputStream is = file.getInputStream();
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<PeopleEntity> peopleEntityList = new ArrayList<>();

            //Skip header in csv file.
            reader.readNext();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                PeopleEntity peopleEntity = new PeopleEntity();
                peopleEntity.setId(Long.parseLong(nextLine[0]));
                peopleEntity.setFirstname(nextLine[1]);
                peopleEntity.setLastname(nextLine[2]);
                peopleEntity.setEmail(nextLine[3]);

                peopleEntityList.add(peopleEntity);
            }
            logger.info("COMPLETED parsing csv file to PeopleEntity list............");
            return peopleEntityList;
        }
    }
}
