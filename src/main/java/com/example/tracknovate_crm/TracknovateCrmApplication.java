package com.example.tracknovate_crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class TracknovateCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracknovateCrmApplication.class, args);
        Date date = new Date();


        try {
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
           String nextactiondate =format.format(date);
           // NextActiondate = nextactiondate.toString();
            System.out.println(nextactiondate);

        } catch (Exception ex) {
            ex.getMessage();
        }

    }


}
