package com.admin.ac.ding.controller;

import com.admin.ac.ding.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin", produces = "application/json; charset=UTF-8")
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping(value = "/getMeetingRoomList", method = {RequestMethod.GET})
    public RestResponse<List<Map<String, Object>>> getMeetingRoomList() {
        return null;
    }
}
