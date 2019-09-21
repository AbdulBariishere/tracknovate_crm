package com.example.tracknovate_crm;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@CrossOrigin(origins = "localhost:8443")
@Repository
public class LeadRepository extends Lead {
    @Autowired
    JdbcTemplate template;
    Lead lead;
    public int addlead(String subject, String email,
                       String mobile,String smobile, String leadnumber, String status,
                       String source, String createddate, String createdby,
                       String type, String description, String stage,
                       String nextaction,String leadname,String address,String state,String city,String pincode, String token, String useremail) {

        String query_token = "SELECT login_token.user_email,login_token.token FROM login_token WHERE user_email='" + (useremail) + "' AND token='" + (token) + "'";
        List<Lead> list = template.query(query_token, new BeanPropertyRowMapper<>(Lead.class));
        if (!list.isEmpty()) {

            String query = "INSERT  INTO crm_lead(lead_owner," +
                    "lead_subject," + "lead_email," + "lead_contact,"+ "lead_scontact,"   + "lead_status," +
                    "lead_source," + "createddate," + "created_by," + "lead_type," + "lead_description," + "lead_state,"+"lead_city," +"lead_pincode,"+ "lead_stage," +
                    "lead_nextaction,lead_name,lead_address) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            template.update(query, useremail, subject, email, mobile, smobile, status, source, createddate, createdby, type, description,state,city,pincode,stage, nextaction,leadname,address);
            String query_select_lead_id="select lead_id AS LeadId from crm_lead order by lead_id desc limit 1";
            Lead SelectLeadId = template.queryForObject(query_select_lead_id, new BeanPropertyRowMapper<>(Lead.class));
            leadnumber="lead"+SelectLeadId.getLeadId();
            String query_update_leadId="UPDATE crm_lead SET lead_number='"+(leadnumber)+"' where lead_id='"+(SelectLeadId.getLeadId())+"'";
            template.update(query_update_leadId);
            try {
                String query_insert = "INSERT INTO crm_leadlog(leadlog_owner,leadlog_subject,leadlog_email,leadlog_contact," +
                        "leadlog_scontact,leadlog_status,leadlog_source,leadlog_createddate,leadlog_createdby,leadlog_description,leadlog_modifieddate," +
                        "leadlog_modifiedby,leadlog_address,leadlog_number,leadlog_nextaction,leadlog_stage,leadlog_state,leadlog_city,leadlog_pincode,leadlog_type) " +
                        "VALUES" +
                        "((select lead_owner from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_subject from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_email from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_contact from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_scontact from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_status from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_source from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select createddate from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select created_by from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_description from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select modified_date from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select modified_by from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_address from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_number from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_nextaction from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_stage from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_city from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_state from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_pincode from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_type from crm_lead where lead_number='" + (leadnumber) + "'))";
                template.update(query_insert);

            } catch (SQLWarningException ex) {
                ex.getMessage();
                System.out.println(ex.getMessage());
            }
            return 1;
        } else {
            return 0;
        }
    }

