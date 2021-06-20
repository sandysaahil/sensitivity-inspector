package com.datamatcher.pdfmanipulator.service;

import com.gliwka.hyperscan.util.PatternFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;

@Slf4j
@Service
public class MatcherService {

    public void matchRegex(File source, PatternFilter patternFilter) throws IOException {
        log.info(" Source File is" + source + " and FilterPattern is " + patternFilter);
        List<String> sourceStrings = Files.readAllLines(Paths.get(source.toURI()));
        for (String current : sourceStrings) {
            log.info("CHecking Strng " + current);
            List<Matcher> matchers = patternFilter.filter(current);
            for(Matcher matcher : matchers) {
                while (matcher.find()) {
                    System.out.println("****" + matcher.group(1));
                }
            }
        }
    }
}
