package com.admin.ac.ding.controller;

import org.apache.poi.ss.usermodel.*;

public class DefaultCellStyleImpl implements DefaultCellStyle {
    @Override
    public CellStyle setCellStyle(Workbook workbook) {
        // create style for header cells
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        font.setBold(true);
        font.setColor(Font.COLOR_NORMAL);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