    /* check mobile numbet  */
    public List<Lead> checkmobile(String mobile) {
        String query = "SELECT lead_name as Name FROM crm_lead WHERE lead_contact ='"+(mobile)+"' OR lead_scontact='"+(mobile)+"'";
        List<Lead> leads = template.query(query,new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* delete user*/
    public int deletelead(String leadnumber,String useremail,String password){
        String query="Delete From crm_lead where lead_number='"+(leadnumber)+"'";
        int x = template.update(query);
        if(x!=0)
            return 1;
        else
            return 0;

    }

    /*get all leads */


    public List<Lead> getallleads(String useremail, String token) {
        String query_token = "SELECT user_email,token FROM login_token WHERE user_email='" + (useremail) + "' AND token='" + (token) + "'";
        List<Lead> list = template.query(query_token, new BeanPropertyRowMapper<>(Lead.class));

        if (!list.isEmpty()) {
            String query = "select lead_owner as Owner, lead_number as Number,lead_subject as Subject,lead_stage as Stage,lead_type as Type,lead_description AS Description,lead_status as Status, lead_source as Source,lead_name as Name,lead_address as Address,lead_state as State,lead_city as City, lead_nextaction as NextActionDate,modified_date as ModifiedDate,createddate as CreatedDate,lead_contact as Mobile from crm_lead ORDER BY lead_id desc";
            List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
            return leads;
        } else
            return list;
    }


    /* get all leads for specific user  */
    public List<Lead> getleadforuser(String leadowner, String useremail, String token) {
        String query_token = "SELECT login_token.user_email,login_token.token FROM login_token WHERE user_email='" + (useremail) + "' AND token='" + (token) + "'";
        List<Lead> list = template.query(query_token, new BeanPropertyRowMapper<>(Lead.class));
        if (!list.isEmpty()) {
            String query = "select lead_number as Number,lead_subject as Subject,lead_stage as Stage,lead_type as Type,lead_description as Description,lead_status as Status,lead_source as Source,lead_name as Name,lead_address as Address,lead_state as State,lead_city as City, lead_nextaction as NextActionDate, modified_date as ModifiedDate,lead_contact as Mobile,lead_pincode as Pincode from crm_lead  WHERE lead_owner=?  ORDER BY lead_id DESC ";
            List<Lead> leads = template.query(query, new Object[]{leadowner}, new BeanPropertyRowMapper<>(Lead.class));
            return leads;
        } else
            return list;
    }
    /* today modification for all */
    public List<Lead> todaymodificationforall( String useremail, String token) {
        String query_token = "SELECT login_token.user_email,login_token.token FROM login_token WHERE user_email='" + (useremail) + "' AND token='" + (token) + "'";
        List<Lead> list = template.query(query_token, new BeanPropertyRowMapper<>(Lead.class));
        if (!list.isEmpty()) {
            Date date = new Date();
            String modifieddate = "";
            DateFormat format = new SimpleDateFormat("M/dd/yyyy");
            modifieddate = format.format(date);
            String query = "select lead_owner as Owner,lead_number as Number,lead_subject as Subject,lead_stage as Stage,lead_type as Type,lead_description as Description,lead_status as Status,lead_source as Source,lead_name as Name,lead_address as Address,lead_state as State,lead_city as City, lead_nextaction as NextActionDate, modified_date as ModifiedDate,lead_contact as Mobile,lead_pincode as Pincode,createddate as CreatedDate from crm_lead  WHERE  modified_date='"+(modifieddate)+"' ORDER BY lead_id DESC ";
            List<Lead> leads = template.query(query,  new BeanPropertyRowMapper<>(Lead.class));
            return leads;
        } else
            return list;
    }
    /*last inserted row */
    public List<Lead> lastinsertedrow(String useremail, String token){
        String query="SELECT lead_number as Number ,lead_owner as Owner,lead_contact as Mobile,lead_stage as Stage FROM crm_lead ORDER BY lead_id DESC LIMIT 1";
        List<Lead> leads = template.query(query,  new BeanPropertyRowMapper<>(Lead.class));

        return leads;
    }
    /*last inserted row in leadlogtable */
    public List<Lead> lastinsertedrowinleadlog(String useremail, String token){
        String query="SELECT leadlog_number as Number ,leadlog_owner as Owner,leadlog_contact as Mobile,leadlog_stage as Stage FROM crm_leadlog ORDER BY leadlog_id DESC LIMIT 1";
        List<Lead> leads = template.query(query,  new BeanPropertyRowMapper<>(Lead.class));

        return leads;
    }

    /* today modification for user*/
public List<Lead> todaymodificationforuser(String leadowner, String useremail, String token) {
    String query_token = "SELECT login_token.user_email,login_token.token FROM login_token WHERE user_email='" + (useremail) + "' AND token='" + (token) + "'";
    List<Lead> list = template.query(query_token, new BeanPropertyRowMapper<>(Lead.class));
    if (!list.isEmpty()) {
        Date date = new Date();
        String modifieddate = "";
        DateFormat format = new SimpleDateFormat("M/dd/yyyy");
        modifieddate = format.format(date);
        String query = "select lead_number as Number,lead_subject as Subject,lead_stage as Stage,lead_type as Type,lead_description as Description,lead_status as Status,lead_source as Source,lead_name as Name,lead_address as Address,lead_state as State,lead_city as City, lead_nextaction as NextActionDate, modified_date as ModifiedDate,lead_contact as Mobile,lead_pincode as Pincode from crm_lead  WHERE lead_owner=? AND modified_date='"+(modifieddate)+"' ORDER BY lead_id DESC ";
        List<Lead> leads = template.query(query, new Object[]{leadowner}, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    } else
        return list;
}

    /* get all leads for specific  Lead */
    public List<Lead> getleadbynumber(String number) {
        String query = "SELECT lead_owner as Owner,lead_subject as Subject,lead_stage as Stage,lead_type as Type,lead_state as State,lead_city as City,lead_description as Description  FROM crm_lead WHERE lead_number =?";
        List<Lead> leads = template.query(query, new Object[]{number}, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* getupdatelead */
    public Lead getleadtoupdate(String number) {
        String query = "SELECT lead_email as Email,lead_contact as Mobile,lead_status as Status,lead_source as Source,lead_stage as Stage,lead_type as Type FROM crm_lead WHERE lead_number =?";
        Lead lead = template.queryForObject(query, new Object[]{number}, new BeanPropertyRowMapper<>(Lead.class));
        return lead;
    }


    /*get single lead for specific lead number on click of edit lead */

    public Lead getleadforspecificleadnumber(String number, String email, String token) {
        String query = "SELECT lead_name as Name,lead_subject as Subject,lead_email as Email,lead_contact as Mobile,lead_scontact as Smobile,lead_pincode as Pincode, lead_address as Address, lead_status as Status,lead_source as Source,lead_stage as Stage,lead_type as Type,lead_description As Description,lead_state as State,lead_city as City,lead_nextaction as NextActionDate  FROM crm_lead WHERE lead_number =?";
        Lead leads = template.queryForObject(query, new Object[]{number}, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* Update status ,source ,type, stage, stage,next action date modified date, modified by according to lead number  */

    public String updatelead(String name,String subject,String email,String contact,String scontact,String address,String status, String stage, String source, String type,String description,String pincode,String state,String city, String modifieddate, String modifiedby, String nextaction, String leadnumber, String useremail, String token) {
        String query_token = "SELECT user_email,token FROM login_token WHERE user_email='" + (useremail) + "' AND token='" + (token) + "'";
        List<Lead> list = template.query(query_token, new BeanPropertyRowMapper<>(Lead.class));

        if (!list.isEmpty()) {
            String query_update = "UPDATE crm_lead set lead_name=?,lead_subject=?,lead_email=?,lead_contact=?,lead_scontact=?,lead_address=?, lead_status=?,lead_stage=?,lead_source=?,lead_type=?,lead_description=?,lead_pincode=?,lead_state=?,lead_city=?,modified_date=?, lead_nextaction=? where lead_number=?";
            template.update(query_update,name,subject,email,contact,scontact,address,status,stage,source,type,description,pincode,state,city, modifieddate, nextaction, leadnumber);
            String query_updateModifyby = "UPDATE crm_lead set modified_by='" + (useremail) + "'";
            template.update(query_updateModifyby);
            try {
                String query_insert = "INSERT INTO crm_leadlog(leadlog_owner,leadlog_subject,leadlog_email,leadlog_contact," +
                        "leadlog_scontact,leadlog_status,leadlog_source,leadlog_createddate,leadlog_createdby,leadlog_description,leadlog_modifieddate," +
                        "leadlog_modifiedby,leadlog_address,leadlog_number,leadlog_nextaction,leadlog_stage,leadlog_pincode,leadlog_city,leadlog_state,leadlog_type) " +
                        "VALUES" +
                        " ((select lead_owner from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_subject from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_email from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_contact from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_scontact from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_status from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_source from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select createddate from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select created_by from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_description from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select modified_date from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select modified_by from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_address from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_number from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_nextaction from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_stage from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_pincode from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_city from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_state from crm_lead where lead_number='" + (leadnumber) + "')," +
                        "(select lead_type from crm_lead where lead_number='" + (leadnumber) + "'))";
                template.update(query_insert);
            } catch (SQLWarningException ex) {
                ex.getMessage();
                System.out.println(ex.getMessage());
            }
            return "updated successfully";

        } else
            return null;
    }

    /* introduction check */
    public int introductioncheck(String email,String contact,String stage,String leadnumber)
    {
        int count=0;
        if(stage.equals("introduction"))
        {
            String query_introduction_count="UPDATE crm_lead set introduction_count=introduction_count+1 WHERE lead_number='"+(leadnumber)+"'";
            template.update(query_introduction_count);
            String query_count="Select introduction_count  from crm_lead where lead_number='"+(leadnumber)+"'";
            count=template.queryForObject(query_count, Integer.class);

        }
        if(count==1)
            return 1;
        else
            return 0;

    }
    /*eligiblity check */
    public int eligiblitycheck(String email,String contact,String stage,String leadnumber)
    {
        int count=0;
        if(stage.equals("eligibility"))
        {
            String query_eligiblity_count="UPDATE crm_lead set eligibility_count=eligibility_count+1 WHERE lead_number='"+(leadnumber)+"'";
            template.update(query_eligiblity_count);
            String query_count="Select eligibility_count  from crm_lead where lead_number='"+(leadnumber)+"'";
            count=template.queryForObject(query_count, Integer.class);

        }
        if(count==1)
            return 1;
        else
            return 0;

    }
/* approval check */

    public int approvalcheck(String email,String contact,String stage,String leadnumber)
    {
        int count=0;
        if(stage.equals("approval"))
        {
            String query_approval_count="UPDATE crm_lead set approval_count=approval_count+1 WHERE lead_number='"+(leadnumber)+"'";
            template.update(query_approval_count);
            String query_count="Select approval_count  from crm_lead where lead_number='"+(leadnumber)+"'";
            count=template.queryForObject(query_count, Integer.class);

        }
        if(count==1)
            return 1;
        else
            return 0;

    }


    /* today's task for admin */
    public List<Lead> todaystaskforadmin() {
        Date date = new Date();
        String nextactiondate = "";
        try {
            DateFormat format = new SimpleDateFormat("M/d/yyyy");
            nextactiondate = format.format(date);


        } catch (Exception ex) {
            ex.getMessage();
        }
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.lead_number as Number,crm_lead.lead_name as Name,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.modified_date as ModifiedDate,crm_lead.lead_nextaction as NextActionDate,crm_lead.lead_address as Address,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description,createddate as CreatedDate FROM crm_lead WHERE crm_lead.lead_nextaction ='"+(nextactiondate)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;


    }

    /* todays task for specific user */


    public List<Lead> todaystaskforuser(String leadowner, String email, String token) {
        Date date = new Date();
        String nextactiondate;
        try {
            DateFormat format = new SimpleDateFormat("M/d/yyyy");
            nextactiondate = format.format(date);
            String query = "SELECT crm_lead.lead_number as Number,crm_lead.lead_name as Name,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.modified_date as ModifiedDate,crm_lead.lead_nextaction as NextActionDate,crm_lead.lead_address as Address,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE crm_lead.lead_nextaction ='"+(nextactiondate)+"'AND crm_lead.lead_owner='" + (leadowner) + "'";
            List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
            return leads;
        } catch (Exception ex) {
            ex.getMessage();
            return null;
        }

    }

    /* GET LEAD LOG FOR USER */

    public List<Lead> getleadlogforuser(String email, String token, String leadnumber) {
        String query = "SELECT crm_leadlog.leadlog_subject AS Subject,crm_leadlog.leadlog_status AS Status,crm_leadlog.leadlog_stage AS Stage,crm_leadlog.leadlog_type AS Type,crm_leadlog.leadlog_description AS Description,crm_leadlog.leadlog_createddate as CreatedDate, crm_leadlog.leadlog_modifieddate AS ModifiedDate,crm_leadlog.leadlog_nextaction AS NextActionDate FROM crm_leadlog WHERE leadlog_number='" + (leadnumber) + "' AND leadlog_owner='" + (email) + "'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /* GET LEAD LOG FOR ADMIN */


    public List<Lead> getleadlogforadmin(String email, String token, String leadnumber) {
        String query = "SELECT crm_leadlog.leadlog_owner AS Owner,crm_leadlog.leadlog_subject AS Subject,crm_leadlog.leadlog_status AS Status,crm_leadlog.leadlog_stage AS Stage,crm_leadlog.leadlog_type AS Type,crm_leadlog.leadlog_description AS Description,crm_leadlog.leadlog_createddate AS CreatedDate,crm_leadlog.leadlog_modifieddate as ModifiedDate,crm_leadlog.leadlog_nextaction AS NextActionDate FROM crm_leadlog WHERE leadlog_number='" + (leadnumber) + "'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* Get open Leads for user */

    public List<Lead> openleadforuser(String useremail,String token,String leadowner){
        String query = "SELECT crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE crm_lead.lead_status ='open'AND crm_lead.lead_owner='" + (leadowner) + "' ORDER BY lead_id DESC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /* Get open Leads for all */

    public List<Lead> openleadforall(String useremail,String token){
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.createddate as CreatedDate,crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE crm_lead.lead_status ='open' ORDER BY lead_id DESC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* Get close Leads with success for user */

    public List<Lead> closeleadwithsuccessforuser(String useremail,String token,String leadowner){
        String query = "SELECT crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE crm_lead.lead_status ='closewithsuccess'  AND crm_lead.lead_owner='" + (leadowner) + "' ORDER BY lead_id DESC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* Get close Leads with success for all */

    public List<Lead> closeleadwithsuccessforall(String useremail,String token){
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.createddate as CreatedDate,crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE crm_lead.lead_status ='closewithsuccess' ORDER BY lead_id DESC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }


    /* Get close Leads without success for user */

    public List<Lead> closeleadwithoutsuccessforuser(String useremail,String token,String leadowner){
        String query = "SELECT crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE  crm_lead.lead_status ='closewithoutsuccess' AND crm_lead.lead_owner='" + (leadowner) + "' ORDER BY lead_id DESC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /* Get close Leads without success for all */

    public List<Lead> closeleadwithoutsuccessforall(String useremail,String token){
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.createddate as CreatedDate,crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE  crm_lead.lead_status ='closewithoutsuccess' ORDER BY lead_id DESC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /*Get pending leads for all */
    public List<Lead> pendingleadforall(String useremail,String token){
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate," +
                "crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City," +
                "crm_lead.lead_description as Description,crm_lead.createddate  FROM crm_lead" +
                " WHERE (crm_lead.lead_status='open' AND STR_TO_DATE(crm_lead.modified_date, '%m/%d/%Y') < CURDATE() AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE()) OR (crm_lead.lead_status='open' AND crm_lead.modified_date IS NULL AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE()) " +
                "ORDER BY crm_lead.lead_id DESC";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /*Get pending leads for user */
    public List<Lead> pendingleadforuser(String useremail,String token,String leadowner){
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate," +
                "crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City," +
                "crm_lead.lead_description as Description  FROM crm_lead" +
                " WHERE (((crm_lead.lead_status='open' AND STR_TO_DATE(crm_lead.modified_date, '%m/%d/%Y') < CURDATE() AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE()))OR (crm_lead.lead_status='open' AND crm_lead.modified_date IS NULL AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE())) AND (crm_lead.lead_owner='"+(leadowner)+"') " +
                "ORDER BY crm_lead.lead_id DESC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /* total leads count*/
    public List<Lead> totalleadcount(String useremail,String Token){
        String query="SELECT count(*) AS Count from crm_lead ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /* total leads count for user */
    public List<Lead> totalleadcountforuser(String useremail,String Token,String leadowner){
        String query="SELECT count(*) AS Count from crm_lead where lead_owner='"+(leadowner)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /*today's task count */
    public List<Lead> todaysleadcount(String useremail,String Token){
        Date date = new Date();
        String nextactiondate = "";
        try {
            DateFormat format = new SimpleDateFormat("M/d/yyyy");
            nextactiondate = format.format(date);
        } catch (Exception ex) {
            ex.getMessage();
        }
        String query="SELECT count(*) AS Count from crm_lead where lead_nextaction='"+(nextactiondate)+"' ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /*today's task count for user */
    public List<Lead> todaysleadcountforuser(String useremail,String Token,String leadowner){
        Date date = new Date();
        String nextactiondate = "";
        try {
            DateFormat format = new SimpleDateFormat("M/d/yyyy");
            nextactiondate = format.format(date);
        } catch (Exception ex) {
            ex.getMessage();
        }
        String query="SELECT count(*) AS Count from crm_lead where lead_nextaction='"+(nextactiondate)+"'  AND lead_owner='"+(leadowner)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }


    /* today's modification count */
    public List<Lead> todaysmodificationleadcount(String useremail,String Token){
        Date date = new Date();
        String modifieddate = "";
        try {
            DateFormat format = new SimpleDateFormat("M/dd/yyyy");
            modifieddate = format.format(date);
        } catch (Exception ex) {
            ex.getMessage();
        }
        String query="SELECT count(*) AS Count from crm_lead where crm_lead.modified_date='"+(modifieddate)+"' ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /* today's modification count for user */
    public List<Lead> todaysmodificationleadcountforuser(String useremail,String Token,String leadowner){
        Date date = new Date();
        String modifieddate = "";
        try {
            DateFormat format = new SimpleDateFormat("M/dd/yyyy");
            modifieddate = format.format(date);
        } catch (Exception ex) {
            ex.getMessage();
        }
        String query="SELECT count(*) AS Count from crm_lead where crm_lead.modified_date='"+(modifieddate)+"' and lead_owner='"+(leadowner)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }


    /*open status count  */
    public List<Lead> totalopencount(String useremail,String Token){
        String query="SELECT count(*) AS Count from crm_lead WHERE crm_lead.lead_status='open'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /*open status count for user   */
    public List<Lead> totalopencountforuser(String useremail,String Token,String leadowner){
        String query="SELECT count(*) AS Count from crm_lead WHERE crm_lead.lead_status='open' AND crm_lead.lead_owner='"+(leadowner)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /*close with success count */
    public List<Lead> totalclosewithsuccesscount(String useremail,String Token){
        String query="SELECT count(*) AS Count from crm_lead WHERE crm_lead.lead_status='closewithsuccess'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }
    /*close with success count for user */
    public List<Lead> totalclosewithsuccesscountforuser(String useremail,String Token,String leadowner){
        String query="SELECT count(*) AS Count from crm_lead WHERE crm_lead.lead_status='closewithsuccess' AND lead_owner='"+(leadowner)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }


    /*close without success count */
    public List<Lead> totalclosewithoutsuccesscount(String useremail,String Token){
        String query="SELECT count(*) AS Count from crm_lead WHERE crm_lead.lead_status='closewithoutsuccess'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /*close without success count for user */
    public List<Lead> totalclosewithoutsuccesscountforuser(String useremail,String Token,String leadowner ){
        String query="SELECT count(*) AS Count from crm_lead WHERE crm_lead.lead_status='closewithoutsuccess' AND lead_owner='"+(leadowner)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }


    /* pending leads count */
    public List<Lead> pendingleadcount(String useremail,String Token){
        String query="SELECT count(*) AS Count from crm_lead WHERE (crm_lead.lead_status='open' AND STR_TO_DATE(crm_lead.modified_date, '%m/%d/%Y') < CURDATE() AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE()) OR (crm_lead.lead_status='open' AND crm_lead.modified_date IS NULL AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE())";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* pending leads count for user */
    public List<Lead> pendingleadcountforuser(String useremail,String Token,String leadowner){
        String query="SELECT count(*) AS Count from crm_lead WHERE ((crm_lead.lead_status='open' AND STR_TO_DATE(crm_lead.modified_date, '%m/%d/%Y') < CURDATE() AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE()) OR (crm_lead.lead_status='open' AND crm_lead.modified_date IS NULL AND STR_TO_DATE(crm_lead.lead_nextaction, '%m/%d/%Y') < CURDATE())) AND lead_owner='"+(leadowner)+"'";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;
    }

    /* REPORTS */
    /* Total Leads  for all*/
    public List<Lead> ReportTotalLeads(String Useremail,String Token,String fromDate, String toDate){
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.createddate as CreatedDate,crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE crm_lead.createddate BETWEEN  '"+(fromDate)+"' AND '"+(toDate)+"' ORDER BY createddate ASC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;

    }

    /* Total Leads  for user*/
    public List<Lead> ReportTotalLeadsforuser(String Useremail,String Token,String fromDate, String toDate,String leadowner){
        String query = "SELECT crm_lead.lead_owner as Owner,crm_lead.createddate as CreatedDate,crm_lead.lead_number as Number,crm_lead.lead_subject as Subject,crm_lead.lead_email as Email,crm_lead.lead_stage as Stage,crm_lead.lead_type as Type,crm_lead.lead_address AS Address,crm_lead.modified_date AS ModifiedDate,crm_lead.lead_nextaction AS NextActionDate,crm_lead.lead_name AS Name, crm_lead.lead_contact as Mobile,crm_lead.lead_status as Status,crm_lead.lead_source as Source,crm_lead.lead_state as State,crm_lead.lead_city as City,crm_lead.lead_description as Description  FROM crm_lead WHERE (crm_lead.createddate BETWEEN  '"+(fromDate)+"' AND '"+(toDate)+"') AND (lead_owner='"+(leadowner)+"') ORDER BY createddate ASC ";
        List<Lead> leads = template.query(query, new BeanPropertyRowMapper<>(Lead.class));
        return leads;

    }


    public String importexcel(MultipartFile file) {
   InputStream input = null;
    try {
        input = file.getInputStream();
    } catch (IOException e) {
        e.printStackTrace();
    }

    XSSFWorkbook wb = null;
    try {
        wb = new XSSFWorkbook(input);
    } catch (IOException e) {
        e.printStackTrace();
    }
    XSSFSheet sheet = wb.getSheetAt(0);
    Iterator rows = sheet.rowIterator();
    int k=sheet.getLastRowNum();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {

        XSSFRow XSSFRow = sheet.getRow(i);
        DataFormatter formatter = new DataFormatter();
        String lead_owner = formatter.formatCellValue(sheet.getRow(i).getCell(0));
        String lead_subject = formatter.formatCellValue(sheet.getRow(i).getCell(1));
        String lead_email = formatter.formatCellValue(sheet.getRow(i).getCell(2));
        String lead_contact = formatter.formatCellValue(sheet.getRow(i).getCell(3));
        String lead_status = formatter.formatCellValue(sheet.getRow(i).getCell(4));
        String lead_source = formatter.formatCellValue(sheet.getRow(i).getCell(5));
        String createddate = formatter.formatCellValue(sheet.getRow(i).getCell(6));
        String created_by = formatter.formatCellValue(sheet.getRow(i).getCell(7));
        String lead_description = formatter.formatCellValue(sheet.getRow(i).getCell(8));
        String modified_date = formatter.formatCellValue(sheet.getRow(i).getCell(9));
        String modified_by = formatter.formatCellValue(sheet.getRow(i).getCell(10));
        String lead_address = formatter.formatCellValue(sheet.getRow(i).getCell(11));
        String lead_number = formatter.formatCellValue(sheet.getRow(i).getCell(12));
        String lead_nextactiondate = formatter.formatCellValue(sheet.getRow(i).getCell(13));
        String lead_stage = formatter.formatCellValue(sheet.getRow(i).getCell(14));
        String lead_type = formatter.formatCellValue(sheet.getRow(i).getCell(15));
        String lead_name = formatter.formatCellValue(sheet.getRow(i).getCell(16));
        String lead_city = formatter.formatCellValue(sheet.getRow(i).getCell(17));
        String lead_state = formatter.formatCellValue(sheet.getRow(i).getCell(18));


        String sql = "INSERT INTO crm_lead(lead_owner,lead_subject,lead_email,lead_contact,lead_status,lead_source,createddate,created_by,lead_description,modified_date" +
                    "modified_by,lead_address,lead_number,lead_nextactiondate,lead_stage,lead_type,lead_name,lead_city,lead_state) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        template.update(sql,lead_owner,lead_subject,lead_email,lead_contact,lead_status,lead_source,createddate,created_by,lead_description,modified_date,
                modified_by,lead_address,lead_number,lead_nextactiondate,lead_stage,lead_type,lead_name,lead_city,lead_state);
        System.out.println("Import rows " + i);
    }
    while (rows.hasNext()) {
        XSSFRow row = (XSSFRow) rows.next();
        Iterator cells = row.cellIterator();
        while (cells.hasNext()) {
            XSSFCell cell = (XSSFCell) cells.next();
        }

        System.out.println("Success import excel to mysql table");

    }
    return "true";
}

}