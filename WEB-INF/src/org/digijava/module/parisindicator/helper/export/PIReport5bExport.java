package org.digijava.module.parisindicator.helper.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.parisindicator.helper.PIReport4Row;
import org.digijava.module.parisindicator.helper.PIReport5bRow;
import org.digijava.module.parisindicator.helper.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;

public class PIReport5bExport extends PIAbstractExport {

	public PIReport5bExport(Site site, String langcode) {
		super(site, langcode);
	}

	@Override
	public Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception {
		Iterator iter = rows.iterator();
		ArrayList<AuxRow5b> list = new ArrayList();
		while (iter.hasNext()) {
			PIReport5bRow row = (PIReport5bRow) iter.next();
			AuxRow5b auxRow = new AuxRow5b();
			auxRow.setDonorGroup(row.getDonorGroup().getOrgGrpName());
			auxRow.setYear(new Integer(row.getYear()).toString());
			auxRow.setColumn1(FormatHelper.getDecimalFormat().format(row.getColumn1()));
			auxRow.setColumn2(FormatHelper.getDecimalFormat().format(row.getColumn2()));
			auxRow.setColumn3(new Integer(new Float(row.getColumn3()).intValue()).toString() + "%");
			list.add(auxRow);
		}

		return list;
	}

	public Map getParameters(int year) throws Exception {
		Map<String, String> parameters = new HashMap();
		parameters.put("PI_REPORT_NUMBER", TranslatorWorker.translateText("Paris Indicator", this.getLangCode(), this
				.getSite().getId())
				+ " "
				+ PIConstants.PARIS_INDICATOR_REPORT_5b
				+ " "
				+ TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL2", TranslatorWorker.translateText("Disbursement Year", this.getLangCode(), this
				.getSite().getId()));
		parameters.put("PI_COL3", TranslatorWorker.translateText(
				"Aid flows to the government sector that use national procurement procedures", this.getLangCode(), this
						.getSite().getId()));
		parameters.put("PI_COL4", TranslatorWorker.translateText("Total Aid flows disbursed to the government sector",
				this.getLangCode(), this.getSite().getId()));
		parameters.put("PI_COL5", TranslatorWorker.translateText(
				"Proportion of aid flows to the government sector using national procurement procedures", this
						.getLangCode(), this.getSite().getId()));
		parameters.put("PI_LAST_YEAR", new Integer(year).toString());
		return parameters;
	}

	public class AuxRow5b {
		private String donorGroup;
		private String year;
		private String column1;
		private String column2;
		private String column3;

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

		public AuxRow5b() {
		}
	}
}