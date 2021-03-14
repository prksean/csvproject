package com.sean.csvproject.service;

import com.sean.csvproject.dao.PeopleRepository;
import com.sean.csvproject.entity.PeopleEntity;
import com.sean.csvproject.model.ResponseMessage;
import com.sean.csvproject.util.CsvUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CsvService {

    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    PeopleRepository peopleRepository;

    @Autowired
    public CsvService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    /**
     * **** Parse CSV to people entity list and persist to DB ****
     *
     * @param file | file to be parsed and inserted to DB
     * @return ResponseMessage | returns number of records and time it took to persist data
     * @throws Exception | data insert to db fail
     * @author Sihyun Park
     */
    public ResponseMessage persistPeople(MultipartFile file) throws Exception {
        try {
            List<PeopleEntity> peopleEntityList = CsvUtil.parseCsvToPeople(file);

            return persistPeopleData(peopleEntityList);
        } catch (Exception e) {
            logger.warn("CSV data insert to DB FAILED: " + e.getMessage());
            throw new Exception("CSV data insert to DB FAILED: " + e.getMessage());
        }
    }

    /**
     * **** Save PeopleEntity list to DB using JPA repository ****
     *
     * @param peopleList | list of PeopleEntity
     * @return ResponseMessage | returns number of records and time it took to persist data
     * @author Sihyun Park
     */
    public ResponseMessage persistPeopleData(List<PeopleEntity> peopleList) {
        ResponseMessage rm = new ResponseMessage();
        int processCnt = peopleList.size();
        logger.info("START DB INSERT for uploaded file............");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < processCnt; i += batchSize) {
            if (i + batchSize > processCnt) {
                List<PeopleEntity> peopleEntitySubList = peopleList.subList(i, processCnt);
                peopleRepository.saveAll(peopleEntitySubList);
                break;
            }
            List<PeopleEntity> peopleEntitySubList = peopleList.subList(i, i + batchSize);
            peopleRepository.saveAll(peopleEntitySubList);
        }
        stopWatch.stop();

        logger.info("COMPLETED DB INSERT for uploaded file............");
        logger.info("DB INSERT process time for batch size {} : {} s", batchSize, (double) stopWatch.getNanoTime() / 1000000000.0);

        rm.setCnt(processCnt);
        rm.setTime((double) stopWatch.getNanoTime() / 1000000000.0);
        return rm;
    }
}
