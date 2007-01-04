
package org.digijava.module.aim.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.form.NationalPlaningDashboardForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import java.util.*;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.helper.IndicatorValuesBean;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ActivityUtil;

public class NationalPlaningDashboardAction extends DispatchAction {

  public ActionForward display(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws
      Exception {
    return doDisplay(mapping, form, request, response, false);
  }

  public ActionForward displayWithFilter(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws
      Exception {
    return doDisplay(mapping, form, request, response, true);
  }

  protected ActionForward doDisplay(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    boolean filter) throws
      Exception {
    NationalPlaningDashboardForm npdForm =
        (NationalPlaningDashboardForm) form;

    // load all themes
    List themes = ProgramUtil.getAllThemes(true);
    Collection sortedThemes = CollectionUtils.getFlatHierarchy(themes, true,
        new ProgramHierarchyDefinition(),
        new HierarchicalProgramComparator());
    Long currentThemeId = npdForm.getCurrentProgramId();
    AmpTheme currentTheme = getCurrentTheme(themes, currentThemeId);

    if (currentTheme != null) {
      npdForm.setCurrentProgramId(currentTheme.getAmpThemeId());
    }
    npdForm.setCurrentProgram(currentTheme);
    List valueBeans=getIndicatorValues(currentTheme);
    npdForm.setValuesForSelectedIndicators(valueBeans);

    npdForm.setPrograms(new ArrayList(sortedThemes));

    if (currentTheme != null) {
      if (!filter) {
        long[] ids = getIndicatorIds(currentTheme);
        npdForm.setSelectedIndicators(ids);
      }

      String statuseCode=(npdForm.getSelectedStatuses()==null)?null:npdForm.getSelectedStatuses()[0];
      Long locationId=(npdForm.getSelectedLocations()==null)?null:new Long(npdForm.getSelectedLocations()[0]);

      Collection activities=ActivityUtil.searchActivities(currentThemeId,statuseCode,null,null,locationId);
      npdForm.setActivities(getActivities(currentTheme));

    }

    npdForm.setYears(ProgramUtil.getYearsBeanList());
    npdForm.setDonors(DbUtil.getAllDonorOrgs());
    npdForm.setActivityStatuses(DbUtil.getAmpStatus());
    npdForm.setLocations(LocationUtil.getAmpLocations());

    return mapping.findForward("viewNPDDashboard");
  }

  private List getIndicatorValues(AmpTheme theme){
      List result=null;
      /** @todo This shoud be implemented correctly to work faster */
      if (theme!=null && theme.getIndicators()!=null){
          for (Iterator iter = theme.getIndicators().iterator(); iter.hasNext(); ) {
              AmpThemeIndicators indicat = (AmpThemeIndicators) iter.next();
              Collection indValues = ProgramUtil.getThemeIndicatorValuesDB(indicat.getAmpThemeIndId());
              if (indValues!=null && indValues.size()>0){
                  if(result==null){
                      result=new ArrayList();
                  }
                  for (Iterator valIterator = indValues.iterator(); valIterator.hasNext(); ) {
                      AmpThemeIndicatorValue dbValue = (AmpThemeIndicatorValue) valIterator.next();
                      IndicatorValuesBean indValueBean=new IndicatorValuesBean(dbValue);
                      result.add(indValueBean);
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
    }
    else {
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

  public ActionForward displayChart(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws
      Exception {
    NationalPlaningDashboardForm npdForm =
        (NationalPlaningDashboardForm) form;

    Long currentThemeId = npdForm.getCurrentProgramId();
    AmpTheme currentTheme = ProgramUtil.getTheme(currentThemeId);

    CategoryDataset dataset = createDataset(currentTheme,
                                            npdForm.getSelectedIndicators());
    JFreeChart chart = ChartUtil.createChart(dataset,
                                             ChartUtil.CHART_TYPE_STACKED_BARS);

    response.setContentType("image/png");
    ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 500, 500);

    return null;
  }

  /**
   * Iterates over all themes and finds the theme which must be current. If
   * there is no currentThemeId passed, simply returns the first theme in the
   * list. Otherwise returns theme by the given currentThemeId;
   * @param themes List
   * @param currentThemeId Long
   * @return AmpTheme
   */
  private AmpTheme getCurrentTheme(List themes,
                                   Long currentThemeId) {
    AmpTheme currentTheme = null;
    Iterator iter = themes.iterator();
    while (iter.hasNext()) {
      AmpTheme item = (AmpTheme) iter.next();
      boolean isEqual = false;

      if (currentTheme == null |
          (currentThemeId != null &&
           (isEqual = item.getAmpThemeId().equals(currentThemeId)))) {
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
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws
      Exception {
    NationalPlaningDashboardForm npdForm =
        (NationalPlaningDashboardForm) form;
    npdForm.setShowChart(true);
    return display(mapping, form, request, response);
  }

  private static CategoryDataset createDataset(AmpTheme currentTheme, long[] selectedIndicators) {

    String[] series = {"Actual", "Target", "Base"};

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    if (selectedIndicators != null) {
      Arrays.sort(selectedIndicators);

      dataset = new DefaultCategoryDataset();

      Iterator iter = currentTheme.getIndicators().iterator();
      while (iter.hasNext()) {
        AmpThemeIndicators item = (AmpThemeIndicators) iter.next();

        int pos = Arrays.binarySearch(selectedIndicators,item.getAmpThemeIndId().longValue());

        if (pos >= 0) {
          String displayLabel = item.getName();
          try {
            Collection indValues = ProgramUtil.getThemeIndicatorValues(item.getAmpThemeIndId());
            for (Iterator valueIter = indValues.iterator(); valueIter.hasNext(); ) {
              AmpPrgIndicatorValue valueItem = (AmpPrgIndicatorValue) valueIter.next();
              dataset.addValue(valueItem.getValAmount(),series[valueItem.getValueType()], displayLabel);
              System.out.println((valueItem.getValAmount()+", "+series[valueItem.getValueType()]+", "+displayLabel));
            }
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }

    }
    return dataset;

  }

  public static List getActivities(AmpTheme theme) throws HibernateException,
      SQLException, DgException {
    Session session = null;
    Query qry = null;

    session = PersistenceManager.getRequestDBSession();
    String queryString = "from " + AmpActivity.class.getName() +
        " ampAct where ampAct.themeId.ampThemeId=:ampThemeId " +
        " and ampAct.status.statusCode=:statusCode order by ampAct.name";

    qry = session.createQuery(queryString);
    qry.setParameter("ampThemeId", theme.getAmpThemeId());
    qry.setParameter("statusCode", "1"); // 1 stands for "Planned"

    return qry.list();
  }

}

class HierarchicalProgramComparator implements Comparator {
  public int compare(Object o1, Object o2) {
    AmpTheme i1 = (AmpTheme) o1;
    AmpTheme i2 = (AmpTheme) o2;

    Long sk1 = i1.getAmpThemeId();
    Long sk2 = i2.getAmpThemeId();

    return sk1.compareTo(sk2);
  }
}

class ProgramHierarchyDefinition implements HierarchyDefinition {
  public Object getObjectIdentity(Object object) {
    AmpTheme i = (AmpTheme) object;
    return i.getAmpThemeId();

  }

  public Object getParentIdentity(Object object) {
    AmpTheme i = (AmpTheme) object;
    if (i.getParentThemeId() == null) {
      return null;
    }
    else {
      return i.getParentThemeId().getAmpThemeId();
    }
  }
}
