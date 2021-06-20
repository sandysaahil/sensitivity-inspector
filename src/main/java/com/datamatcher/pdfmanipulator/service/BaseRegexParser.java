package com.datamatcher.pdfmanipulator.service;


import com.datamatcher.pdfmanipulator.exception.ParseException;

import java.util.List;
import java.util.regex.Pattern;

public interface BaseRegexParser {
    public List<Pattern> parseInput(final String input) throws ParseException;
}
