package com.datamatcher.pdfmanipulator.exception;

import java.io.FileNotFoundException;

public class ParseException extends Exception {

    public ParseException(String file_not_found, FileNotFoundException e) {
        super();
    }

    public ParseException(String s) {
        super(s);
    }

    public ParseException(String file_not_found, Exception e) {
        super(e);

    }
}
