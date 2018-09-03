package com.admin.ac.ding.controller;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static org.apache.poi.ss.usermodel.Font.U_SINGLE;

public abstract class ExcelView extends AbstractXlsView {
    protected CellStyle cellStyle;
    protected CellStyle hyperLinkCellStyle;

    protected abstract void setRow(Sheet sheet, Map<String, Object> map);

    protected abstract void setStyle(Workbook workbook);

    protected String getHyperLink(String address, String label) {
        return "HYPERLINK(\"" + address + "\",\"" + label + "\")";
    }

    protected void setHyperLinkStyle(Workbook workbook) {
        CellStyle linkStyle = workbook.createCellStyle();
        Font cellFont= workbook.createFont();
        cellFont.setUnderline(U_SINGLE);
        cellFont.setColor(IndexedColors.BLUE.getIndex());
        linkStyle.setFont(cellFont);

        hyperLinkCellStyle = linkStyle;
    }

    /**
     *
     * @param map
     * name:文件名
     * sheetName:sheet名
     * detail:数据
     * @param workbook
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     */
    @Override
    protected void buildExcelDocument(
            Map<String, Object> map,
            Workbook workbook,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws UnsupportedEncodingException {
        String excelName = map.get("name").toString() + ".xls";
        String Agent = request.getHeader("User-Agent");
        if (null != Agent) {
            Agent = Agent.toLowerCase();
            if (Agent.indexOf("firefox") != -1) {
                response.setHeader("content-disposition", String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(excelName, "utf-8")));
            } else {
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8"));
            }
        }
        response.setContentType("application/ms-excel; charset=UTF-8");
        String sn = (String)map.get("sheetName");
        Sheet sheet = workbook.createSheet(sn);
        sheet.setDefaultColumnWidth(30);
        setStyle(workbook);
        setHyperLinkStyle(workbook);
        setRow(sheet, map);
    }
}
