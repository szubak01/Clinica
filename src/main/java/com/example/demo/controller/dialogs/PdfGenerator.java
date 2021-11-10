package com.example.demo.controller.dialogs;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.VerticalPositionMark;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfGenerator {
  private final Long prescriptionId;
  private final String patientName;
  private final String doctorName;
  private final String dateTime;
  private final String description;
  private final String filePath;
  private final String fileName;
  private final Color blackColor;

  public PdfGenerator(Long prescriptionId, String patientName, String doctorName, String dateTime, String description, String filePath, String fileName) {
    this.prescriptionId = prescriptionId;
    this.patientName = patientName;
    this.doctorName = doctorName;
    this.dateTime = dateTime;
    this.description = description;
    this.filePath = filePath;
    this.fileName = fileName;
    this.blackColor =  new Color(20, 20, 20);
  }

  private Document initializeDocument() {
    return new Document(PageSize.A6, 30, 30, 40, 25);
  }

  private void initializeWriter(Document document) throws FileNotFoundException {
    FileOutputStream file = new FileOutputStream(this.filePath + this.fileName);
    PdfWriter.getInstance(document, file);
  }

  private void addWhiteSpace(Document document) {
    Paragraph paragraphText = new Paragraph("\n");
    document.add(paragraphText);
  }

  private void addBell(Document document) {
    Paragraph paragraphText = new Paragraph("___________________________________");
    document.add(paragraphText);
    addWhiteSpace(document);
  }

  private void openDocument(Document document) {
    document.open();
  }

  private void closeDocument(Document document) {
    document.close();
  }

  private Font getHeaderStyle() {
    return FontFactory.getFont(FontFactory.HELVETICA_BOLD,22, Font.BOLD, this.blackColor);
  }

  private Font getSubHeaderGeneralStyle() {
    return FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL, this.blackColor);
  }

  private Font getArgumentStyle() {
    return FontFactory.getFont(FontFactory.HELVETICA_BOLD,10, Font.NORMAL, this.blackColor);
  }

  private Font getValueStyle() {
    return FontFactory.getFont(FontFactory.HELVETICA,10, Font.NORMAL, this.blackColor);
  }

  private Font getFooterStyle() {
    return FontFactory.getFont(FontFactory.TIMES_ROMAN,9, Font.NORMAL, this.blackColor);
  }

  private void setParagraphWithFont(Document document, String text, Font font) {
    Paragraph paragraphText = new Paragraph();
    paragraphText.setFont(font);
    paragraphText.add(text);
    document.add(paragraphText);
  }

  private void addPrescriptionHeader(Document document) {
    Font font = getHeaderStyle();
    setParagraphWithFont(document, "Prescription", font);
    addWhiteSpace(document);
    addWhiteSpace(document);
  }

  private void addTwoTexts(Document document, String firstText, String secondText, Font font) {
    Paragraph paragraphText = new Paragraph();
    paragraphText.setFont(font);
    paragraphText.add(firstText);
    Chunk rightText = new Chunk(new VerticalPositionMark());
    paragraphText.add(new Chunk(rightText));
    paragraphText.add(secondText);
    document.add(paragraphText);
  }

  private void addPrescriptionSubHeader(Document document) {
    Font font = getSubHeaderGeneralStyle();
    addTwoTexts(document, "Number " + this.prescriptionId, "Issued " + this.dateTime, font);
    addBell(document);
  }

  private void setRightText(Document document, String text, Font font) {
    Paragraph textArea = new Paragraph();
    textArea.setFont(font);
    textArea.add(text);
    textArea.setAlignment(Element.ALIGN_RIGHT);
    document.add(textArea);
  }

  private void addPrescriptionFooter(Document document) {
    Font font = getFooterStyle();
    setRightText(document, "_______________", font);
    setRightText(document, this.doctorName, font);
  }

  private PdfPTable initTable() {
    PdfPTable table = new PdfPTable(new float[] { 30, 70 });
    table.setWidthPercentage(100);
    table.setHeaderRows(1);
    table.addCell("Key");
    table.addCell("Value");
    table.setSkipFirstHeader(true);
    return table;
  }

  private void addCellToTable(PdfPTable table, String text, Font font) {
    Phrase phrase = new Phrase();
    phrase.setFont(font);
    phrase.add(text);
    PdfPCell cell = new PdfPCell(phrase);
    cell.setBorder(Rectangle.NO_BORDER);
    table.addCell(cell);
  }

  private void addPrescriptionData(Document document, String arg, String value) {
    PdfPTable table = initTable();
    Font argumentFont = getArgumentStyle();
    addCellToTable(table, arg, argumentFont);
    Font valueFont = getValueStyle();
    addCellToTable(table, value, valueFont);
    document.add(table);
  }

  private void addPrescriptionDataWithBell(Document document, String arg, String value) {
    addPrescriptionData(document, arg, value);
    addBell(document);
  }

  public void createPrescription() throws FileNotFoundException {
    Document document = initializeDocument();
    initializeWriter(document);
    openDocument(document);
    addPrescriptionHeader(document);
    addPrescriptionSubHeader(document);
    addPrescriptionData(document, "Patient: ", this.patientName);
    addPrescriptionDataWithBell(document, "Doctor: ", this.doctorName);
    addPrescriptionDataWithBell(document, "Prescription: ", this.description);
    addPrescriptionFooter(document);
    closeDocument(document);
  }

}