package com.example.hello.Controller;

import com.example.hello.Service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/hello")
public class SystemController {

    @Autowired
    private ParserService parserService;

    @GetMapping("/counter_house")
    public void counter_house() throws IOException, ParseException{
        parserService.counter_house();
    }
    @GetMapping("/counter_house_remote")
    public void counter_house_remote() throws IOException, ParseException{
        parserService.counter_house_remote();
    }
    @GetMapping("/counter_personal")
    public void counter_personal() throws IOException, ParseException{
        parserService.counter_personal();
    }
    @GetMapping("/counter_personal_remote")
    public void counter_personal_remote() throws IOException, ParseException{
        parserService.counter_personal_remote();
    }
    @GetMapping("/sewerage")
    public void sewerage() throws IOException, ParseException{
        parserService.sewerage();
    }
    @GetMapping("/sewerage_need_repair")
    public void sewerage_need_repair() throws IOException, ParseException{
        parserService.sewerage_need_repair();
    }
    @GetMapping("/water_pipes")
    public void water_pipes() throws IOException, ParseException{
        parserService.water_pipes();
    }
    @GetMapping("/water_pipes_construction")
    public void water_pipes_construction() throws IOException, ParseException{
        parserService.water_pipes_construction();
    }
    @GetMapping("/water_pipes_construction_crash")
    public void water_pipes_construction_crash() throws IOException, ParseException{
        parserService.water_pipes_construction_crash();
    }
    @GetMapping("/water_pipes_crash")
    public void water_pipes_crash() throws IOException, ParseException{
        parserService.water_pipes_crash();
    }
    @GetMapping("/water_pipes_need_repair")
    public void water_pipes_need_repair() throws IOException, ParseException{
        parserService.water_pipes_need_repair();
    }
    @GetMapping("/water_pipes_net")
    public void water_pipes_net() throws IOException, ParseException{
        parserService.water_pipes_net();
    }

}
