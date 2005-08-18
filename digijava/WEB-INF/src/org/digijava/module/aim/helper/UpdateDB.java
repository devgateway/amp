package org.digijava.module.aim.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpSector;

import org.apache.log4j.Logger;


public class UpdateDB {
	
	private static String driver = "";
	private static String url = "";
	private static String username = "";
	private static String passwd = "";
	
	private static Logger logger = Logger.getLogger(UpdateDB.class);
	
	
	public static int updateReportCache(Long ampActivityId) throws Exception {

		logger.debug("In Update Report Cache");

		Connection con = null;
		try {
			
			setConnectionProps();
			
			//logger.debug("JDBC Driver :" + driver);
			//logger.debug("URL :" + url);
			//logger.debug("Username :" + username);
			//logger.debug("Password :" + passwd);
			
			Class.forName(driver).newInstance();

			con = DriverManager.getConnection(url, username, passwd);

			Statement stmt = con.createStatement();
			ResultSet rs = null;
			

			String sql = "delete from amp_report_cache where amp_activity_id='" + ampActivityId + "'";
			// Query #1
			long t1 = System.currentTimeMillis();			
			stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			logger.debug("Query #1 executed in " + (t2-t1) + "ms");
			

			sql = "insert into amp_report_cache ";
			sql += " select '',amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name',amp_funding.amp_modality_id 'amp_modality_id',amp_modality.name 'modality_name',";
			sql += " amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name',";
			sql += " amp_terms_assist.terms_assist_name,amp_organisation.name 'donor_name',amp_organisation.amp_org_id 'amp_donor_id',amp_organisation.org_type 'org_type',amp_funding.amp_funding_id,";
			sql += " case when transaction_type='0' and adjustment_type='0' and org_role_code='DN' then transaction_amount else 0 end 'planned_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='0' and org_role_code='DN' then transaction_amount else 0 end 'planned_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='0' and org_role_code='DN' then transaction_amount else 0 end 'planned_expenditure',";
			sql += " case when transaction_type='0' and adjustment_type='1' and org_role_code='DN' then transaction_amount else 0 end 'actual_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='1' and org_role_code='DN' then transaction_amount else 0 end 'actual_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='1' and org_role_code='DN' then transaction_amount else 0 end 'actual_expenditure',";
			sql += " currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.proposed_approval_date,extract(YEAR from transaction_date) 'fiscal_year',";
			sql += " case when extract(MONTH from transaction_date) between '1' and '3' then '1'";
			sql += " when extract(MONTH from transaction_date) between '4' and '6' then '2'";
			sql += " when extract(MONTH from transaction_date) between '7' and '9' then '3'";
			sql += " when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter',org_role_code 'perspective',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name ";
			sql += " from amp_activity,amp_currency,amp_modality,amp_terms_assist,amp_funding,amp_organisation,amp_status,amp_funding_detail,amp_level ";
			sql += " where amp_funding.amp_modality_id=amp_modality.amp_modality_id ";
			sql += " and amp_activity.amp_activity_id=amp_funding.amp_activity_id and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += " and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += " and amp_activity.amp_status_id =amp_status.amp_status_id ";
			sql += " and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += " and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "'";
			sql += " and amp_activity.amp_level_id=amp_level.amp_level_id and org_role_code='DN'";

			// Query #2
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #2 executed in " + (t2-t1) + "ms");			
			

			sql = "insert into amp_report_cache ";
			sql += " select '',amp_activity.amp_activity_id,amp_id,amp_activity.name 'activity_name',amp_funding.amp_modality_id 'amp_modality_id',amp_modality.name 'modality_name',";
			sql += " amp_activity.amp_status_id 'amp_status_id',amp_status.name 'status_name',";
			sql += " amp_terms_assist.terms_assist_name,amp_organisation.name 'donor_name',amp_organisation.amp_org_id 'amp_donor_id',amp_organisation.org_type 'org_type',amp_funding.amp_funding_id,";
			sql += " case when transaction_type='0' and adjustment_type='0' and org_role_code='MA' then transaction_amount else 0 end 'planned_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='0' and org_role_code='MA' then transaction_amount else 0 end 'planned_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='0' and org_role_code='MA' then transaction_amount else 0 end 'planned_expenditure',";
			sql += " case when transaction_type='0' and adjustment_type='1' and org_role_code='MA' then transaction_amount else 0 end 'actual_commitment',";
			sql += " case when transaction_type='1' and adjustment_type='1' and org_role_code='MA' then transaction_amount else 0 end 'actual_disbursement',";
			sql += " case when transaction_type='2' and adjustment_type='1' and org_role_code='MA' then transaction_amount else 0 end 'actual_expenditure',";
			sql += " currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.proposed_approval_date,extract(YEAR from transaction_date) 'fiscal_year',";
			sql += " case when extract(MONTH from transaction_date) between '1' and '3' then '1'";
			sql += " when extract(MONTH from transaction_date) between '4' and '6' then '2'";
			sql += " when extract(MONTH from transaction_date) between '7' and '9' then '3'";
			sql += " when extract(MONTH from transaction_date) between '10' and '12' then '4' end 'fiscal_quarter',org_role_code 'perspective',amp_team_id ,transaction_date 'transaction_date',amp_activity.amp_level_id,amp_level.name ";
			sql += " from amp_activity,amp_currency,amp_modality,amp_terms_assist,amp_funding,amp_organisation,amp_status,amp_funding_detail,amp_level ";
			sql += " where amp_funding.amp_modality_id=amp_modality.amp_modality_id ";
			sql += " and amp_activity.amp_activity_id=amp_funding.amp_activity_id and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += " and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += " and amp_activity.amp_status_id =amp_status.amp_status_id ";
			sql += " and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += " and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "'";
			sql += " and amp_activity.amp_level_id=amp_level.amp_level_id and org_role_code='MA'";
			
			// Query #3
			t1 = System.currentTimeMillis();			
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #3 executed in " + (t2-t1) + "ms");			

			
			sql = "insert into amp_report_cache ";
			sql += "select '',amp_activity.amp_activity_id,amp_activity.amp_id,amp_activity.name,NULL,NULL, ";
			sql += "amp_activity.amp_status_id,amp_status.name,NULL,amp_organisation.name,amp_org_role.organisation,amp_organisation.org_type,NULL,";
			sql += "0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',";
			sql += "0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure',";
			sql += "NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,";
			sql += "amp_activity.proposed_approval_date,NULL 'fiscal_year',";
			sql += "NULL 'fiscal_quarter','MA',amp_activity.amp_team_id,NULL,amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' ";
			sql += "when amp_activity.amp_level_id='2' then 'NATIONAL' ";
			sql += "when amp_activity.amp_level_id='3' then 'BOTH' else 'Not Exist' end ";
			sql += "'level_name' from amp_activity LEFT JOIN amp_report_cache ON ";
			sql += "amp_activity.amp_activity_id=amp_report_cache.amp_activity_id,amp_org_role,amp_organisation,amp_status ";
			sql += "where amp_report_cache.amp_activity_id is null ";
			//sql += "and amp_role_id='1' and amp_activity.amp_activity_id=amp_org_role.activity ";
			sql += "and amp_org_role.role='1' and amp_activity.amp_activity_id=amp_org_role.activity ";
			sql += "and amp_org_role.organisation=amp_organisation.amp_org_id ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id";
			
			// Query #4
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #4 executed in " + (t2-t1) + "ms");			

			sql = "insert into amp_report_cache ";
			sql += "select distinct ' ',";
			sql += "amp_activity.amp_activity_id,amp_id,amp_activity.name,NULL,NULL, ";
			sql += "amp_activity.amp_status_id,amp_status.name ";
			sql += "'status_name',NULL,NULL,NULL,NULL,NULL,";
			sql += "0 'planned_commitment',0 'planned_disbursement',0 'planned_expenditure',";
			sql += "0 'actual_commitment',0 'actual_disbursement',0 'actual_expenditure',";
			sql += "NULL,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.proposed_approval_date,NULL 'fiscal_year',";
			sql += "NULL 'fiscal_quarter','MA',amp_team_id,NULL,amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' when amp_activity.amp_level_id='2' then 'NATIONAL' when amp_activity.amp_level_id='3' then 'BOTH' else 'Unspecified' end 'level_name' ";
			sql += "from amp_activity LEFT JOIN amp_funding ON ";
			sql += "amp_activity.amp_activity_id=amp_funding.amp_activity_id,amp_status ";
			sql += "where amp_funding.amp_funding_id is null and amp_activity.amp_activity_id='" + ampActivityId + "' ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id";

			// Query #5
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #5 executed in " + (t2-t1) + "ms");			
			
			sql = "insert into amp_report_cache select ";
			sql += "'',amp_activity.amp_activity_id,amp_id,amp_activity.name,amp_funding.amp_modality_id,case ";
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
			sql += "currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.proposed_approval_date,extract(YEAR from ";
			sql += "transaction_date) 'fiscal_year',case when extract(MONTH from ";
			sql += "transaction_date) between '1' and '3' then '1' ";
			sql += "when extract(MONTH from transaction_date) between '4' and '6' then '2' ";
			sql += "when extract(MONTH from transaction_date) between '7' and '9' then '3' ";
			sql += "when extract(MONTH from transaction_date) between '10' and '12' then '4' ";
			sql += "end 'fiscal_quarter',org_role_code 'perspective',amp_team_id,transaction_date 'transaction_date',amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' when amp_activity.amp_level_id='2' then 'NATIONAL' when amp_activity.amp_level_id='3' then 'BOTH' else 'Unspecified' end 'level_name' ";
			sql += "from amp_activity LEFT JOIN amp_funding ON ";
			sql += "amp_activity.amp_activity_id=amp_funding.amp_activity_id,amp_funding_detail, ";
			sql += "amp_terms_assist,amp_organisation, amp_currency,amp_status ";
			sql += "where (amp_funding.amp_modality_id is null or amp_level_id is null) ";
			sql += "and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += "and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += "and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += "and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "' ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id";
			
			// Query #6
			t1 = System.currentTimeMillis();						
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #6 executed in " + (t2-t1) + "ms");			
			
			sql = "insert into amp_report_cache select ";
			sql += "'',amp_activity.amp_activity_id,amp_id,amp_activity.name,amp_funding.amp_modality_id,case ";
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
			sql += "currency_code,amp_activity.actual_start_date,amp_activity.actual_completion_date,amp_activity.proposed_start_date,amp_activity.proposed_approval_date,extract(YEAR from ";
			sql += "transaction_date) 'fiscal_year',case when extract(MONTH from ";
			sql += "transaction_date) between '1' and '3' then '1' ";
			sql += "when extract(MONTH from transaction_date) between '4' and '6' then '2' ";
			sql += "when extract(MONTH from transaction_date) between '7' and '9' then '3' ";
			sql += "when extract(MONTH from transaction_date) between '10' and '12' then '4' ";
			sql += "end 'fiscal_quarter',org_role_code 'perspective',amp_team_id ,transaction_date 'transaction_date',amp_activity.amp_level_id,case when amp_activity.amp_level_id='1' then 'REGIONAL' when amp_activity.amp_level_id='2' then 'NATIONAL' when amp_activity.amp_level_id='3' then 'BOTH' else 'Unspecified' end 'level_name' ";
			sql += "from amp_activity LEFT JOIN amp_funding ON ";
			sql += "amp_activity.amp_activity_id=amp_funding.amp_activity_id,amp_funding_detail, ";
			sql += "amp_terms_assist,amp_organisation, amp_currency,amp_status ";
			sql += "where (amp_funding.amp_modality_id is null or amp_level_id is null) ";
			sql += "and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += "and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += "and amp_funding.amp_terms_assist_id=amp_terms_assist.amp_terms_assist_id ";
			sql += "and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id and amp_activity.amp_activity_id='" + ampActivityId + "' ";
			sql += "and amp_activity.amp_status_id=amp_status.amp_status_id";
			
			// Query #7
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
				AmpSector ampSector=DbUtil.getAmpParentSector(new Long(rs.getLong("amp_sector_id")));
				logger.debug("Sector: " + ampSector.getAmpSectorId());
				sb.append("insert into amp_report_sector values('','");
				sb.append(ampActivityId);
				sb.append("','");
				sb.append(ampSector.getName());
				sb.append("','");
				sb.append(ampSector.getAmpSectorId());
				sb.append("','','','");
				sb.append(rs.getString("amp_sector_id"));
				sb.append("')");
				// Query #10
				t1 = System.currentTimeMillis();							
				stmt.executeUpdate(sb.toString());
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
			sql += "select distinct '',amp_activity_id,country,region ";
			sql += "from amp_activity_location,amp_location ";
			sql += "where amp_activity_location.amp_location_id=amp_location.amp_location_id and amp_activity_id='" + ampActivityId + "'";
			
			// Query #12
			t1 = System.currentTimeMillis();										
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #12 executed in " + (t2-t1) + "ms");			

			sql = "delete from amp_physical_component_report where amp_activity_id='" + ampActivityId + "'";
			// Query #13
			t1 = System.currentTimeMillis();										
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #13 executed in " + (t2-t1) + "ms");			
			
			sql = "insert into amp_physical_component_report ";
			sql += "select distinct '',amp_activity.amp_activity_id,amp_activity.name ";
			sql += "'activity_name',amp_organisation.amp_org_id 'amp_donor_id',amp_organisation.name 'donor_name',amp_components.amp_component_id 'amp_component_id',amp_components.title 'component_name',amp_components.description 'objective',";
			sql += "signature_date,amp_activity.actual_start_date,amp_activity.actual_completion_date,";
			sql += "case when (transaction_type=0 and adjustment_type=1) then transaction_amount else 0 end 'actual_commitment',";
			sql += "case when (transaction_type=0 and adjustment_type=1) then currency_code else null end 'comm_currency_code', amount 'actual_expenditure',currency_code 'exp_currency_code',amp_physical_performance.title 'status',amp_physical_performance.reporting_date,";
			sql += "case when (mofed_cnt_first_name is not null or mofed_cnt_last_name is not null) then concat_ws(' ',mofed_cnt_first_name,mofed_cnt_last_name) else null end 'mofed_contact',";
			sql += "case when (cont_first_name is not null or cont_last_name is not null) then concat_ws(' ',cont_first_name,cont_last_name) else null end 'donor_contact',";
			sql += "amp_team_id,transaction_date,extract(YEAR from transaction_date) 'fiscal_year',";
			sql += "amp_status_id,amp_funding.amp_modality_id from amp_components,amp_physical_performance,amp_activity,amp_funding,amp_funding_detail,amp_organisation,amp_currency ";
			sql += "where amp_components.amp_component_id=amp_physical_performance.amp_component_id ";
			sql += "and amp_components.amp_activity_id=amp_activity.amp_activity_id ";
			sql += "and amp_activity.amp_activity_id=amp_funding.amp_activity_id ";
			sql += "and amp_funding.amp_donor_org_id=amp_organisation.amp_org_id ";
			sql += "and amp_funding.amp_funding_id=amp_funding_detail.amp_funding_id ";
			sql += "and amp_funding_detail.amp_currency_id=amp_currency.amp_currency_id ";
			sql += "and amp_components.currency_id=amp_currency.amp_currency_id ";
			sql += "and org_role_code='MA' and amp_activity.amp_activity_id='" + ampActivityId + "' ";
			sql += "order by amp_organisation.amp_org_id,amp_activity.amp_activity_id,amp_components.amp_component_id,amp_physical_performance.reporting_date";
			// Query #14
			t1 = System.currentTimeMillis();										
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			logger.debug("Query #14 executed in " + (t2-t1) + "ms");			

			logger.debug("All the statements got executed");
			con.close();
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException from updateReportCache() " + e.getMessage());
			e.printStackTrace(System.out);
		} catch (SQLException e) {
			logger.error("SQLException from updateReportCache() " + e.getMessage());
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);			
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception ex) {
				logger.error("Cannot close the connection");
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
		//System.out.println(" UpdateDB  Test1 passed");
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
			//System.out.println(val + "  ASD " + res);

		
			return(res);
		}
		catch(Exception e)
		{
					System.out.println( "ActivityStartDate :"+e);
		}
		return query;
	}


	String computeActivityCloseDate(String val)
	{
		//System.out.println("Test2 passed");
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
			//System.out.println(val + "  ACD " + res);


			return(res);
		}
		catch(Exception e)
		{
					System.out.println("ActivityCloseDate : "+e);
		}
		return query;
	}


	String computeActivityOrigCloseDate(String val)
	{
		//System.out.println("Test3 passed");
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
			//System.out.println(val + "  AOCD " + res);


			return(res);
		}
		catch(Exception e)
		{
					System.out.println(" ActivityOrigCloseDate :"+e);
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

		//System.out.println("Test4 passed");
		query = "update amp_activity set activity_start_date = "+sdd+", activity_close_date = "+cdd ;
		query+= " , original_comp_date ="+ocd+"  where amp_activity_id = "+ id;	
		System.out.println(query);		

		Statement stmt = connection.createStatement();
		int cols = stmt.executeUpdate(query);
		//System.out.println( cols +" columns updated");
		}
		catch(Exception e)
		{
			System.out.println(" updateAmpActivityDates :"+ e);
		}
	}

//-=======================================================================	
	
}
