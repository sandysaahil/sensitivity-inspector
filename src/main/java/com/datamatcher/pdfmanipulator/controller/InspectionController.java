package com.datamatcher.pdfmanipulator.controller;

import com.datamatcher.pdfmanipulator.exception.ParseException;
import com.datamatcher.pdfmanipulator.service.BaseRegexParser;
import com.datamatcher.pdfmanipulator.service.FileJsonRegexParser;
import com.datamatcher.pdfmanipulator.service.MatcherService;
import com.gliwka.hyperscan.util.PatternFilter;
import com.gliwka.hyperscan.wrapper.CompileErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.datamatcher.pdfmanipulator.service.MatcherService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RestController()
public class InspectionController {

    @Autowired
    private BaseRegexParser fileJsonRegexParser;

    @Autowired
    private MatcherService matcherService;

    private final String inputRegexFile = "regex.json";
    private final String sourceDataRegexFile = "facebook_auth.txt";

    @GetMapping("inspect")
    public ResponseEntity<String> maniualtePdf() {

        try {
            List<Pattern> patterns = fileJsonRegexParser.parseInput(inputRegexFile);
            log.info("The Patterns length is " + patterns.size());
            PatternFilter patternFilter = new PatternFilter(patterns);
            File sourceDataFile = ResourceUtils.getFile("classpath:" + sourceDataRegexFile);
            this.matcherService.matchRegex(sourceDataFile, patternFilter);

        } catch (Exception e) {
            log.error("Parsing Error while inspecting the input file", e);
            return new ResponseEntity("Inspection Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity("Inspection Completed Succssfully", HttpStatus.OK);

    }
}
