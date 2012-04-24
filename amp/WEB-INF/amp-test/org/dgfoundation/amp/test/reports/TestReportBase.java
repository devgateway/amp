package org.dgfoundation.amp.test.reports;

import java.io.BufferedReader;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;

import junit.framework.Test;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.ComputedDateCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.test.categorymanager.api.CategoryManagerApiTest;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.ViewNewAdvancedReport;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.hibernate.Transaction;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public abstract class TestReportBase extends BasicActionTestCaseAdapter implements Test {
	private static Logger logger = Logger.getLogger(CategoryManagerApiTest.class);
	private Collection<AmpColumns> columnList = null;
	private Collection<AmpMeasures> measuresList = null;
	protected AmpARFilter filters=new AmpARFilter();
	protected MockHttpSession session;
	protected ViewNewAdvancedReport action;
	protected AdvancedReportForm form;
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
	protected GroupReportData generatedReport;
	private static Random random = new Random();

	DecimalFormat format = new DecimalFormat("#.00");
	private Long id = null;

	protected Integer getReportType() {
		return ArConstants.DONOR_TYPE;
	}

	public TestReportBase(String name) {
		super(name);
	}

	public TestReportBase() {
		super();
	}

	public Long getID() {
		if (this.id == null) {

			// get the range, casting to long to avoid overflow problems
			long range = 9999999 - 9000000;
			// compute a fraction of the range, 0 <= frac < range
			Double fraction = range * random.nextDouble();
			id = new Double(fraction + 9999999).longValue();

		}
		// System.out.println("generated id "+this.id);
		return id;
	};

	public String getReportName() {
		return this.getClass().getSimpleName();
	}

	public abstract Long[] getHierarchies() throws Exception;

	public Long getOwnerID() {
		TeamMember temMember = (TeamMember) session.getAttribute("currentMember");
		return temMember.getMemberId();
	};

	/**
	 * 1 Actual Commitments 2 Planned Commitments 4 Actual Disbursements 5
	 * Planned Disbursements
	 * 
	 * @return
	 * @throws Exception
	 */
	public Long[] getMeasures() throws Exception {

		return new Long[] { getMeasureByName("Actual Commitments").getMeasureId(), getMeasureByName("Actual Disbursements").getMeasureId(), getMeasureByName("Planned Commitments").getMeasureId(),
				getMeasureByName("Planned Disbursements").getMeasureId() };
	}

	/**
	 * @return
	 */
	public Long[] getColumns() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] { getColumnByName("Project Title").getColumnId() };
	}

	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		runSQLFile(new File(this.getClass().getResource("create_simulated_data.sql").getFile()));
		action = new ViewNewAdvancedReport();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(ViewNewAdvancedReport.class.getName(), action);

		form = (AdvancedReportForm) createActionForm(AdvancedReportForm.class);
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().createMockRequest();
		response = getActionMockObjectFactory().getMockResponse();

		TestUtil.setSiteDomain(request);
		TestUtil.setLocaleEn(request);
		TestUtil.setCurrentMemberFirstATLTeam(session);

		setValidate(true);
		buildReport();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		removeData();
	}

	public void removeData() throws Exception {
		Connection con = PersistenceManager.getSession().connection();
		con.createStatement().execute("DELETE FROM amp_report_hierarchy WHERE amp_report_id=" + getID());
		con.createStatement().execute("DELETE FROM amp_report_measures WHERE amp_report_id=" + getID());
		con.createStatement().execute("DELETE FROM amp_report_column WHERE amp_report_id=" + getID());
		con.createStatement().execute("DELETE FROM amp_reports WHERE amp_report_id=" + getID());
		con.commit();

	}

	protected void runSQLFile(File file) {
		String thisLine, sqlQuery;
		try {
			BufferedReader d = new BufferedReader(new java.io.FileReader(file));
			Connection con = PersistenceManager.getSession().connection();
			Statement stmt = con.createStatement();

			sqlQuery = "";
			while ((thisLine = d.readLine()) != null) {
				// Skip comments and empty lines
				if (thisLine.length() > 0 && thisLine.charAt(0) == '-' || thisLine.length() == 0)
					continue;
				sqlQuery = sqlQuery + " " + thisLine;
				// If one command complete
				if (sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
					sqlQuery = sqlQuery.replace(';', ' '); // Remove the ; since
					// jdbc complains

					stmt.execute(sqlQuery);

					sqlQuery = "";
				}
				con.commit();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

	}

	public boolean checkValues(List<AmountCell> items, Double... values) throws Exception {
		if (items.size() != values.length)
			throw new Exception("Different items and values length ");

		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == null && values[i] != null) {
				return false;
			}
			if (items.get(i) != null) {
				if (!format.format(items.get(i).getAmount()).equalsIgnoreCase(format.format(values[i]))) {
					return false;
				}
			}

		}
		return true;
	}

	public boolean checkStringValues(List<ComputedDateCell> items, String... values) throws Exception {
		if (items.size() != values.length)
			throw new Exception("Different items and values length ");

		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == null && values[i] != null) {
				return false;
			}
			if (items.get(i) != null) {
				if (!items.get(i).toString().equalsIgnoreCase(values[i])) {
					return false;
				}
			}

		}
		return true;
	}

	protected void buildReport() throws Exception {
//beginTransaction();
		Connection con = PersistenceManager.getSession().connection();
		con.setAutoCommit(false);

		con
				.createStatement()
				.execute(
						"INSERT INTO amp_reports  (amp_report_id, name, options, report_description, type, hide_activities, drilldown_tab, publicReport, ownerId, updated_date, cv_activity_level, ampPageId, allow_empty_fund_cols) "
								+ "VALUES (" + getID() + ", '" + getReportName() + "', 'A', '', " + getReportType() + ", False, False, False," + getOwnerID() + ", '2009-01-01', NULL,NULL, False)");

		Long[] columns = getColumns();
		for (int i = 0; i < columns.length; i++) {
			con.createStatement().execute("INSERT INTO amp_report_column    (amp_report_id, columnId, order_id, cv_level_id)   VALUES   (" + getID() + "," + columns[i] + "," + (i + 1) + ",116)");

		}

		Long[] hierarchies = getHierarchies();
		for (int i = 0; i < hierarchies.length; i++) {
			con.createStatement().execute("INSERT INTO amp_report_hierarchy (amp_report_id, columnId, levelId, cv_level_id)    VALUES   (" + getID() + "," + hierarchies[i] + "," + (i + 1) + ",116)");

		}

		Long[] measures = getMeasures();
		for (int i = 0; i < measures.length; i++) {
			con.createStatement().execute("INSERT INTO amp_report_measures  (amp_report_id,measureId,order_id) VALUES (" + getID() + "," + measures[i] + "," + (i + 1) + ")");
		}

		con.commit();
		con.setAutoCommit(true);
		//tx.commit();

		DecimalFormat format = FormatHelper.getDecimalFormatNotRounded();
		format.setMaximumFractionDigits(2);
		request.setSession(session);
		request.setupAddParameter("view", "reset");
		request.setupAddParameter("widget", "false");
		request.setupAddParameter("ampReportId", getID() + "");

		request.setupAddParameter("debugMode", "debugMode");
		request.setupAddParameter("ampCurrencyId", "USD");
		filters.setCurrency(CurrencyUtil.getAmpcurrency("USD"));
		session.setAttribute(ArConstants.REPORTS_FILTER, filters);
		generatedReport = ARUtil.generateReport(null, form, request, response);

	}

	public AmpColumns getColumnByName(String name) throws Exception {
		if (columnList == null) {
			columnList = AdvancedReportUtil.getColumnList();
		}

		for (AmpColumns col : columnList) {
			if (col.getColumnName().equalsIgnoreCase(name)) {
				return col;
			}
		}
		throw new Exception("Column Not found  " + name);
	}

	public AmpMeasures getMeasureByName(String name) throws Exception {
		if (measuresList == null) {
			measuresList = AdvancedReportUtil.getMeasureList();
		}

		for (AmpMeasures mes : measuresList) {
			if (mes.getMeasureName().equalsIgnoreCase(name)) {
				return mes;
			}
		}
		throw new Exception("Measures Not found " + name);
	}

	public abstract void testGrandTotalValues() throws Exception;

}
