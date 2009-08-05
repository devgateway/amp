package org.digijava.module.parisindicator.helper.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.parisindicator.helper.PIReport5aRow;
import org.digijava.module.parisindicator.helper.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;

public class PIReport5aExport extends PIAbstractExport {

	public PIReport5aExport(Site site, String langcode) {
		super(site, langcode);
	}

	@Override
	public Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception {
		Iterator iter = rows.iterator();
		ArrayList<AuxRow5a> list = new ArrayList();
		while (iter.hasNext()) {
			PIReport5aRow row = (PIReport5aRow) iter.next();
			AuxRow5a auxRow = new AuxRow5a();
			auxRow.setDonorGroup(row.getDonorGroup().getOrgGrpName());
			auxRow.setYear(new Integer(row.getYear()).toString());
			auxRow.setColumn1(FormatHelper.getDecimalFormat().format(row.getColumn1()));
			auxRow.setColumn2(FormatHelper.getDecimalFormat().format(row.getColumn2()));
			auxRow.setColumn3(FormatHelper.getDecimalFormat().format(row.getColumn3()));
			auxRow.setColumn4(FormatHelper.getDecimalFormat().format(row.getColumn4()));
			auxRow.setColumn5(FormatHelper.getDecimalFormat().format(row.getColumn5()));
			auxRow.setColumn6(new Integer(new Float(row.getColumn6()).intValue()).toString() + "%");
			auxRow.setColumn7(new Integer(new Float(row.getColumn7()).intValue()).toString() + "%");
			list.add(auxRow);
		}
		return list;
	}

	public Map getParameters(int year) throws Exception {
		Map<String, String> parameters = new HashMap();
		parameters.put("PI_REPORT_NUMBER", TranslatorWorker.translateText("Paris Indicator", this.getLangCode(), this
				.getSite().getId())
				+ " "
				+ PIConstants.PARIS_INDICATOR_REPORT_5a
				+ " "
				+ TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL2", TranslatorWorker.translateText("Disbursement Year", this.getLangCode(), this
				.getSite().getId()));
		parameters.put("PI_COL3", TranslatorWorker.translateText(
				"Aid flows to the goverment sector that use national budget execution procedures", this.getLangCode(),
				this.getSite().getId()));
		parameters.put("PI_COL4", TranslatorWorker.translateText(
				"Aid flows to the goverment sector that use national financial reporting procedures", this
						.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL5", TranslatorWorker.translateText(
				"Aid flows to the goverment sector that use national financial auditing procedures",
				this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL6", TranslatorWorker.translateText("ODA that uses all 3 national PFM",
				this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL7", TranslatorWorker.translateText("Total Aid flows disbursed to the government sector",
				this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL8", TranslatorWorker.translateText(
				"Proportion of aid flows to the government sector using one of the 3 country PFM systems", this
						.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL9", TranslatorWorker.translateText(
				"Proportion of aid flows to the government sector using all the 3 country PFM systems", this
						.getLangCode(), this.getSite().getId()));
		parameters.put("PI_LAST_YEAR", new Integer(year).toString());
		return parameters;
	}

	public class AuxRow5a {
		private String donorGroup;
		private String year;
		private String column1;
		private String column2;
		private String column3;
		private String column4;
		private String column5;
		private String column6;
		private String column7;

		public String getDonorGroup() {
			return donorGroup;
		}

		public void setDonorGroup(String donorGroup) {
			this.donorGroup = donorGroup;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getColumn1() {
			return column1;
		}

		public void setColumn1(String column1) {
			this.column1 = column1;
		}

		public String getColumn2() {
			return column2;
		}

		public void setColumn2(String column2) {
			this.column2 = column2;
		}

		public String getColumn3() {
			return column3;
		}

		public void setColumn3(String column3) {
			this.column3 = column3;
		}

		public AuxRow5a() {
		}

		public String getColumn4() {
			return column4;
		}

		public void setColumn4(String column4) {
			this.column4 = column4;
		}

		public String getColumn5() {
			return column5;
		}

		public void setColumn5(String column5) {
			this.column5 = column5;
		}

		public String getColumn6() {
			return column6;
		}

		public void setColumn6(String column6) {
			this.column6 = column6;
		}

		public String getColumn7() {
			return column7;
		}

		public void setColumn7(String column7) {
			this.column7 = column7;
		}
	}
}