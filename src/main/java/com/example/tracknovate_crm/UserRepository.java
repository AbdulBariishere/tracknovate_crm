package com.example.tracknovate_crm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Repository
public class UserRepository extends User {

    @Autowired
    JdbcTemplate template;
User user;
    /* login user */
    public String getUser(String email, String password, String token) {
        Date date = new Date();
        String tostringDate = date.toString();
        String query_check_email="SELECT user_email AS Email from login_token WHERE user_email='"+(email)+"'";
        try {
          List <User> useremail =template.query(query_check_email, new BeanPropertyRowMapper<>(User.class));
            if(useremail.isEmpty())
            {
                String query_insert_email="INSERT INTO login_token (user_email,token,date_time) VALUES ('"+(email)+"','"+(token)+"','"+(tostringDate)+"')";
                template.update(query_insert_email);
            }
            else
            {
                String update_token="UPDATE login_token SET token='"+(token)+"',date_time='"+(tostringDate)+"' WHERE user_email='"+(email)+"'";
                template.update(update_token);
            }


        }catch(Exception e){
            e.getMessage();
        }

        String query = "SELECT crm_user.user_email,crm_user.user_password from crm_user INNER JOIN login_token ON crm_user.user_email=login_token.user_email WHERE crm_user.user_email=? AND crm_user.user_password=?";
        User user = template.queryForObject(query, new Object[]{email, password}, new BeanPropertyRowMapper<>(User.class));
        if (user != null)
            return "login successfullY";
        else
            return "login failed";
    }

    /* logout */
    public int logout(String email, String token) {
        String query = "Delete  FROM  login_token where token = '" + (token) + "'";
        int x = template.update(query);
        if (x != 0) {
            return 1;
        } else
            return 0;
    }

    /* forgot password */
    public int forgotpassword(String email,String password)
    {
        String query="update crm_user set user_password='"+(password)+"' where user_email='"+(email)+"'";
        int x=template.update(query);
        if (x != 0) {
            return 1;
        } else
            return 0;

    }

    /* check email for forget password */
    public List<User> getemailforforgotpassword(String email) {
        String query = "SELECT user_email as Email FROM crm_user WHERE user_email =?";
       List<User> users = template.query(query, new Object[]{email}, new BeanPropertyRowMapper<>(User.class));
        return users;
    }
    /* check email */
    public String getemail(String email) {
        String query = "SELECT user_email as Email FROM crm_user WHERE user_email =?";
        User user = template.queryForObject(query, new Object[]{email}, new BeanPropertyRowMapper<>(User.class));
        return user.getEmail();
    }
    /* check password */
    public String getpassword(String email) {
        String query = "Select user_password as Password From crm_user Where user_email=?";
        User user = template.queryForObject(query, new Object[]{email}, new BeanPropertyRowMapper<>(User.class));
        return user.getPassword();
    }


    /* get all users */
    public List<User> getallusers(String useremail, String token) {
        String query_token = "SELECT user_email,token FROM login_token WHERE user_email='" + (useremail) + "' AND token='" + (token) + "'";
        List<User> list = template.query(query_token, new BeanPropertyRowMapper<>(User.class));

        if (!list.isEmpty()) {
            String query = "select username as Username,user_firstname as Firstname,user_lastname as Lastname,user_mobile as Contact,user_email as Email,user_department as Department, user_createdby as CreatedBy from crm_user WHERE user_email !='"+(useremail)+"'";
            List<User> users = template.query(query, new BeanPropertyRowMapper<>(User.class));
            return users;
        }  else
             return list;
    }

    /* adduser */
    public Boolean adduser(String firstname, String lastname, String mobile, String email, String department, String password, String createdby, String username, String token) {
        String query_token = "SELECT login_token.user_email FROM login_token WHERE token=?";
        User user = template.queryForObject(query_token, new Object[]{token}, new BeanPropertyRowMapper<>(User.class));

        if (!user.equals(null)) {
            String query_emailExist = "SELECT user_email as Email from crm_user WHERE user_email='" + (email) + "'";
       List <User> user_emailExist = template.query(query_emailExist,  new BeanPropertyRowMapper<>(User.class));
            if (user_emailExist.isEmpty()) {
                String query = "INSERT  INTO crm_user(user_firstname," +
                        "user_lastname," +
                        "user_mobile," +
                        "user_email," +
                        "username," +
                        "user_department," +
                        "user_password," +
                        "user_createdby) VALUES(?,?,?,?,?,?,?,?)";
                Date date = new Date();
                String createddate = date.toString();
                String query_createdate = "UPDATE crm_user SET created_date ='" + (date) + "'WHERE username = '" + (username) + "'";
                template.update(query, firstname, lastname, mobile, email, username, department, password, createdby);
                template.update(query_createdate);
                return true;
            } else {
                return false;
            }
        }else
        {
            return false;
        }

    } /* GET user login token FROM ADMIN dashboard */

        public List<User> getuserlogintoken(String email,String token){
            String query="SELECT user_email as Email, token as Token,createdDate as CreatedDate FROM login_token where user_email='"+(email)+"' ";
            List<User> users = template.query(query, new BeanPropertyRowMapper<>(User.class));
            return users;
        }

    /* DELETE user login token FROM ADMIN dashboard */

    public int deleteuserlogintoken(String email,String token){
        String query = "Delete  FROM  login_token where token = '" + (token) + "'";
        int x = template.update(query);
        if (x != 0) {
            return 1;
        } else
            return 0;
    }


    /* get user to update */
    public List<User> GetUserToUpdate(String useremail){
        String query ="Select user_firstname as Firstname,user_lastname as Lastname,user_mobile as Contact,user_department as Department from crm_user where user_email='"+(useremail)+"'";
        List<User> users = template.query(query, new BeanPropertyRowMapper<>(User.class));
        return users;
    }
/* delete user*/
public int deleteuser(String email,String useremail,String password){
    String query="Delete From crm_user where user_email='"+(email)+"'";
    int x = template.update(query);
    if(x!=0)
        return 1;
    else
        return 0;

}
    /* Update User */
    public int UpdateUser(String email, String useremail,String firstname,String lastname,String contact,String department){
        Date date = new Date();
        String query="Update crm_user set user_firstname ='"+(firstname)+"',user_lastname='"+(lastname)+"',user_mobile='"+(contact)+"',user_department='"+(department)+"',modified_date='"+(date.toString())+"',modified_by='"+(email)+"' WHERE user_email='"+(useremail)+"'";
        int x = template.update(query);

        if(x!=0)
            return 1;
        else
            return 0;
    }
    /*total user */
    public List<User> totalusers(String useremail,String token){
        int x=1;
        String query ="select crm_user.user_email AS Email from crm_user where user_email <> '"+(useremail)+"'";
        List<User> users = template.query(query, new BeanPropertyRowMapper<>(User.class));
        return users;
    }
    }


