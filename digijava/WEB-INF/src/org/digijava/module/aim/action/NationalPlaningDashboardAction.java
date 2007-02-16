package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.form.NationalPlaningDashboardForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.IndicatorValuesBean;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class NationalPlaningDashboardAction extends DispatchAction {

	public ActionForward display(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return doDisplay(mapping, form, request, response, false);
	}

	public ActionForward displayWithFilter(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return doDisplay(mapping, form, request, response, true);
	}

	protected ActionForward doDisplay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			boolean filter) throws Exception {
		NationalPlaningDashboardForm npdForm = (NationalPlaningDashboardForm) form;
		System.out.println(filter);
		// load all themes
		List themes = ProgramUtil.getAllThemes(true);
		Collection sortedThemes = CollectionUtils.getFlatHierarchy(themes,
				true, new ProgramUtil.ProgramHierarchyDefinition(),
				new ProgramUtil.HierarchicalProgramComparator());
		npdForm.setPrograms(new ArrayList(sortedThemes));

		Long currentThemeId = npdForm.getCurrentProgramId();

		// set up tree
		Collection tree = CollectionUtils.getHierarchy(themes,
				new ProgramUtil.ProgramHierarchyDefinition());
		npdForm.setProgramTree(tree);
		// generate XML from hierarchy
		String xml = ProgramUtil.getThemesHierarchyXML(themes);
		npdForm.setXmlTree(xml);

		AmpTheme currentTheme = null;

		// according new requirments we should not display any info on initial
		// view of NPD.
		if (currentThemeId != null && currentThemeId.longValue() > 0) {
			currentTheme = getCurrentTheme(themes, currentThemeId);
			npdForm.setCurrentProgramId(currentTheme.getAmpThemeId());
			npdForm.setCurrentProgram(currentTheme);
		}

		if (currentTheme != null) {

			long[] ids = getIndicatorIds(currentTheme);
			npdForm.setSelectedIndicators(ids);

			Long locationId = null;
			if (npdForm.getSelectedLocations() != null
					&& npdForm.getSelectedLocations()[0] > 0) {
				locationId = new Long(npdForm.getSelectedLocations()[0]);
			}

			Date fromDate = null;
			if (npdForm.getFromYear() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.set(npdForm.getFromYear(), 0, 1, 0, 0, 0);
				fromDate = cal.getTime();
			}

			Date toDate = null;
			if (npdForm.getToYear() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.set(npdForm.getToYear(), 0, 1, 0, 0, 0);
				toDate = cal.getTime();
			}

			// Activitie filters : from date
			Date fromDateActivities = null;
			if (npdForm.getFromyearActivities() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.set(npdForm.getFromyearActivities(), 0, 1, 0, 0, 0);
				fromDateActivities = cal.getTime();
			}

			// Activitie filters : to date
			Date toDateActivities = null;
			if (npdForm.getToYearActivities() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.set(npdForm.getToYearActivities(), 0, 1, 0, 0, 0);
				toDateActivities = cal.getTime();
			}

			Long donorOrgId = null;
			if (npdForm.getSelectedDonors() != null
					&& npdForm.getSelectedDonors()[0] > 0) {
				donorOrgId = new Long(npdForm.getSelectedDonors()[0]);
			}

			// retrive activities
			Collection activities = ActivityUtil.searchActivities(
					currentThemeId, "1", donorOrgId, fromDateActivities,
					toDateActivities, locationId);
			if (activities != null) {
				npdForm.setActivities(new ArrayList(activities));
				DecimalFormat frmt=new DecimalFormat("$,###.##");
				String sum=frmt.format(getAmountSum(new ArrayList(activities)));
				npdForm.setFundingSum(sum);
			} else {
				npdForm.setActivities(new ArrayList());
				npdForm.setFundingSum(" 0 ");
			}

			List valueBeans = getIndicatorValues(currentTheme, fromDate, toDate);
			npdForm.setValuesForSelectedIndicators(valueBeans);

		}

		npdForm.setYears(ProgramUtil.getYearsBeanList());
		npdForm.setDonors(getDonorsList(30));
		npdForm.setLocations(LocationUtil.getAmpLocations());

		return mapping.findForward("viewNPDDashboard");
	}

	private List getIndicatorValues(AmpTheme theme, Date start, Date end) {
		List result = null;
		/** @todo This shoud be implemented correctly to work faster */
		if (theme != null && theme.getIndicators() != null) {
			for (Iterator iter = theme.getIndicators().iterator(); iter
					.hasNext();) {
				AmpThemeIndicators indicat = (AmpThemeIndicators) iter.next();
				Collection indValues = ProgramUtil
						.getThemeIndicatorValuesDB(indicat.getAmpThemeIndId());
				if (indValues != null && indValues.size() > 0) {
					if (result == null) {
						result = new ArrayList();
					}
					for (Iterator valIterator = indValues.iterator(); valIterator
							.hasNext();) {
						AmpThemeIndicatorValue dbValue = (AmpThemeIndicatorValue) valIterator
								.next();
						if ((start == null && end == null)
								|| ((dbValue.getCreationDate() != null) && ((start != null
										&& end == null && dbValue
										.getCreationDate().after(start))
										|| (start == null && end != null && dbValue
												.getCreationDate().before(end)) || (start != null
										&& end != null
										&& dbValue.getCreationDate().after(
												start) && dbValue
										.getCreationDate().before(end))

								))

						) {
							IndicatorValuesBean indValueBean = new IndicatorValuesBean(
									dbValue);
							result.add(indValueBean);
						}
					}
				}
			}
		}

		return result;
	}

	private long[] getIndicatorIds(AmpTheme currentTheme) {
		long[] ids = null;
		Set indicators = currentTheme.getIndicators();
		if (indicators == null) {
		} else {
			ids = new long[indicators.size()];
			int i = 0;
			Iterator iter = indicators.iterator();
			while (iter.hasNext()) {
				AmpThemeIndicators item = (AmpThemeIndicators) iter.next();
				ids[i++] = item.getAmpThemeIndId().longValue();
			}
		}
		return ids;
	}

	/**
	 * Returns Labelvaluebean's for Donors.
	 * 
	 * @param nameLimit
	 *            max number of chars for donor names
	 * @return Collection of LabelValueBean objects
	 */
	private Collection getDonorsList(int nameLimit) {
		Collection result = null;
		Collection dbDonors = DbUtil.getAllDonorOrgs();
		if (dbDonors != null) {
			result = new ArrayList();
			Iterator dbIter = dbDonors.iterator();
			System.out.println("======================================");
			while (dbIter.hasNext()) {
				AmpOrganisation donor = (AmpOrganisation) dbIter.next();
				String id = donor.getAmpOrgId().toString();
				String name = (donor.getName().length() > nameLimit) ? donor
						.getName().substring(0, nameLimit)
						+ "..." : donor.getName();
				System.out.println("===============>"+id);
				LabelValueBean lvBean = new LabelValueBean(name, id);
				result.add(lvBean);
			}
			System.out.println("======================================");
		}
		return result;
	}

	public ActionForward displayChart(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NationalPlaningDashboardForm npdForm = (NationalPlaningDashboardForm) form;

		Long currentThemeId = npdForm.getCurrentProgramId();

		CategoryDataset dataset = null;
		if (currentThemeId != null && currentThemeId.longValue() > 0) {
			AmpTheme currentTheme = ProgramUtil.getThemeObject(currentThemeId);

			dataset = createPercentsDataset(currentTheme, npdForm
					.getSelectedIndicators());
		}
		JFreeChart chart = ChartUtil.createChart(dataset,
				ChartUtil.CHRAT_TYPE_STACKED_BARS_PERCENTAGE);

		response.setContentType("image/png");
		ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 500,
				500);

		return null;
	}

	/**
	 * Iterates over all themes and finds the theme which must be current. If
	 * there is no currentThemeId passed, simply returns the first theme in the
	 * list. Otherwise returns theme by the given currentThemeId;
	 * 
	 * @param themes
	 *            List
	 * @param currentThemeId
	 *            Long
	 * @return AmpTheme
	 */
	private AmpTheme getCurrentTheme(List themes, Long currentThemeId) {
		AmpTheme currentTheme = null;
		Iterator iter = themes.iterator();
		while (iter.hasNext()) {
			AmpTheme item = (AmpTheme) iter.next();
			boolean isEqual = false;

			if (currentTheme == null
					| (currentThemeId != null && (isEqual = item
							.getAmpThemeId().equals(currentThemeId)))) {
				currentTheme = item;

				// Break the loop
				if (isEqual) {
					break;
				}
			}
		}
		return currentTheme;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NationalPlaningDashboardForm npdForm = (NationalPlaningDashboardForm) form;
		npdForm.setShowChart(true);
		return display(mapping, form, request, response);
	}

	private static CategoryDataset createDataset(AmpTheme currentTheme,
			long[] selectedIndicators) {

		String[] series = { "Target", "Actual", "Base" };

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (selectedIndicators != null) {
			Arrays.sort(selectedIndicators);

			dataset = new DefaultCategoryDataset();

			Iterator iter = currentTheme.getIndicators().iterator();
			while (iter.hasNext()) {
				AmpThemeIndicators item = (AmpThemeIndicators) iter.next();

				int pos = Arrays.binarySearch(selectedIndicators, item
						.getAmpThemeIndId().longValue());

				if (pos >= 0) {
					String displayLabel = item.getName();
					try {
						Collection indValues = ProgramUtil
								.getThemeIndicatorValues(item
										.getAmpThemeIndId());
						for (Iterator valueIter = indValues.iterator(); valueIter
								.hasNext();) {
							AmpPrgIndicatorValue valueItem = (AmpPrgIndicatorValue) valueIter
									.next();
							dataset.addValue(valueItem.getValAmount(),
									series[valueItem.getValueType()],
									displayLabel);
							System.out
									.println((valueItem.getValAmount() + ", "
											+ series[valueItem.getValueType()]
											+ ", " + displayLabel));
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

		}
		return dataset;

	}

	private static CategoryDataset createPercentsDataset(AmpTheme currentTheme,
			long[] selectedIndicators) {

		String[] series = { "Target", "Actual", "Base" };

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (selectedIndicators != null && currentTheme.getIndicators()!=null) {
			Arrays.sort(selectedIndicators);

			dataset = new DefaultCategoryDataset();

			Iterator iter = currentTheme.getIndicators().iterator();
			while (iter.hasNext()) {
				AmpThemeIndicators item = (AmpThemeIndicators) iter.next();

				int pos = Arrays.binarySearch(selectedIndicators, item
						.getAmpThemeIndId().longValue());

				if (pos >= 0) {
					String displayLabel = item.getName();
					try {
						Collection indValues = ProgramUtil
								.getThemeIndicatorValues(item
										.getAmpThemeIndId());
						Double actualValue = null;
						Double targetValue = null;
						Double baseValue = null;
						for (Iterator valueIter = indValues.iterator(); valueIter
								.hasNext();) {
							AmpPrgIndicatorValue valueItem = (AmpPrgIndicatorValue) valueIter
									.next();
							if (valueItem.getValueType() == 0) {
								targetValue = valueItem.getValAmount();
							}
							if (valueItem.getValueType() == 1) {
								actualValue = valueItem.getValAmount();
							}
							if (valueItem.getValueType() == 2) {
								baseValue = valueItem.getValAmount();
							}
						}
						if (actualValue == null) {
							actualValue = new Double(0.0);
						}
						if (targetValue == null) {
							targetValue = new Double(1.0);
						}
						actualValue = new Double(actualValue.doubleValue()
								/ targetValue.doubleValue());

						dataset.addValue(actualValue.doubleValue(), series[1],
								displayLabel);
						dataset.addValue(new Double(1.0 - actualValue
								.doubleValue()), series[0], displayLabel);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

		}
		return dataset;

	}

	public static Double getAmountSum(List actList) {
		if (actList != null) {
			Double mnt = new Double(0);
			AmpActivity act;
			Set funs;
			AmpFunding fun;
			AmpFundingDetail funDetales = null;
			;
			Iterator funItr = null;
			Iterator funDetalesItr = null;
			Iterator actItr = actList.iterator();
			while (actItr.hasNext()) {
				act = (AmpActivity) actItr.next();
				if (act.getFunAmount() != null) {

					double tpi = CurrencyWorker.convert(act.getFunAmount()
							.doubleValue(), act.getCurrencyCode());
					mnt = new Double(mnt.doubleValue() + tpi);
				}
				// funs=act.getFunding();
				// if(funs!=null){
				// funItr=funs.iterator();
				// while(funItr.hasNext()){
				// fun=(AmpFunding)funItr.next();
				// if(fun.getFundingDetails()!=null){
				// funDetalesItr=fun.getFundingDetails().iterator();
				// while(funDetalesItr.hasNext()){
				// funDetales=(AmpFundingDetail)funDetalesItr.next();
				// double
				// tpi=CurrencyWorker.convert(funDetales.getTransactionAmount().doubleValue(),funDetales.getAmpCurrencyId().getCurrencyCode());
				// mnt=new Double(mnt.doubleValue()+tpi);
				// }
				// }
				// }
				// }
			}
			return mnt;
		}
		return null;
	}
}
