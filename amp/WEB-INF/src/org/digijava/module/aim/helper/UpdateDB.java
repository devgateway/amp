package org.digijava.module.aim.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;


public class UpdateDB {
	
	private static String driver = "";
	private static String url = "";
	private static String username = "";
	private static String passwd = "";
	
	private static Logger logger = Logger.getLogger(UpdateDB.class);
	
	
	public static int updateReportCache(Long ampActivityId) throws Exception {

		logger.info("In Update Report Cache");

		Connection con = null;
		Session session = null;
		try {
			
			//setConnectionProps();
			
			//logger.debug("JDBC Driver :" + driver);
			//logger.debug("URL :" + url);
			//logger.debug("Username :" + username);
			//logger.debug("Password :" + passwd);
			
			//Class.forName(driver).newInstance();

			//con = DriverManager.getConnection(url, username, passwd);
			session = PersistenceManager.getSession();
			con = ((SessionImplementor)session).connection();

			Statement stmt = con.createStatement();
			ResultSet rs = null;
			
			// Deletes all the exisiting records for ampActivityId in amp_report_cache.
			String sql = "delete from amp_report_cache where amp_activity_id='" + ampActivityId + "'";
			
			// Query #1
			// This script inserts details of activities having funding details entered from Donor perspective.
			long t1 = System.currentTimeMillis();			
			stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			logger.debug("Query #1 executed in " + (t2-t1) + "ms");
			

			sql = "insert into amp_report_cache ";
			sql += " select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name',amp_funding.amp_modality_id 'amp_modality_id',amp_modality.name 'modality_name',";
			sql += " amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name',";
			sql += " amp_terms_assist.terms_assist_name,amp_organisation.name 'donor_name',amp_organisation.amp_org_id 'amp_donor_id',amp_organisation.org_type 'org_type',amp_funding.amp_funding_id,";
			sql += " case when transaction_type='0' and adjustment_type='0' and org_role_code='DN' then transaction_amount else 0 end 'planned_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='0' and org_role_code='DN' then transaction_amount else 0 end 'planned_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='0' and org_role_code='DN' then transaction_amount else 0 end 'planned_expenditure',";
			sql += " case when transaction_type='0' and adjustment_type='1' and org_role_code='DN' then transaction_amount else 0 end 'actual_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='1' and org_role_code='DN' then transaction_amount else 0 end 'actual_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='1' and org_role_code='DN' then transaction_amount else 0 end 'actual_expenditure',";
			sql += " currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.actual_completion_date,extract(YEAR from transaction_date) 'fiscal_year',";
			sql += " case when extract(MONTH from transaction_date) between '1' and '3' then '1'";
			sql += " when extract(MONTH from transaction_date) between '4' and '6' then '2'";
			sql += " when extract(MONTH from transaction_date) between '7' and '9' then '3'";
			sql += " when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter',org_role_code 'perspective',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description,NULL,NULL,NULL,NULL,1 ";
			sql += " from amp_activity,amp_currency,amp_modality,amp_terms_assist,amp_funding,amp_organisation,amp_status,amp_funding_detail,amp_level ";
			sql += " where amp_funding.amp_modality_id=amp_modality.amp_modality_id ";
			sql += " and amp_activity.amp_activity_id=amp_funding.amp_activity_id and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += " and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += " and amp_activity.amp_status_id =amp_status.amp_status_id ";
			sql += " and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += " and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "'";
			sql += " and amp_activity.amp_level_id=amp_level.amp_level_id and org_role_code='DN'";

			logger.debug(sql);
			// Query #2
			// This script inserts details of activities having funding details entered from MOFED perspective.
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #2 executed in " + (t2-t1) + "ms");			
			

			sql = "insert into amp_report_cache ";
			sql += " select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name',amp_funding.amp_modality_id 'amp_modality_id',amp_modality.name 'modality_name',";
			sql += " amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name',";
			sql += " amp_terms_assist.terms_assist_name,amp_organisation.name 'donor_name',amp_organisation.amp_org_id 'amp_donor_id',amp_organisation.org_type 'org_type',amp_funding.amp_funding_id,";
			sql += " case when transaction_type='0' and adjustment_type='0' and org_role_code='MA' then transaction_amount else 0 end 'planned_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='0' and org_role_code='MA' then transaction_amount else 0 end 'planned_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='0' and org_role_code='MA' then transaction_amount else 0 end 'planned_expenditure',";
			sql += " case when transaction_type='0' and adjustment_type='1' and org_role_code='MA' then transaction_amount else 0 end 'actual_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='1' and org_role_code='MA' then transaction_amount else 0 end 'actual_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='1' and org_role_code='MA' then transaction_amount else 0 end 'actual_expenditure',";
			sql += " currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.actual_completion_date,extract(YEAR from transaction_date) 'fiscal_year',";
			sql += " case when extract(MONTH from transaction_date) between '1' and '3' then '1'";
			sql += " when extract(MONTH from transaction_date) between '4' and '6' then '2'";
			sql += " when extract(MONTH from transaction_date) between '7' and '9' then '3'";
			sql += " when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter',org_role_code 'perspective',amp_team_id ,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description,NULL,NULL,NULL,NULL,1 ";
			sql += " from amp_activity,amp_currency,amp_modality,amp_terms_assist,amp_funding,amp_organisation,amp_status,amp_funding_detail,amp_level ";
			sql += " where amp_funding.amp_modality_id=amp_modality.amp_modality_id ";
			sql += " and amp_activity.amp_activity_id=amp_funding.amp_activity_id and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += " and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += " and amp_activity.amp_status_id =amp_status.amp_status_id ";
			sql += " and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += " and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "'";
			sql += " and amp_activity.amp_level_id=amp_level.amp_level_id and org_role_code='MA'";
			
			logger.debug(sql);
			// Query #3
			// This script inserts details of activities having funding organisation but no funding details.
			t1 = System.currentTimeMillis();			
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #3 executed in " + (t2-t1) + "ms");			

			
			sql = "insert into amp_report_cache ";
			sql += "select NULL,amp_activity.amp_activity_id,amp_activity.amp_id,amp_activity.name,NULL,NULL, ";
			sql += "amp_activity.amp_status_id,amp_status.name,NULL,amp_organisation.name,amp_org_role.organisation,amp_organisation.org_type,NULL,";
			sql += "0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',";
			sql += "0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure',";
			sql += "NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,";
			sql += "amp_activity.actual_completion_date,NULL 'fiscal_year',";
			sql += "NULL 'fiscal_quarter','MA',amp_activity.amp_team_id,NULL,amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' ";
			sql += "when amp_activity.amp_level_id='2' then 'NATIONAL' ";
			sql += "when amp_activity.amp_level_id='3' then 'BOTH' else 'Not Exist' end,amp_activity.description,NULL,NULL,NULL,NULL,1 ";
			sql += "'level_name' from amp_activity LEFT JOIN amp_report_cache ON ";
			sql += "amp_activity.amp_activity_id=amp_report_cache.amp_activity_id,amp_org_role,amp_organisation,amp_status ";
			sql += "where amp_report_cache.amp_activity_id is null ";
			//sql += "and amp_role_id='1' and amp_activity.amp_activity_id=amp_org_role.activity ";
			sql += "and amp_org_role.role='1' and amp_activity.amp_activity_id=amp_org_role.activity ";
			sql += "and amp_org_role.organisation=amp_organisation.amp_org_id ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id";
			
			logger.debug(sql);
			// Query #4
			// This script inserts details of activities with no funding information.
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #4 executed in " + (t2-t1) + "ms");			

			sql = "insert into amp_report_cache ";
			sql += "select distinct NULL,";
			sql += "amp_activity.amp_activity_id,amp_id,amp_activity.name,NULL,NULL, ";
			sql += "amp_activity.amp_status_id,amp_status.name ";
			sql += "'status_name',NULL,NULL,NULL,NULL,NULL,";
			sql += "0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',";
			sql += "0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure',";
			sql += "NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.actual_completion_date,NULL 'fiscal_year',";
			sql += "NULL 'fiscal_quarter','MA',amp_team_id,NULL,amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' when amp_activity.amp_level_id='2' then 'NATIONAL' when amp_activity.amp_level_id='3' then 'BOTH' else 'Unspecified' end 'level_name',amp_activity.description,NULL,NULL,NULL,NULL,1 ";
			sql += "from amp_activity LEFT JOIN amp_funding ON ";
			sql += "amp_activity.amp_activity_id=amp_funding.amp_activity_id,amp_status ";
			sql += "where amp_funding.amp_funding_id is null and amp_activity.amp_activity_id='" + ampActivityId + "' ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id";

			logger.debug(sql);
			// Query #5
			// This script inserts details of activities having funding details but with funding instrument or implementation level null 
			// entered from donor perspective.
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #5 executed in " + (t2-t1) + "ms");			
			
			sql = "insert into amp_report_cache select ";
			sql += "NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name,amp_funding.amp_modality_id,case ";
			sql += "when amp_funding.amp_modality_id='1' then 'Direct Budget Support' ";
			sql += "when amp_funding.amp_modality_id='2' then 'Pool Fund' ";
			sql += "when amp_funding.amp_modality_id='3' then 'Project Support' ";
			sql += "when amp_funding.amp_modality_id='4' then 'Food Aid' ";
			sql += "when amp_funding.amp_modality_id='5' then 'Technical Assistance' when amp_funding.amp_modality_id='6' then 'Program Support' else 'Not Exist' end ";
			sql += "'modality_name',";
			sql += "amp_activity.amp_status_id,amp_status.name ";
			sql += "'status_name',amp_terms_assist.terms_assist_name,amp_organisation.name";
			sql += "'donor_name',amp_organisation.amp_org_id";
			sql += "'amp_donor_id',amp_organisation.org_type";
			sql += "'org_type',amp_funding.amp_funding_id,";
			sql += "case when transaction_type='0' and adjustment_type='0' and ";
			sql += "org_role_code='DN' then transaction_amount else 0 end ";
			sql += "'planned_commitment',";
			sql += "case when transaction_type='1' and adjustment_type='0' and ";
			sql += "org_role_code='DN' then transaction_amount else 0 end ";
			sql += "'planned_disbursement',";
			sql += "case when transaction_type='2' and adjustment_type='0' and ";
			sql += "org_role_code='DN' then transaction_amount else 0 end ";
			sql += "'planned_expenditure',";
			sql += "case when transaction_type='0' and adjustment_type='1' and ";
			sql += "org_role_code='DN' then transaction_amount else 0 end ";
			sql += "'actual_commitment',";
			sql += "case when transaction_type='1' and adjustment_type='1' and ";
			sql += "org_role_code='DN' then transaction_amount else 0 end ";
			sql += "'actual_disbursement',";
			sql += "case when transaction_type='2' and adjustment_type='1' and ";
			sql += "org_role_code='DN' then transaction_amount else 0 end ";
			sql += "'actual_expenditure',";
			sql += "currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.actual_completion_date,extract(YEAR from ";
			sql += "transaction_date) 'fiscal_year',case when extract(MONTH from ";
			sql += "transaction_date) between '1' and '3' then '1' ";
			sql += "when extract(MONTH from transaction_date) between '4' and '6' then '2' ";
			sql += "when extract(MONTH from transaction_date) between '7' and '9' then '3' ";
			sql += "when extract(MONTH from transaction_date) between '10' and '12' then '4' ";
			sql += "end 'fiscal_quarter',org_role_code 'perspective',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' when amp_activity.amp_level_id='2' then 'NATIONAL' when amp_activity.amp_level_id='3' then 'BOTH' else 'Unspecified' end 'level_name',amp_activity.description,NULL,NULL,NULL,NULL,1 ";
			sql += "from amp_activity LEFT JOIN amp_funding ON ";
			sql += "amp_activity.amp_activity_id=amp_funding.amp_activity_id,amp_funding_detail, ";
			sql += "amp_terms_assist,amp_organisation, amp_currency,amp_status ";
			sql += "where (amp_funding.amp_modality_id is null or amp_level_id is null) ";
			sql += "and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += "and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += "and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += "and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "' ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id and org_role_code='DN'";
			
			logger.debug(sql);
			// Query #6
			// This script inserts details of activities having funding details but with funding instrument or implementation level null 
			//entered from MOFED perspective.
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #6 executed in " + (t2-t1) + "ms");			
			
			sql = "insert into amp_report_cache select ";
			sql += "NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name,amp_funding.amp_modality_id,case ";
			sql += "when amp_funding.amp_modality_id='1' then 'Direct Budget Support' ";
			sql += "when amp_funding.amp_modality_id='2' then 'Pool Fund' ";
			sql += "when amp_funding.amp_modality_id='3' then 'Project Support' ";
			sql += "when amp_funding.amp_modality_id='4' then 'Food Aid' ";
			sql += "when amp_funding.amp_modality_id='5' then 'Technical Assistance' when amp_funding.amp_modality_id='6' then 'Program Support' else 'Not Exist' end ";
			sql += "'modality_name',";
			sql += "amp_activity.amp_status_id,amp_status.name ";
			sql += "'status_name',amp_terms_assist.terms_assist_name,amp_organisation.name";
			sql += "'donor_name',amp_organisation.amp_org_id";
			sql += "'amp_donor_id',amp_organisation.org_type";
			sql += "'org_type',amp_funding.amp_funding_id,";
			sql += "case when transaction_type='0' and adjustment_type='0' and ";
			sql += "org_role_code='MA' then transaction_amount else 0 end ";
			sql += "'planned_commitment',";
			sql += "case when transaction_type='1' and adjustment_type='0' and ";
			sql += "org_role_code='MA' then transaction_amount else 0 end ";
			sql += "'planned_disbursement',";
			sql += "case when transaction_type='2' and adjustment_type='0' and ";
			sql += "org_role_code='MA' then transaction_amount else 0 end ";
			sql += "'planned_expenditure',";
			sql += "case when transaction_type='0' and adjustment_type='1' and ";
			sql += "org_role_code='MA' then transaction_amount else 0 end ";
			sql += "'actual_commitment',";
			sql += "case when transaction_type='1' and adjustment_type='1' and ";
			sql += "org_role_code='MA' then transaction_amount else 0 end ";
			sql += "'actual_disbursement',";
			sql += "case when transaction_type='2' and adjustment_type='1' and ";
			sql += "org_role_code='MA' then transaction_amount else 0 end ";
			sql += "'actual_expenditure',";
			sql += "currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.actual_completion_date,extract(YEAR from ";
			sql += "transaction_date) 'fiscal_year',case when extract(MONTH from ";
			sql += "transaction_date) between '1' and '3' then '1' ";
			sql += "when extract(MONTH from transaction_date) between '4' and '6' then '2' ";
			sql += "when extract(MONTH from transaction_date) between '7' and '9' then '3' ";
			sql += "when extract(MONTH from transaction_date) between '10' and '12' then '4' ";
			sql += "end 'fiscal_quarter',org_role_code 'perspective',amp_team_id ,transaction_date 'transaction_date',amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' when amp_activity.amp_level_id='2' then 'NATIONAL' when amp_activity.amp_level_id='3' then 'BOTH' else 'Unspecified' end 'level_name',amp_activity.description,NULL,NULL,NULL,NULL,1 ";
			sql += "from amp_activity LEFT JOIN amp_funding ON ";
			sql += "amp_activity.amp_activity_id=amp_funding.amp_activity_id,amp_funding_detail, ";
			sql += "amp_terms_assist,amp_organisation, amp_currency,amp_status ";
			sql += "where (amp_funding.amp_modality_id is null or amp_level_id is null) ";
			sql += "and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += "and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += "and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += "and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "' ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id and org_role_code='MA'";
			
			logger.debug(sql);
			// Query #7
			// Deletes all existing records from amp_report_sector for ampActivityId.
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #7 executed in " + (t2-t1) + "ms");			

			sql = "delete from amp_report_sector where amp_activity_id='" + ampActivityId + "'";
			
			// Query #8
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #8 executed in " + (t2-t1) + "ms");			
			
			sql = "select amp_sector_id from amp_activity_sector ";
			sql += "where amp_activity_id='" + ampActivityId + "'";
			
			// Query #9
			t1 = System.currentTimeMillis();						
			rs=stmt.executeQuery(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #9 executed in " + (t2-t1) + "ms");
			
			StringBuffer sb = new StringBuffer();
			while(rs.next())
			{
				AmpSector ampSector=SectorUtil.getAmpParentSector(new Long(rs.getLong("amp_sector_id")));
				logger.debug("Sector: " + ampSector.getAmpSectorId());
				sb.append("insert into amp_report_sector values(NULL,'");
				sb.append(ampActivityId);
				sb.append("','");
				sb.append(ampSector.getName());
				sb.append("','");
				sb.append(ampSector.getAmpSectorId());
				sb.append("',NULL,NULL,'");
				sb.append(rs.getString("amp_sector_id"));
				sb.append("')");
				// Query #10
				t1 = System.currentTimeMillis();
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate(sb.toString());
				t2 = System.currentTimeMillis();
				logger.debug("Query #10 executed in " + (t2-t1) + "ms");
				sb.delete(0,sb.length());
			}

			sql= "delete from amp_report_location where amp_activity_id='" + ampActivityId + "'";
			// Query #11
			t1 = System.currentTimeMillis();										
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #11 executed in " + (t2-t1) + "ms");			

			sql= "insert into amp_report_location ";
			sql += "select distinct NULL,amp_activity_id,country,region ";
			sql += "from amp_activity_location,amp_location ";
			sql += "where amp_activity_location.amp_location_id=amp_location.amp_location_id and amp_activity_id='" + ampActivityId + "'";
			
			logger.debug(sql);
			// Query #12
			t1 = System.currentTimeMillis();										
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #12 executed in " + (t2-t1) + "ms");			

			sql = "delete from amp_report_physical_performance where amp_activity_id='" + ampActivityId + "'";
			// Query #13
			t1 = System.currentTimeMillis();										
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #13 executed in " + (t2-t1) + "ms");			
			
			sql  = "insert into amp_report_physical_performance ";
			sql += "select NULL,amp_activity.amp_activity_id,amp_physical_performance.title,amp_physical_performance.description ";
			sql += "from amp_activity,amp_components,amp_physical_performance ";
			sql += "where amp_activity.amp_activity_id=amp_components.amp_activity_id ";
			sql += "and amp_components.amp_component_id=amp_physical_performance.amp_component_id and amp_activity.amp_activity_id='" + ampActivityId + "'";
			logger.debug(sql);
			// Query #14
			t1 = System.currentTimeMillis();										
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #14 executed in " + (t2-t1) + "ms");		
			
			sql="insert into amp_report_cache " +
					"select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name', " +
					"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name', " +
					"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id', " +
					"case when transaction_type='0' and adjustment_type='0' and perspective_id='2' then transaction_amount else 0 end 'planned_commitment', " +
					"case when transaction_type='1' and adjustment_type='0' and perspective_id='2' then transaction_amount else 0 end 'planned_disbursement', " +
					"case when transaction_type='2' and adjustment_type='0' and perspective_id='2' then transaction_amount else 0 end 'planned_expenditure', " +
					"case when transaction_type='0' and adjustment_type='1' and perspective_id='2' then transaction_amount else 0 end 'actual_commitment', " +
					"case when transaction_type='1' and adjustment_type='1' and perspective_id='2' then transaction_amount else 0 end 'actual_disbursement', " +
					"case when transaction_type='2' and adjustment_type='1' and perspective_id='2' then transaction_amount else 0 end 'actual_expenditure', " +
					"currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date, " +
					"amp_activity.actual_completion_date,extract(YEAR from transaction_date) 'fiscal_year', " +
					"case when extract(MONTH from transaction_date) between '1' and '3' then '1' " +
					"when extract(MONTH from transaction_date) between '4' and '6' then '2' " +
					"when extract(MONTH from transaction_date) between '7' and '9' then '3' " +
					"when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter', " +
					"'MA',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
					"amp_components.amp_component_id,amp_components.title , NULL , NULL, 2  " +
					"from amp_activity,amp_components,amp_component_funding,amp_status,amp_currency,amp_level " +
					"where amp_activity.amp_activity_id=amp_components.amp_activity_id " +
					"and amp_components.amp_component_id=amp_component_funding.amp_component_id " +
					"and amp_activity.amp_status_id =amp_status.amp_status_id " +
					"and amp_component_funding.currency_id=amp_currency.amp_currency_id " +
					"and amp_activity.amp_level_id=amp_level.amp_level_id and perspective_id='2' " +
					"and amp_activity.amp_activity_id='" + ampActivityId + "'";

			logger.debug(sql);
			//Query #15 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #15 executed in " + (t2-t1) + "ms");
			
			
			
			sql="insert into amp_report_cache " +
				"select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name', " +
				"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name', " +
				"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id', " +
				"case when transaction_type='0' and adjustment_type='0' and perspective_id='1' then transaction_amount else 0 end 'planned_commitment', " +
				"case when transaction_type='1' and adjustment_type='0' and perspective_id='1' then transaction_amount else 0 end 'planned_disbursement', " +
				"case when transaction_type='2' and adjustment_type='0' and perspective_id='1' then transaction_amount else 0 end 'planned_expenditure', " +
				"case when transaction_type='0' and adjustment_type='1' and perspective_id='1' then transaction_amount else 0 end 'actual_commitment', " +
				"case when transaction_type='1' and adjustment_type='1' and perspective_id='1' then transaction_amount else 0 end 'actual_disbursement', " +
				"case when transaction_type='2' and adjustment_type='1' and perspective_id='1' then transaction_amount else 0 end 'actual_expenditure', " +
				"currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date, " +
				"amp_activity.actual_completion_date,extract(YEAR from transaction_date) 'fiscal_year', " +
				"case when extract(MONTH from transaction_date) between '1' and '3' then '1' " +
				"when extract(MONTH from transaction_date) between '4' and '6' then '2' " +
				"when extract(MONTH from transaction_date) between '7' and '9' then '3' " +
				"when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter', " +
				"'DN',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
				" amp_components.amp_component_id,amp_components.title , NULL , NULL, 2 " +
				" from amp_activity,amp_components,amp_component_funding,amp_status,amp_currency,amp_level " +
				"where amp_activity.amp_activity_id=amp_components.amp_activity_id " +
				"and amp_components.amp_component_id=amp_component_funding.amp_component_id " +
				"and amp_activity.amp_status_id =amp_status.amp_status_id " +
				"and amp_component_funding.currency_id=amp_currency.amp_currency_id " +
				"and amp_activity.amp_level_id=amp_level.amp_level_id and perspective_id='1' " +
				"and amp_activity.amp_activity_id='" + ampActivityId + "'";

			logger.debug(sql);
			//Query #16 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #16 executed in " + (t2-t1) + "ms");
			
			
			sql="insert into amp_report_cache " +
				"select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name', " +
				"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name', " +
				"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id', " +
				"0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure', " +
				"NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date, " +
				"amp_activity.actual_completion_date,NULL 'fiscal_year', " +
				"NULL 'fiscal_quarter','MA',amp_team_id,NULL 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
				"NULL,NULL, NULL, NULL, 2  from amp_activity LEFT JOIN amp_components ON " +
				"amp_activity.amp_activity_id=amp_components.amp_activity_id,amp_status,amp_level " +
				"where amp_activity.amp_status_id =amp_status.amp_status_id " +
				"and amp_activity.amp_level_id=amp_level.amp_level_id and amp_components.amp_activity_id is null " +
				"and amp_activity.amp_activity_id='" + ampActivityId + "'";

			logger.debug(sql);
			//Query #17 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #17 executed in " + (t2-t1) + "ms");
			
			sql="insert into amp_report_cache select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name', " +
				"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name', " +
				"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id', " +
				"0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure', " +
				"NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date, " +
				"amp_activity.actual_completion_date,NULL 'fiscal_year',NULL 'fiscal_quarter', " +
				"'DN',amp_team_id,NULL 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
				"NULL,NULL , NULL , NULL , 2 from amp_activity LEFT JOIN amp_components ON " +
				"amp_activity.amp_activity_id=amp_components.amp_activity_id,amp_status,amp_level " +
				"where amp_activity.amp_status_id =amp_status.amp_status_id " +
				"and amp_activity.amp_level_id=amp_level.amp_level_id and amp_components.amp_activity_id is null " +
				"and amp_activity.amp_activity_id='" + ampActivityId + "'";

			logger.debug(sql);
			//Query #18 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #18 executed in " + (t2-t1) + "ms");
			
			
			sql="insert into amp_report_cache select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name', " +
					"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name'," +
					"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id'," +
					"case when transaction_type='0' and adjustment_type='0' and perspective_id='2' then transaction_amount else 0 end 'planned_commitment'," +
					"case when transaction_type='1' and adjustment_type='0' and perspective_id='2' then transaction_amount else 0 end 'planned_disbursement'," +
					"case when transaction_type='2' and adjustment_type='0' and perspective_id='2' then transaction_amount else 0 end 'planned_expenditure'," +
					"case when transaction_type='0' and adjustment_type='1' and perspective_id='2' then transaction_amount else 0 end 'actual_commitment'," +
					"case when transaction_type='1' and adjustment_type='1' and perspective_id='2' then transaction_amount else 0 end 'actual_disbursement'," +
					"case when transaction_type='2' and adjustment_type='1' and perspective_id='2' then transaction_amount else 0 end 'actual_expenditure'," +
					"currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date," +
					"amp_activity.actual_completion_date,extract(YEAR from transaction_date) 'fiscal_year'," +
					"case when extract(MONTH from transaction_date) between '1' and '3' then '1' " +
					"when extract(MONTH from transaction_date) between '4' and '6' then '2' " +
					"when extract(MONTH from transaction_date) between '7' and '9' then '3' " +
					"when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter', " +
					"'MA',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
					"NULL,NULL , amp_region_id, amp_region.name , 3  " +
					"from amp_activity,amp_region,amp_regional_funding,amp_status,amp_currency,amp_level " +
					"where amp_activity.amp_activity_id=amp_regional_funding.activity_id " +
					"and amp_regional_funding.region_id=amp_region.amp_region_id " +
					"and amp_activity.amp_status_id =amp_status.amp_status_id " +
					"and amp_regional_funding.currency_id=amp_currency.amp_currency_id " +
					"and amp_activity.amp_level_id=amp_level.amp_level_id and perspective_id='2' "+
					"and amp_activity.amp_activity_id='" + ampActivityId + "'";
			
			logger.debug(sql);
			//Query #19 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #19 executed in " + (t2-t1) + "ms");
			
			
			sql="insert into amp_report_cache select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name',  " +
				"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name'," +
				"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id'," +
				"case when transaction_type='0' and adjustment_type='0' and perspective_id='1' then transaction_amount else 0 end 'planned_commitment'," +
				"case when transaction_type='1' and adjustment_type='0' and perspective_id='1' then transaction_amount else 0 end 'planned_disbursement'," +
				"case when transaction_type='2' and adjustment_type='0' and perspective_id='1' then transaction_amount else 0 end 'planned_expenditure'," +
				"case when transaction_type='0' and adjustment_type='1' and perspective_id='1' then transaction_amount else 0 end 'actual_commitment'," +
				"case when transaction_type='1' and adjustment_type='1' and perspective_id='1' then transaction_amount else 0 end 'actual_disbursement'," +
				"case when transaction_type='2' and adjustment_type='1' and perspective_id='1' then transaction_amount else 0 end 'actual_expenditure'," +
				"currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date," +
				"amp_activity.actual_completion_date,extract(YEAR from transaction_date) 'fiscal_year'," +
				"case when extract(MONTH from transaction_date) between '1' and '3' then '1' " +
				"when extract(MONTH from transaction_date) between '4' and '6' then '2' " +
				"when extract(MONTH from transaction_date) between '7' and '9' then '3' " +
				"when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter', " +
				"'DN',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
				"NULL ,NULL , amp_region_id, amp_region.name, 3 " +
				"from amp_activity,amp_region,amp_regional_funding,amp_status,amp_currency,amp_level " +
				"where amp_activity.amp_activity_id=amp_regional_funding.activity_id " +
				"and amp_regional_funding.region_id=amp_region.amp_region_id " +
				"and amp_activity.amp_status_id =amp_status.amp_status_id " +
				"and amp_regional_funding.currency_id=amp_currency.amp_currency_id " +
				"and amp_activity.amp_level_id=amp_level.amp_level_id and perspective_id='1' "+
				"and amp_activity.amp_activity_id='" + ampActivityId + "'";


			logger.debug(sql);
			//Query #20 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #20 executed in " + (t2-t1) + "ms");
			
			
			sql="insert into amp_report_cache select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name', " +
				"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name', " +
				"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id', " +
				"0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure', " +
				"NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date, " +
				"amp_activity.actual_completion_date,NULL 'fiscal_year', NULL 'fiscal_quarter', " +
				"'MA',amp_team_id,NULL 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
				"NULL,NULL , NULL , NULL , 3  from amp_activity LEFT JOIN amp_regional_funding ON " +
				"amp_activity.amp_activity_id=amp_regional_funding.activity_id,amp_status,amp_level where amp_activity.amp_status_id =amp_status.amp_status_id " +
				"and amp_activity.amp_level_id=amp_level.amp_level_id and activity_id is null "+
				"and amp_activity.amp_activity_id='" + ampActivityId + "'";
			
			logger.debug(sql);
			//Query #21 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #21 executed in " + (t2-t1) + "ms");
			
			
			
			sql="insert into amp_report_cache select NULL,amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name', " +
				"NULL 'amp_modality_id', NULL 'modality_name',amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name', " +
				"NULL 'term_assist_name',NULL 'donor_name',NULL 'amp_donor_id',NULL 'org_type',NULL 'amp_funding_id', " +
				"0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure', " +
				"NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date, " +
				"amp_activity.actual_completion_date,NULL 'fiscal_year',NULL 'fiscal_quarter', " +
				"'DN',amp_team_id,NULL 'transaction_date',amp_activity.amp_level_id,amp_level.name,amp_activity.description, " +
				"NULL ,NULL , NULL , NULL , 3 from amp_activity LEFT JOIN amp_regional_funding ON amp_activity.amp_activity_id=amp_regional_funding.activity_id, " +
				"amp_status,amp_level where amp_activity.amp_status_id =amp_status.amp_status_id and amp_activity.amp_level_id=amp_level.amp_level_id and activity_id is null " +
				"and amp_activity.amp_activity_id='" + ampActivityId + "'";

			logger.debug(sql);
			//Query #22 (added by mihai)
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #22 executed in " + (t2-t1) + "ms");
			
			
			
			logger.debug("All the statements got executed");
//			con.close();
		} catch (SQLException e) {
			logger.error("SQLException from updateReportCache() " + e.getMessage());
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);			
		} finally {
			if (con != null) {
				try {
//					con.close();
				} catch (Exception ex) {
					logger.error("Cannot close the connection");
				}
			}
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		logger.debug("updateReportCache() returning");

		return 1;
	}

	private static void setConnectionProps() {

		String file =  new UpdateDB().getClass().getResource("/hibernate.properties").getPath(); 

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				if (line != null && line.trim().length() != 0) {
					if (line.charAt(0) == '#')
						continue;

					int del = line.indexOf(" ");
					if (del != -1) {
						String sub1 = line.substring(0, del);
						String sub2 = line.substring(del + 1, line.length());
						if (sub1
								.equalsIgnoreCase("hibernate.connection.driver_class")) {
							driver = sub2;
							count++;
						}
						if (sub1.equalsIgnoreCase("hibernate.connection.url")) {
							url = sub2;
							count++;
						}
						if (sub1
								.equalsIgnoreCase("hibernate.connection.username")) {
							username = sub2;
							count++;
						}
						if (sub1
								.equalsIgnoreCase("hibernate.connection.password")) {
							passwd = sub2;
							count++;
						}
					}

					if (count >= 4)
						break;
				}
			}
		} catch (Exception e) {
			logger.error("Exception thrown from setConnectionProps() " + e.getMessage());
		}

