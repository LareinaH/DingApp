package com.admin.ac.ding.controller;

import com.admin.ac.ding.model.RestResponse;
import com.admin.ac.ding.model.SuggestManageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    DingController dingController;

    @RequestMapping(value = "/exportSuggestList", method = RequestMethod.GET)
    public ModelAndView exportSuggestList(
            String gmtStart,
            String gmtEnd
    ) {
        RestResponse<List<SuggestManageVO>> listResult = dingController.querySuggestByDateRange(gmtStart, gmtEnd);
        List<SuggestManageVO> suggestManageVOList;
        if (listResult.getSuccessed()) {
            suggestManageVOList = listResult.getData();
        } else {
            suggestManageVOList = new ArrayList<>();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("detail", suggestManageVOList);
        map.put("name", String.format("%s~%s意见建议列表-%d", gmtStart, gmtEnd, System.currentTimeMillis()));
        map.put("sheetName", "意见建议列表");

        return new ModelAndView(new ExportSuggestView(), map);
    }
}
