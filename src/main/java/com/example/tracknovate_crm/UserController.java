package com.example.tracknovate_crm;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import java.util.Base64.Encoder;

import static java.lang.System.out;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class UserController<jsonObject> extends HttpServlet {

    @Autowired
    UserRepository userRepository;
    User user;
    String u1;

    @Autowired

    JavaMailSender javaMailSender;

    /* @RequestMapping(value="/",method = RequestMethod.GET)
     public String homepage(){
         return "index";
     }
     */
  /* @CrossOrigin
   @RequestMapping(value= "/**", method=RequestMethod.OPTIONS)
    public void corsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
    }
*/
    @RequestMapping(value = "/login", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject FindUser(@RequestParam(value = "Email", defaultValue = "undefined") String email, @RequestParam(value = "Password", defaultValue = "undefined") String password, HttpSession session, ModelMap modelMap) {

        // creating JSONObject

        JSONObject jo = new JSONObject();
        try {
            String Email = userRepository.getemail(email);
            String Password = userRepository.getpassword(email);
            String token;
            Encoder encoder;
            encoder = Base64.getUrlEncoder();

            UUID uuid = UUID.randomUUID();

            // Create byte[] for base64 from uuid
            byte[] src = ByteBuffer.wrap(new byte[16])
                    .putLong(uuid.getMostSignificantBits())
                    .putLong(uuid.getLeastSignificantBits())
                    .array();
            token = encoder.encodeToString(src).substring(0, 22);
            try {
                // Static getInstance method is called with hashing SHA
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // digest() method called
                // to calculate message digest of an input
                // and return array of byte
                byte[] messageDigest = md.digest(password.getBytes());

                // Convert byte array into signum representation
                BigInteger no = new BigInteger(1, messageDigest);

                // Convert message digest into hex value
                String hashtext = no.toString(16);

                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;

                }
                password = hashtext;
                // For specifying wrong message digest algorithms


            } catch (Exception e) {
                out.println("Exception thrown"
                        + " for incorrect algorithm: " + e);
            }


            if (email.equals("undefined") && password.equals("undefined")) {
                jo.put("Status", false);
                jo.put("message", "please enter email & password ");
                return jo;

            } else if (email.equals("undefined")) {
                jo.put("Status", false);
                jo.put("message", "please enter email ");
                return jo;
            } else if (password.equals("undefined")) {
                jo.put("Status", false);
                jo.put("message", "please enter password");
                return jo;
            } else if (email.equals(Email) && password.equals(Password)) {
                try {
                    session.setAttribute("Email", email);
                    userRepository.getUser(email, password, token);
                    JSONArray ja = new JSONArray();
                    Map m = new LinkedHashMap();
                    m.put("Email", email);
                    m.put("password", password);
                    m.put("Token", token);
                    ja.add(m);
                    jo.put("Data", ja);
                    jo.put("Status", true);
                    jo.put("message", "login successfully");
                    return jo;
                } catch (Exception ex) {

                }
            } else {
                jo.put("Status", false);
                jo.put("message", " entered email or password is wrong");
            }


        } catch (Exception e) {
            jo.put("Status", false);
            jo.put("message", " entered email or password is wrong");

        }
        return jo;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject logout(@RequestParam(value = "Email", defaultValue = "undefined") String email, @RequestParam(value = "Token", defaultValue = "undefined") String token) {

        JSONObject jsonObject = new JSONObject();
        if (userRepository.logout(email, token) >= 1) {
            jsonObject.put("status", true);
            jsonObject.put("message", "Logout successfully");
            return jsonObject;
        } else {
            jsonObject.put("status",false );
            jsonObject.put("message", "Logout Failed");
            return jsonObject;
        }

    }

    @RequestMapping(value = "/forgotpassword", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject forgotpassword(@RequestParam(value = "Email", defaultValue = "undefined") String email,@RequestParam(value = "Password", defaultValue = "undefined") String password) {
    JSONObject jsonObject= new JSONObject();
        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;

            }
            password = hashtext;
            // For specifying wrong message digest algorithms


        } catch (Exception e) {
            out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
        }

        if(userRepository.forgotpassword(email,password)>=1){
        jsonObject.put("status",true );
        jsonObject.put("message", "password is changed successfully ");
        return jsonObject;

    }else{
        jsonObject.put("status",false );
        jsonObject.put("message", "password change failed");
        return jsonObject;
    }
    }

        @RequestMapping(value = "/getemail", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject getemail(@RequestParam(value = "Email", defaultValue = "undefined") String email) {
        JSONObject jsonObject= new JSONObject();
        if(userRepository.getemailforforgotpassword(email).isEmpty())
        {
            jsonObject.put("status",false );
            jsonObject.put("message", "user is absent");
            return jsonObject;
        }
        else
        {
            jsonObject.put("status", true);
            jsonObject.put("message", "user is present");
            return jsonObject;
        }
    }


        @RequestMapping(value = "/adduser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject AddUser(@RequestParam(value = "Firstname", defaultValue = "undefined") String firstname,
                              @RequestParam(value = "Lastname", defaultValue = "undefined") String lastname,
                              @RequestParam(value = "Email", defaultValue = "undefined") String email,
                              @RequestParam(value = "Mobile", defaultValue = "undefined") String mobile,
                              @RequestParam(value = "Department", defaultValue = "undefined") String department,
                              @RequestParam(value = "Password", defaultValue = "undefined") String password,
                              @RequestParam(value = "Useremail", defaultValue = "undefined") String Useremail,
                              @RequestParam(value = "token", defaultValue = "undefined") String token

    ) {
        JSONObject jsonObject = new JSONObject();
        String createdby=Useremail;
       String emailPassword=password;

        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;

            }
            password = hashtext;
            // For specifying wrong message digest algorithms


        } catch (Exception e) {
            out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
        }

        String username = "undefined";
        String ALPHA_NUMERIC_STRING = "0123456789";
        int i = 0;
        StringBuilder builder = new StringBuilder();
        while (i < 3) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
            i++;
        }
        username = firstname + builder.toString();

        if (firstname.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter firstname");
            return jsonObject;

        } else if (lastname.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter lastname");
            return jsonObject;

        } else if (email.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter email");
            return jsonObject;

        } else if (password.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter password");
            return jsonObject;

        } else if (mobile.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter mobile");
            return jsonObject;

        } else if (department.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter department");
            return jsonObject;

        } else if (createdby.equals("undefined")) {
            jsonObject.put("status", false);
            jsonObject.put("message", "please enter createdby");
            return jsonObject;

        } else {
            if (userRepository.adduser(firstname, lastname,mobile, email,  department, password, createdby, username, token) == true) {
               /* SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(email);
                msg.setSubject("Tracknovate CRM:Account is Successfully Created.");
                msg.setText("Hi "+(firstname)+",\n Your CRM account is Created.\n\n\n"
                +"!! CREADENTIALS !! \n\n"
                +"\nEmail : "+email
                +"\nPassword : "+emailPassword
                +"\n\n\nTHIS MAIL IS CONFIDENTIAL TO YOU.KINDLY DO NOT SHARE WITH ANYONE"
                +"\n\n© Copyrights by\n"
                +"\nTRACKNOVATE MOBILE RESOURCE MANAGEMENT PVT. LTD.");
                javaMailSender.send(msg);

                */
                jsonObject.put("status", true);
                jsonObject.put("message", "User Added Successfully");
                return jsonObject;
            } else {
                jsonObject.put("status", true);
                jsonObject.put("message", "Something went go wrong");
                return jsonObject;
            }
        }
    }
    @RequestMapping(value = "/deleteuser", method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="localhost:8443")
    @ResponseBody
    public JSONObject DeleteUser(@RequestParam(value="Email" ,defaultValue = "undefined") String email, @RequestParam(value="Useremail" ,defaultValue = "undefined") String useremail,@RequestParam(value="Token" ,defaultValue = "undefined") String token ){
        JSONObject jsonObject = new JSONObject();
        if (userRepository.deleteuser(email,useremail, token)==0) {
            jsonObject.put("status", false);
            jsonObject.put("message", "User is not Exist!");
            return jsonObject;
        } else {
            jsonObject.put("status", true);
            jsonObject.put("message", "User Deleted Successfully!");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getallusers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject getallUsers(@RequestParam(value="Email" ,defaultValue = "undefined") String email, @RequestParam(value="Token" ,defaultValue = "undefined") String token ) {
        JSONObject jsonObject = new JSONObject();
        if (userRepository.getallusers(email, token).isEmpty()) {
            jsonObject.put("status", false);
            jsonObject.put("data", "something went wrong");
            return jsonObject;
        } else {
            jsonObject.put("status", true);
            jsonObject.put("data", userRepository.getallusers(email, token));
            return jsonObject;
        }
    }
    /* GET user login token on ADMIN dashboard */
    @RequestMapping(value = "/getuserlogintoken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject getuserlogintoken(@RequestParam("Email") String email, @RequestParam("Token") String token) {
        JSONObject jsonObject = new JSONObject();
        if (userRepository.getuserlogintoken(email,token).isEmpty()) {
            jsonObject.put("status", false);
            jsonObject.put("data", "something went wrong");
            return jsonObject;
        } else {
            jsonObject.put("status", true);
            jsonObject.put("data", userRepository.getuserlogintoken(email,token));
            return jsonObject;
        }
    }

    /* DELETE user login token FROM ADMIN dashboard */
    @RequestMapping(value = "/deleteuserlogintoken", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject deleteuserlogintoken(@RequestParam(value = "Email", defaultValue = "undefined") String email, @RequestParam(value = "Token", defaultValue = "undefined") String token) {
        JSONObject jsonObject = new JSONObject();
        if (userRepository.deleteuserlogintoken(email, token) >= 1) {
            jsonObject.put("status", true);
            jsonObject.put("message", "DELETE successfully");
            return jsonObject;
        } else {
            jsonObject.put("status", false);
            jsonObject.put("message", "DELETE Failed");
            return jsonObject;
        }
    }

    /* GET User to Update */
    @RequestMapping(value = "/getusertoupdate", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject GetUserToUpdate(@RequestParam(value = "Email", defaultValue = "undefined") String email, @RequestParam(value = "Token", defaultValue = "undefined") String token,@RequestParam(value = "Useremail", defaultValue = "undefined") String useremail) {
        JSONObject jsonObject = new JSONObject();
        if (!userRepository.GetUserToUpdate(useremail).isEmpty()) {
            jsonObject.put("status", true);
            jsonObject.put("data", userRepository.GetUserToUpdate(useremail));
            return jsonObject;
        } else {
            jsonObject.put("status", false);
            jsonObject.put("message", "something went wrong!");
            return jsonObject;
        }
    }

    /*  User to Update */
    @RequestMapping(value = "/UpdateUser", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject UpdateUser(@RequestParam(value = "Email",defaultValue = "undefined") String email, @RequestParam(value = "Token", defaultValue = "undefined") String token,@RequestParam(value = "Useremail", defaultValue = "undefined") String useremail,@RequestParam(value = "Firstname",defaultValue = "undefined") String firstname,@RequestParam(value="Lastname",defaultValue = "undefined") String lastname,@RequestParam(value = "Department",defaultValue = "undefined") String department,@RequestParam(value = "Contact",defaultValue = "undefined") String contact) {
        JSONObject jsonObject = new JSONObject();
        if (userRepository.UpdateUser(email,useremail,firstname,lastname,contact,department)>=1) {
            jsonObject.put("status", true);
            jsonObject.put("message", "user updated successfully");
            return jsonObject;
        } else {
            jsonObject.put("status", false);
            jsonObject.put("message", "user not yet updated!");
            return jsonObject;
        }
    }

    /* total user */
    @RequestMapping(value = "/totaluser", method = RequestMethod.GET, headers = "Accept=application/json")
    @CrossOrigin(origins = "localhost:8443")
    @ResponseBody
    public JSONObject totaluser(@RequestParam(value = "Useremail", defaultValue = "undefined") String email, @RequestParam(value = "Token", defaultValue = "undefined") String token) {
        JSONObject jsonObject = new JSONObject();
        if (!userRepository.totalusers(email,token).isEmpty()) {
            jsonObject.put("status", true);
            jsonObject.put("data", userRepository.totalusers(email,token));
            return jsonObject;
        } else {
            jsonObject.put("status", false);
            jsonObject.put("message", "something went wrong!");
            return jsonObject;
        }
    }


}