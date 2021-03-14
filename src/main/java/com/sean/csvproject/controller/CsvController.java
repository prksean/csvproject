package com.sean.csvproject.controller;

import com.sean.csvproject.model.ResponseMessage;
import com.sean.csvproject.service.CsvService;
import com.sean.csvproject.util.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@ControllerAdvice
@RestController
@RequestMapping("/csv")
public class CsvController {

    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    private final CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    /**
     * **** If the uploaded file is csv, saves records to a database table ****
     *
     * @param file | the file received through http request
     * @return ResponseEntity<ResponseMessage> | returns result message and processed file's record length
     * @author Sihyun Park
     * @since 1.0
     */
    @ExceptionHandler(Exception.class)
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> csvUploader(@RequestPart("file") MultipartFile file) {
        String message;
        ResponseEntity<ResponseMessage> response;
        if (CsvUtil.isCsvType(file)) {
            try {
                ResponseMessage rm = csvService.persistPeople(file);

                logger.info("File uploaded successfully: {}", file.getOriginalFilename());
                message = "File uploaded successfully: " + file.getOriginalFilename();
                rm.setMessage(message);
                response = ResponseEntity.status(HttpStatus.OK).body(rm);
            } catch (Exception e) {
                logger.warn("File upload failed with exception: {} | " + e.getMessage(), file.getOriginalFilename());
                message = "File upload failed: " + file.getOriginalFilename();
                response = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, -1));
            }
        } else {
            logger.info("Uploaded file is not CSV: {}", file.getOriginalFilename());
            message = "Uploaded file is not CSV: " + file.getOriginalFilename();
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message, -1));
        }

        return response;
    }
}