		logger.debug("setConnectionProps returning");
		
	}

	
//	===========================================================================
	
	String computeActivityStartDate(String val)
	{
		////////System.out.println(" UpdateDB  Test1 passed");
		String query ="", res="";
		try
		{
        Connection connection = DriverManager.getConnection(url, username, passwd);

			query =  "select min(actual_start_date) from amp_funding f where amp_activity_id = " + val ;
			query = query	+ " group by amp_activity_id ";
			 Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.first() == true)
			{
				res = rs.getString(1);
			}
			else
			{
				res=null;
			}
			////////System.out.println(val + "  ASD " + res);

		
			return(res);
		}
		catch(Exception e)
		{
					//////System.out.println( "ActivityStartDate :"+e);
		}
		return query;
	}


	String computeActivityCloseDate(String val)
	{
		////////System.out.println("Test2 passed");
		String query ="", res="";
		try
		{
        Connection connection = DriverManager.getConnection(url, username, passwd);

			query = "select max(d.closing_date) from amp_funding f, amp_closing_date_history d ";
			query = query + "where (f.amp_activity_id = " + val + " )and (f.amp_funding_id = d.amp_funding_id) and (d.type = 2) " ;
			Statement stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(query);
			if(rs.first() == true)
			{
				res = rs.getString(1);
			}
			else
			{
				res=null;
			}
			////////System.out.println(val + "  ACD " + res);


			return(res);
		}
		catch(Exception e)
		{
					//////System.out.println("ActivityCloseDate : "+e);
		}
		return query;
	}


	String computeActivityOrigCloseDate(String val)
	{
		////////System.out.println("Test3 passed");
		String query ="", res="";
		try
		{
        Connection connection = DriverManager.getConnection(url, username, passwd);
			query = " select max(d.closing_date) from amp_funding f, amp_closing_date_history d ";
			query += "where (f.amp_activity_id = " + val + " ) and (f.amp_funding_id = d.amp_funding_id) ";
			query += "and (d.type = 0) ";

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.first() == true)
			{
				res = rs.getString(1);
			}
			else
			{
				res=null;
			}
			////////System.out.println(val + "  AOCD " + res);


			return(res);
		}
		catch(Exception e)
		{
					//////System.out.println(" ActivityOrigCloseDate :"+e);
		}
		return query;
	}

	void updateAmpActivityDates(String id,String sdd, String cdd, String ocd)
	{
		
		String query ="";
		try
		{
        Connection connection = DriverManager.getConnection(url, username, passwd);
			
		if(sdd != null)
		{
			sdd = "'"+sdd+"'";
		}
		if(cdd != null)
		{
			cdd = "'"+cdd+"'";
		}
		if(ocd != null)
		{
			ocd = "'"+ocd+"'";
		}

		////////System.out.println("Test4 passed");
		query = "update amp_activity set activity_start_date = "+sdd+", activity_close_date = "+cdd ;
		query+= " , original_comp_date ="+ocd+"  where amp_activity_id = "+ id;	
		//////System.out.println(query);		

		Statement stmt = connection.createStatement();
		int cols = stmt.executeUpdate(query);
		////////System.out.println( cols +" columns updated");
		}
		catch(Exception e)
		{
			//////System.out.println(" updateAmpActivityDates :"+ e);
		}
	}

//-=======================================================================	
	
}
