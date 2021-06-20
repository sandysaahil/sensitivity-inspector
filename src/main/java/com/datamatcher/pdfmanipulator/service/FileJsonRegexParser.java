package com.datamatcher.pdfmanipulator.service;

import com.datamatcher.pdfmanipulator.exception.ParseException;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class FileJsonRegexParser implements BaseRegexParser {

    @Override
    public List<Pattern> parseInput(String input) throws ParseException {
        Gson gson = new Gson();
        List<Pattern> regexPatterns = new ArrayList<>();
        LinkedTreeMap<String, LinkedTreeMap> regexTreeMap = null;
        Reader targetReader = null;
        try {
            File initialFile = ResourceUtils.getFile("classpath:" + input);
            initialFile.createNewFile();
            targetReader = new FileReader(initialFile);
            regexTreeMap = gson.fromJson(targetReader, LinkedTreeMap.class);
            if (regexTreeMap.isEmpty() == false) {

                for (Map.Entry<String, LinkedTreeMap> entry : regexTreeMap.entrySet()) {
                    LinkedTreeMap regexList = regexTreeMap.get(entry.getKey());
                    List<LinkedTreeMap> regexes = (List<LinkedTreeMap>)regexList.get("regexes");
                    for (LinkedTreeMap<String, String> current : regexes) {
                        for (Map.Entry<String, String> currentEntry : current.entrySet()) {
                            if (currentEntry.getKey().equals("value")){
                               regexPatterns.add(Pattern.compile((String)currentEntry.getValue(), Pattern.CASE_INSENSITIVE));
                            }
                        }
                    }

                }

            } else {
                throw new ParseException("No Regex found in file "+ input);
            }
        } catch (FileNotFoundException e) {
            throw new ParseException("File not found", e);
        } catch (Exception e) {
            throw new ParseException("Exception occured while parsing inut file", e);
        } finally {
         if (targetReader != null) {
             try {
                 targetReader.close();
             } catch (IOException e) {
                 throw new ParseException("Exception occured while parsing inut file", e);
             }
         }
        }

        return regexPatterns;
    }
}
