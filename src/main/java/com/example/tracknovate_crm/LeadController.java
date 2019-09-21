package com.example.tracknovate_crm;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.internet.MimeMessage;
import javax.print.attribute.standard.Media;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class LeadController {

    @Autowired

    LeadRepository leadRepository;

    @Autowired

    JavaMailSender javaMailSender;

    int introcount = 0, approvalcount = 0, eligiblitycount = 0;

    @RequestMapping(value = "/addlead", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject AddLead(
            @RequestParam(value = "Subject", defaultValue = "undefined") String subject,
            @RequestParam(value = "Email", defaultValue = "undefined") String email,
            @RequestParam(value = "Mobile", defaultValue = "undefined") String mobile,
            @RequestParam(value = "Smobile", defaultValue = "undefined") String smobile,
            @RequestParam(value = "Status", defaultValue = "undefined") String status,
            @RequestParam(value = "Source", defaultValue = "undefined") String source,
            @RequestParam(value = "Type", defaultValue = "undefined") String type,
            @RequestParam(value = "Description", defaultValue = "undefined") String description,
            @RequestParam(value = "Stage", defaultValue = "undefined") String stage,
            @RequestParam(value = "Nextactiondate", defaultValue = "undefined") String nextactiondate,
            @RequestParam(value = "Name", defaultValue = "undefined") String name,
            @RequestParam(value = "Address", defaultValue = "undefined") String address,
            @RequestParam(value = "State", defaultValue = "undefined") String state,
            @RequestParam(value = "City", defaultValue = "undefined") String city,
            @RequestParam(value = "Pincode", defaultValue = "undefined") String pincode,
            @RequestParam(value = "token", defaultValue = "undefined") String token,
            @RequestParam(value = "Useremail", defaultValue = "undefined") String useremail) {


        String number = null;
        String createdby = useremail;
        Date date = new Date();
        String createddate = "";
        DateFormat format = new SimpleDateFormat("M/dd/yyyy");
        createddate = format.format(date);
        JSONObject jsonObject = new JSONObject();
        if (subject.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter subject");
            return jsonObject;

        } else if (email.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter email");
            return jsonObject;

        } else if (mobile.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter mobile");
            return jsonObject;


        } else if (status.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter status");
            return jsonObject;

        } else if (stage.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter stage");
            return jsonObject;

        } else if (source.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter source");
            return jsonObject;

        } else if (createddate.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter createddate");
            return jsonObject;

        } else if (createdby.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter lastname");
            return jsonObject;

        } else if (description.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter description");
            return jsonObject;

        } else if (type.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter type");
            return jsonObject;

        } else if (name.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter lead name");
            return jsonObject;
        } else if (address.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter address");
            return jsonObject;
        } else if (nextactiondate.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter nextactiondate");
            return jsonObject;

        } else if (state.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter state");
            return jsonObject;

        } else if (city.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter city");
            return jsonObject;

        } else {
            if (leadRepository.checkmobile(mobile).isEmpty()) {
                if (leadRepository.addlead(subject, email, mobile, smobile, number, status, source, createddate, createdby,
                        type, description, stage, nextactiondate, name, address, state, city, pincode, token, useremail) >= 1) {


                    if (stage.equals("introduction")) {
                        MimeMessage messageMail = javaMailSender.createMimeMessage();
                        try {
                            MimeMessageHelper helper = new MimeMessageHelper(messageMail, false, "utf-8");
                            helper.setTo(email);
                            helper.setCc(useremail);
                            helper.setBcc("jitendrasoni@tracknovate.com");
                            helper.setSubject("Voso Store – Registration confirmation for Amazon Easy.");
                            messageMail.setContent(leadRepository.getContent1(), "text/html");
                            javaMailSender.send(messageMail);

                            RestTemplate restTemplate = new RestTemplate();
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);

                            HttpEntity<String> entity = new HttpEntity<String>(headers);
                            String message = leadRepository.getContent1();
                            restTemplate.put("http://trans.masssms.tk/api.php?username=ezshop&password=sumit500&sender=VosoSt&sendto=" + (mobile) + "&message=Dear Sir,\n" +
                                    "Thank you for showing your interest in Amazon Easy Store. Your application is received and currently\n" +
                                    "under verification stage. We will get back to you in 24 working hours.\n" +
                                    "Thanks:\n" +
                                    "Voso Store.\n" +
                                    "Exclusive Partner - Amazon Easy.\n" +
                                    "6269556278", entity);

                        } catch (Exception ex) {
                            ex.getMessage();
                        }

                    }
                    jsonObject.put("status", true);
                    jsonObject.put("message", "Lead Added successfully");
                    jsonObject.put("data",leadRepository.lastinsertedrow(useremail, token));
                    return jsonObject;
                } else {
                    jsonObject.put("status", false);
                    jsonObject.put("message", "Lead not Added.");
                    return jsonObject;


                }
            } else {
                jsonObject.put("status", false);
                jsonObject.put("message", "contact number is already exist");
                return jsonObject;
            }

        }
    }
    @RequestMapping(value = "/deletelead", method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="localhost:8443")
    @ResponseBody
    public JSONObject DeleteUser(@RequestParam(value="leadnumber" ,defaultValue = "undefined") String leadnumber, @RequestParam(value="Useremail" ,defaultValue = "undefined") String useremail,@RequestParam(value="Token" ,defaultValue = "undefined") String token ){
        JSONObject jsonObject = new JSONObject();
        if (leadRepository.deletelead(leadnumber,useremail, token)==0) {
            jsonObject.put("status", false);
            jsonObject.put("message", "Lead is not Exist!");
            return jsonObject;
        } else {
            jsonObject.put("status", true);
            jsonObject.put("message", "Lead Deleted Successfully!");
            return jsonObject;
        }
    }
        @RequestMapping(value = "/checkmobile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject checkmobile (@RequestParam("Email") String email, @RequestParam("Token") String
        token, @RequestParam("Contact") String contact ){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.checkmobile(contact).isEmpty()) {


                jsonObject.put("status", true);
                jsonObject.put("data", "mobile number doesnt exist");
                return jsonObject;
            } else {
                jsonObject.put("status", false);
                jsonObject.put("data", "mobile number exist");
                return jsonObject;
            }

        }


        @RequestMapping(value = "/getallleads", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject getallleads (@RequestParam("Email") String email, @RequestParam("Token") String token){

       /* JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("Status","Data fetched Sucessfullye");
        jsonObject.put("Result","Data fetched Sucessfully");
        jsonObject.put("Data",jsonArray);
        jsonArray.add(leadRepository.getallleads());

      SimpleMailMessage msg = new SimpleMailMessage();

            msg.setTo("ajaybatham306@gmail.com");
            msg.setTo("abdulbariishere@gmail.com","tracknovate@gmail.com");

            msg.setSubject("Tracknovate CRM:Lead created");
            msg.setText("hello Sumit, \n your lead is created.\n" +
                    "Subject    :   lead is related with GPS devices \n" +
                    "Status     :   open \n" +
                    "Stage     :   open  \n" +
                    "Source    :    Retailer \n" +
                    "Next Action Date       :   14/5/2019\n");

            javaMailSender.send(msg);
*/

            JSONObject jsonObject = new JSONObject();
            if (leadRepository.getallleads(email, token).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("data", "something went wrong");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.getallleads(email, token));
                return jsonObject;
            }
        }
        /* GET LEAD FOR SPECIFIC NUMBER */
        @RequestMapping(value = "/getleadfornumber", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public List<Lead> getleadfornumber (@RequestParam("Number") String leadnumber){

            return leadRepository.getleadbynumber(leadnumber);
        }
        /* GET LEAD FOR SPECIFIC OWNER */
        @RequestMapping(value = "/getleadforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject getleadforuser (@RequestParam("leadowner") String leadowner, @RequestParam("email") String
        email, @RequestParam("token") String token){

            JSONObject jsonObject = new JSONObject();
            if(email.equals("null") && token.equals("null")){
                jsonObject.put("status", false);
                jsonObject.put("message", "Not Logged In!");
                return jsonObject;

            }
            else
            {
                if (leadRepository.getleadforuser(leadowner, email, token).isEmpty()) {
                    jsonObject.put("status", true);
                    jsonObject.put("message", "you don't have any lead yet!");
                    return jsonObject;
                } else {
                    jsonObject.put("status", true);
                    jsonObject.put("data", leadRepository.getleadforuser(leadowner, email, token));
                    return jsonObject;
                }
            }
        }
        /* Today's modification for user */
        @RequestMapping(value = "/todaymodification", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject todaymodification (@RequestParam("leadowner") String leadowner, @RequestParam("email") String
        email, @RequestParam("token") String token){

            JSONObject jsonObject = new JSONObject();
            if (leadRepository.todaymodificationforuser(leadowner, email, token).isEmpty()) {
                jsonObject.put("status", true);
                jsonObject.put("message", "leads are not modified yet!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.todaymodificationforuser(leadowner, email, token));
                return jsonObject;
            }
        }

        /* Today's modification for all */
        @RequestMapping(value = "/todaymodificationforall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject todaymodificationforall (@RequestParam("email") String email, @RequestParam("token") String
        token){

            JSONObject jsonObject = new JSONObject();
            if (leadRepository.todaymodificationforall(email, token).isEmpty()) {
                jsonObject.put("status", true);
                jsonObject.put("message", "you don't have any lead yet!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.todaymodificationforall(email, token));
                return jsonObject;
            }
        }


        /* GET LEAD BY NUMBER */
        ;
        @RequestMapping(value = "/getleadtoupdate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public Lead GetLeadToUpdate (@RequestParam("Number") String number){
            return leadRepository.getleadtoupdate(number);
        }
        @RequestMapping(value = "/getsinglelead", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject getsinglelead (@RequestParam("leadnumber") String
        leadnumber, @RequestParam("Useremail") String email, @RequestParam("Token") String token){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", true);
            jsonObject.put("data", leadRepository.getleadforspecificleadnumber(leadnumber, email, token));
            return jsonObject;
        }
        /* UPDATE LEAD */
        @RequestMapping(value = "/updatelead", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject updateLeadbynumber (@RequestParam("Name") String name, @RequestParam("Subject") String
        subject, @RequestParam("Email") String email, @RequestParam("Contact") String
        contact, @RequestParam("Scontact") String scontact, @RequestParam("Address") String
        address, @RequestParam("Pincode") String pincode, @RequestParam("Status") String
        status, @RequestParam("Stage") String stage, @RequestParam("Source") String source,
                @RequestParam("Type") String type, @RequestParam("Description") String
        description, @RequestParam("State") String state, @RequestParam("City") String
        city, @RequestParam("Nextaction") String nextaction, @RequestParam("leadnumber") String
        leadnumber, @RequestParam("useremail") String useremail, @RequestParam("Token") String token){
            JSONObject jsonObject = new JSONObject();
            Date date = new Date();
            String modifieddate = "";
            DateFormat format = new SimpleDateFormat("M/dd/yyyy");
            modifieddate = format.format(date);
            String modifiedby = useremail;

            if (leadRepository.updatelead(name, subject, email, contact, scontact, address, status, stage, source, type, description, pincode, state, city, modifieddate, modifiedby, nextaction, leadnumber, useremail, token) != null) {
                if(stage.equals("eligibility")){
                if (leadRepository.eligiblitycheck(email, contact, stage, leadnumber) == 1) {
                    MimeMessage messageMail = javaMailSender.createMimeMessage();
                    try {
                        MimeMessageHelper helper = new MimeMessageHelper(messageMail, false, "utf-8");
                        helper.setTo(email);
                        helper.setCc(useremail);
                        helper.setBcc("jitendrasoni@tracknovate.com");
                        helper.setSubject("Voso Store - Eligibility confirmation for Amazon Easy.");
                        messageMail.setContent(leadRepository.getContent2(), "text/html");
                        javaMailSender.send(messageMail);

                        RestTemplate restTemplate = new RestTemplate();
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        HttpEntity<String> entity = new HttpEntity<String>(headers);
                        String message = leadRepository.getContent2();
                        restTemplate.put("http://trans.masssms.tk/api.php?username=ezshop&password=sumit500&sender=VosoSt&sendto=" + (contact) + "&message=Dear Sir,\n" +
                                "Reference to you Easy Store application. Your application is received and the same is eligible for\n" +
                                "allotment of the Easy store. Our business associate will connect with you with further details.\n" +
                                "Thank you\n" +
                                "Voso Store.\n" +
                                "Exclusive Partner - Amazon Easy.\n" +
                                "6269556278.", entity);

                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
                }
                else if(stage.equals("approval")) {
                    if (leadRepository.approvalcheck(email, contact, stage, leadnumber) == 1) {
                        MimeMessage messageMail = javaMailSender.createMimeMessage();
                        try {
                            MimeMessageHelper helper = new MimeMessageHelper(messageMail, false, "utf-8");
                            helper.setTo(email);
                            helper.setCc(useremail);
                            helper.setBcc("jitendrasoni@tracknovate.com");
                            helper.setSubject("Voso Store - Approval confirmation for Amazon Easy.");
                            messageMail.setContent(leadRepository.getContent3(), "text/html");
                            javaMailSender.send(messageMail);

                            RestTemplate restTemplate = new RestTemplate();
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);

                            HttpEntity<String> entity = new HttpEntity<String>(headers);
                            String message = leadRepository.getContent3();
                            restTemplate.put("http://trans.masssms.tk/api.php?username=ezshop&password=sumit500&sender=VosoSt&sendto=" + (contact) + "&message=Dear Sir,\n" +
                                    "Congratulations!\n" +
                                    "Your application is approved basis on the documents and details provided by you. You will shortly\n" +
                                    "receive your login credentials followed by schedule for training and process for going live on store\n" +
                                    "network.\n" +
                                    "Thank you\n" +
                                    "Voso Store.\n" +
                                    "Exclusive Partner - Amazon Easy.\n" +
                                    "6269556278.", entity);

                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                }

                jsonObject.put("status", true);
                jsonObject.put("message", "Lead update successfully");
                return jsonObject;
            } else {
                jsonObject.put("status", false);
                jsonObject.put("message", "Lead  not updated ");
                return jsonObject;
            }
        }

        /* TODAY TASK FOR ADMIN */
        @RequestMapping(value = "/todaystaskforadmin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject TodayTaskForAdmin (@RequestParam("Email") String email, @RequestParam("Token") String token){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.todaystaskforadmin().isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("data", "something went wrong");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.todaystaskforadmin());
                return jsonObject;
            }
        }
        /* TODAY TASK FOR USER */
        @RequestMapping(value = "/todaystaskforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject TodayTaskForUser (@RequestParam("Owner") String owner, @RequestParam("Useremail") String
        email, @RequestParam("Token") String token){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.todaystaskforuser(owner, email, token).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("data", "something went wrong");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.todaystaskforuser(owner, email, token));
                return jsonObject;
            }
        }
        /* GET LEAD LOG FOR USER */
        @RequestMapping(value = "/getleadlogforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject getleadlogforuser (@RequestParam("Email") String email, @RequestParam("Token") String
        token, @RequestParam(value = "leadnumber", defaultValue = "undefined") String leadnumber){
            JSONObject jsonObject = new JSONObject();
            if (!leadnumber.equals("undefined")) {
                if (leadRepository.getleadlogforuser(email, token, leadnumber).isEmpty()) {
                    jsonObject.put("status", false);
                    jsonObject.put("message", "entered lead doesn't exist!");
                    return jsonObject;
                } else {
                    jsonObject.put("status", true);
                    jsonObject.put("data", leadRepository.getleadlogforuser(email, token, leadnumber));
                    return jsonObject;
                }

            } else {
                jsonObject.put("status", false);
                jsonObject.put("message", "please enter lead number");
                return jsonObject;
            }
        }


        /* GET LEAD LOG FOR ADMIN */
        @RequestMapping(value = "/getleadlogforadmin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject getleadlogforadmin (@RequestParam("Email") String email, @RequestParam("Token") String
        token, @RequestParam("leadnumber") String leadnumber){

            JSONObject jsonObject = new JSONObject();
            if (!leadnumber.equals("undefined")) {
                if (leadRepository.getleadlogforadmin(email, token, leadnumber).isEmpty()) {
                    jsonObject.put("status", false);
                    jsonObject.put("message", "entered lead doesn't exist! ");
                    return jsonObject;
                } else {
                    jsonObject.put("status", true);
                    jsonObject.put("data", leadRepository.getleadlogforadmin(email, token, leadnumber));
                    return jsonObject;
                }
            } else {
                jsonObject.put("status", false);
                jsonObject.put("message", "please enter lead number");
                return jsonObject;
            }
        }
        /* Total open lead for all */
        @RequestMapping(value = "/openleadforall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject openlead (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.openleadforall(useremail, token).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads are in Open Status!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.openleadforall(useremail, token));
                return jsonObject;
            }
        }



        /* Total open lead for User */
        @RequestMapping(value = "/openleadforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject openlead (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token,
                @RequestParam("Owner") String owner){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.openleadforuser(useremail, token, owner).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads are in Open Status!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.openleadforuser(useremail, token, owner));
                return jsonObject;
            }
        }

        /* Total close with success lead for User */
        @RequestMapping(value = "/closeleadwithsuccessforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject closeleadwithsuccess (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token,
                @RequestParam("Owner") String owner){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.closeleadwithsuccessforuser(useremail, token, owner).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads are in Close with success Status!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.closeleadwithsuccessforuser(useremail, token, owner));
                return jsonObject;
            }
        }
        /* Total close with success lead for all */
        @RequestMapping(value = "/closeleadwithsuccessforall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject closeleadwithsuccess (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token
                                           ){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.closeleadwithsuccessforall(useremail, token).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads are in Close with success Status!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.closeleadwithsuccessforall(useremail, token));
                return jsonObject;
            }
        }

        /* Total close without success lead for User */
        @RequestMapping(value = "/closeleadwithoutsuccessforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject closeleadwithoutsuccess (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token,
                @RequestParam("Owner") String owner){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.closeleadwithoutsuccessforuser(useremail, token, owner).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads are in Close with success Status!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.closeleadwithoutsuccessforuser(useremail, token, owner));
                return jsonObject;
            }
        }


        /* Total close without success lead for all */
        @RequestMapping(value = "/closeleadwithoutsuccessforall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject closeleadwithoutsuccessforall (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token
                                              ){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.closeleadwithoutsuccessforall(useremail, token).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads are in Close without success Status!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.closeleadwithoutsuccessforall(useremail, token));
                return jsonObject;
            }
        }
        /* pending leads for all */
        @RequestMapping(value = "/pendingleadforall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject pendingleadforall (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token
    ){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.pendingleadforall(useremail, token).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No pending leads!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.pendingleadforall(useremail, token));
                return jsonObject;
            }
        }
        /* pending leads for user */
        @RequestMapping(value = "/pendingleadforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject pendingleadforall (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token,
                @RequestParam("Owner") String owner
    ){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.pendingleadforuser(useremail, token, owner).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No pending leads!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.pendingleadforuser(useremail, token, owner));
                return jsonObject;
            }
        }
        /* total leads count */
        @RequestMapping(value = "/totalleadcount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject totalleadcount (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token

    ){
            JSONObject jsonObject = new JSONObject();
            Map<String, List> hm = new HashMap<String, List>();
            hm.put("TotalLead", leadRepository.totalleadcount(useremail, token));
            hm.put("TodaysLead", leadRepository.todaysleadcount(useremail, token));
            hm.put("TodaysModification", leadRepository.todaysmodificationleadcount(useremail, token));
            hm.put("TotalOpenStatusLeads", leadRepository.totalopencount(useremail, token));
            hm.put("TotalClosewithSuccessCount", leadRepository.totalclosewithsuccesscount(useremail, token));
            hm.put("TotalClosewitoutSuccessCount", leadRepository.totalclosewithoutsuccesscount(useremail, token));
            hm.put("TotalPendingLeadsCount", leadRepository.pendingleadcount(useremail, token));
            jsonObject.putAll(hm);
            return jsonObject;

        }
        /* total leads count for user*/
        @RequestMapping(value = "/totalleadcountforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject totalleadcountforlead (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token,
                @RequestParam("Owner") String leadowner

    ){
            JSONObject jsonObject = new JSONObject();
            Map<String, List> hm = new HashMap<String, List>();
            hm.put("TotalLead", leadRepository.totalleadcountforuser(useremail, token, leadowner));
            hm.put("TodaysLead", leadRepository.todaysleadcountforuser(useremail, token, leadowner));
            hm.put("TodaysModification", leadRepository.todaysmodificationleadcountforuser(useremail, token, leadowner));
            hm.put("TotalOpenStatusLeads", leadRepository.totalopencountforuser(useremail, token, leadowner));
            hm.put("TotalClosewithSuccessCount", leadRepository.totalclosewithsuccesscountforuser(useremail, token, leadowner));
            hm.put("TotalClosewitoutSuccessCount", leadRepository.totalclosewithoutsuccesscountforuser(useremail, token, leadowner));
            hm.put("TotalPendingLeadsCount", leadRepository.pendingleadcountforuser(useremail, token, leadowner));
            jsonObject.putAll(hm);
            return jsonObject;

        }


        /* Report Total Leads for all */
        @RequestMapping(value = "/reporttotalleads", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject ReportTotalLeads (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token,
                @RequestParam("fromdate") String fromDate,
                @RequestParam("todate") String toDate

    ){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.ReportTotalLeads(useremail, token, fromDate, toDate).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads between these dates!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.ReportTotalLeads(useremail, token, fromDate, toDate));
                return jsonObject;
            }
        }
        /* Report Total Leads for user */
        @RequestMapping(value = "/reporttotalleadsforuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public JSONObject ReportTotalLeads (@RequestParam("Useremail") String useremail,
                @RequestParam("token") String token,
                @RequestParam("fromdate") String fromDate,
                @RequestParam("todate") String toDate,
                @RequestParam("Owner") String leadowner

    ){
            JSONObject jsonObject = new JSONObject();
            if (leadRepository.ReportTotalLeadsforuser(useremail, token, fromDate, toDate, leadowner).isEmpty()) {
                jsonObject.put("status", false);
                jsonObject.put("message", "No leads between these dates!");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("data", leadRepository.ReportTotalLeadsforuser(useremail, token, fromDate, toDate, leadowner));
                return jsonObject;
            }
        }




        /* GET LEAD LOG FOR ADMIN */
        @RequestMapping(value = "/importlead", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @CrossOrigin(origins = "localhost:8443")
        @ResponseBody
        public String uploadFile (@RequestParam("file") MultipartFile file){
            String fileName = leadRepository.importexcel(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

            return "file uploaded successfully";
        }
    }