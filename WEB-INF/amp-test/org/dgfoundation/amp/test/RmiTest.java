package org.dgfoundation.amp.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.digijava.module.aim.helper.Constants;

public class RmiTest {

	public static void main(String[] args) {
		java.util.Hashtable<String, String> env = new java.util.Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, Constants.JNP_URL);
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");

		try {
			InitialContext ctx = new InitialContext(env);
			DataSource ds = (DataSource) ctx.lookup("localDS");
			java.sql.Connection con = ds.getConnection();
			PreparedStatement prs=con.prepareStatement("select reports0_.amp_member_id as amp1_7_, reports0_.amp_report_id as amp2_7_, ampreports1_.amp_report_id as amp1_100_0_, ampreports1_.name as name100_0_, ampreports1_.options as options100_0_, ampreports1_.report_description as report4_100_0_, ampreports1_.type as type100_0_, ampreports1_.hide_activities as hide6_100_0_, ampreports1_.drilldown_tab as drilldown7_100_0_, ampreports1_.publicReport as publicRe8_100_0_, ampreports1_.allow_empty_fund_cols as allow9_100_0_, ampreports1_.ownerId as ownerId100_0_, ampreports1_.cv_activity_level as cv11_100_0_, ampreports1_.updated_date as updated12_100_0_, ampreports1_.ampPageId as ampPageId100_0_, ampteammem2_.amp_team_mem_id as amp1_95_1_, ampteammem2_.read_permission as read2_95_1_, ampteammem2_.write_permission as write3_95_1_, ampteammem2_.delete_permission as delete4_95_1_, ampteammem2_.user_ as user5_95_1_, ampteammem2_.amp_team_id as amp6_95_1_, ampteammem2_.amp_member_role_id as amp7_95_1_, user3_.ID as ID3_2_, user3_.CREATION_DATE as CREATION2_3_2_, user3_.CREATION_IP as CREATION3_3_2_, user3_.LAST_MODIFIED as LAST4_3_2_, user3_.MODIFYING_IP as MODIFYING5_3_2_, user3_.FIRST_NAMES as FIRST6_3_2_, user3_.LAST_NAME as LAST7_3_2_, user3_.EMAIL as EMAIL3_2_, user3_.EMAIL_VERIFIED as EMAIL9_3_2_, user3_.EMAIL_BOUNCING as EMAIL10_3_2_, user3_.NO_ALERTS_UNTIL as NO11_3_2_, user3_.PASSWORD as PASSWORD3_2_, user3_.SALT as SALT3_2_, user3_.PASS_QUESTION as PASS14_3_2_, user3_.PASS_ANSWER as PASS15_3_2_, user3_.URL as URL3_2_, user3_.ACTIVE as ACTIVE3_2_, user3_.BANNED as BANNED3_2_, user3_.REFERRAL as REFERRAL3_2_, user3_.ORGANIZATION_TYPE_OTHER as ORGANIZ20_3_2_, user3_.ADDRESS as ADDRESS3_2_, user3_.COUNTRY_ISO as COUNTRY22_3_2_, user3_.assigned_org_id as assigned23_3_2_, user3_.image_id as image24_3_2_, user3_.ORGANIZATION_TYPE_ID as ORGANIZ25_3_2_, user3_.REGISTRATION_LANGUAGE as REGISTR26_3_2_, user3_.GLOBAL_ADMIN as GLOBAL27_3_2_, user3_.REGISTERED_THROUGH as REGISTERED28_3_2_, ampteam4_.amp_team_id as amp1_89_3_, ampteam4_.name as name89_3_, ampteam4_.access_type as access3_89_3_, ampteam4_.description as descript4_89_3_, ampteam4_.add_activity as add5_89_3_, ampteam4_.computation as computat6_89_3_, ampteam4_.team_lead_id as team7_89_3_, ampteam4_.type_categoryvalue_id as type8_89_3_, ampteam4_.parent_team_id as parent9_89_3_, ampteam4_.team_category as team10_89_3_, ampteam4_.related_team_id as related11_89_3_, ampteammem5_.amp_team_mem_role_id as amp1_94_4_, ampteammem5_.role as role94_4_, ampteammem5_.description as descript3_94_4_, ampteammem5_.read_permission as read4_94_4_, ampteammem5_.write_permission as write5_94_4_, ampteammem5_.delete_permission as delete6_94_4_, ampteammem5_.team_head as team7_94_4_, ampcategor6_.id as id179_5_, ampcategor6_.category_value as category2_179_5_, ampcategor6_.amp_category_class_id as amp3_179_5_, ampcategor6_.index_column as index4_179_5_, ampcategor7_.id as id177_6_, ampcategor7_.category_name as category2_177_6_, ampcategor7_.keyName as keyName177_6_, ampcategor7_.description as descript4_177_6_, ampcategor7_.is_multiselect as is5_177_6_, ampcategor7_.is_ordered as is6_177_6_ from AMP_MEMBER_REPORTS reports0_ left outer join AMP_REPORTS ampreports1_ on reports0_.amp_report_id=ampreports1_.amp_report_id left outer join AMP_TEAM_MEMBER ampteammem2_ on ampreports1_.ownerId=ampteammem2_.amp_team_mem_id left outer join DG_USER user3_ on ampteammem2_.user_=user3_.ID left outer join AMP_TEAM ampteam4_ on ampteammem2_.amp_team_id=ampteam4_.amp_team_id left outer join AMP_TEAM_MEMBER_ROLES ampteammem5_ on ampteammem2_.amp_member_role_id=ampteammem5_.amp_team_mem_role_id left outer join AMP_CATEGORY_VALUE ampcategor6_ on ampreports1_.cv_activity_level=ampcategor6_.id left outer join AMP_CATEGORY_CLASS ampcategor7_ on ampcategor6_.amp_category_class_id=ampcategor7_.id where reports0_.amp_member_id=1");
			ResultSet rs=prs.executeQuery();
			rs.next();
			System.out.println(rs.getString(1)+"\n\r");
			System.out.println(rs.getString(2)+"\n\r");
			System.out.println(rs.getString(3)+"\n\r");
			System.out.println(rs.getString(4)+"\n\r");
			System.out.println(rs.getString(5)+"\n\r");
			System.out.println(rs.getString(6)+"\n\r");
			System.out.println(rs.getString(7)+"\n\r");
			System.out.println(rs.getString(8)+"\n\r");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
