package com.datamatcher.pdfmanipulator.controller;

import com.datamatcher.pdfmanipulator.config.ConfigProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@RestController()
public class PdfController {

    @Autowired
    private ConfigProperties configProperties;

    @GetMapping("manipulate")
    public ResponseEntity<String> maniualtePdf() {
        File sourceFile = null;
        try {
            sourceFile = ResourceUtils.getFile("classpath:source.pdf");
        } catch (FileNotFoundException e) {
            return new ResponseEntity("File not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PDDocument sourceDocument = null;
        Document targetDocument = null;
        try {
            sourceDocument = PDDocument.load(sourceFile);

            PDFTextStripper pdfTextStripper =  new PDFTextStripper();
            pdfTextStripper.setPageStart("\t");
            pdfTextStripper.setSortByPosition(true);

            targetDocument = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(targetDocument, new FileOutputStream("target.pdf"));
            targetDocument.open();


            for (String line: pdfTextStripper.getText(sourceDocument).split(pdfTextStripper.getParagraphStart()))
            {
                log.info("Search Text " + configProperties.getSearchText());
                log.info("Replacement Text " + configProperties.getReplacementText());
                boolean isContain = line.contains(configProperties.getSearchText());
                if (isContain) {
                    line = line.replace(configProperties.getSearchText(), configProperties.getReplacementText());
                }

                Paragraph para = new Paragraph(line);
                para.setAlignment(Element.ALIGN_JUSTIFIED);

                targetDocument.add(para);
                targetDocument.add(new Paragraph("\n"));
            }
        } catch (IOException e) {
            return new ResponseEntity("Error while manupulating document", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DocumentException e) {
            return new ResponseEntity("Error while manupulating the document", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (targetDocument != null) targetDocument.close();
            try {
                if (sourceDocument != null) sourceDocument.close();
            } catch (IOException e) {
                return new ResponseEntity("IO Error while closing the document", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity("PDF Manipulated Succssfully", HttpStatus.OK);

    }
}
