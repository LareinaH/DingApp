package com.admin.ac.ding.controller;

import com.admin.ac.ding.enums.SuggestProcessStatus;
import com.admin.ac.ding.model.SuggestManageVO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ExportSuggestView extends ExcelView {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void setRow(Sheet sheet, Map<String, Object> map) {
        Row header = sheet.createRow(0);
        int cellIndex = 0;
        header.createCell(cellIndex).setCellValue("提交人");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("提交人电话");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("提交人所在部门");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("提交时间");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("意见描述");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("处理人");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("处理时间");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("处理备注");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);
        header.createCell(cellIndex).setCellValue("当前状态");
        header.getCell(cellIndex++).setCellStyle(super.cellStyle);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        List<SuggestManageVO> suggestManageVOList = (List<SuggestManageVO>)map.get("detail");
        AtomicInteger rowIndex = new AtomicInteger(1);
        suggestManageVOList.forEach(x -> {
            int ci = 0;
            Row row = sheet.createRow(rowIndex.getAndIncrement());
            row.createCell(ci++).setCellValue(x.getSubmitUserDetail().getName());
            row.createCell(ci++).setCellValue(x.getSubmitUserDetail().getMobile());
            row.createCell(ci++).setCellValue(x.getSubmitUserDetail().getDeptInfoList().stream().map(y -> y.getName()).collect(Collectors.joining(",")));
            row.createCell(ci++).setCellValue(sdf.format(x.getGmtCreate()));
            row.createCell(ci++).setCellValue(x.getSuggest());
            if (x.getProcessUserDetail() != null) {
                row.createCell(ci++).setCellValue(x.getProcessUserDetail().getName());
            } else {
                row.createCell(ci++).setCellValue("-");
            }

            if (x.getGmtProcess() != null) {
                row.createCell(ci++).setCellValue(sdf.format(x.getGmtProcess()));
            } else {
                row.createCell(ci++).setCellValue("-");
            }
            row.createCell(ci++).setCellValue(x.getProcessComment());
            row.createCell(ci++).setCellValue(SuggestProcessStatus.valueOf(x.getStatus()).getDisplayName());
        });
    }

    @Override
    protected void setStyle(Workbook workbook) {
        super.cellStyle = new DefaultCellStyleImpl().setCellStyle(workbook);
    }
}
