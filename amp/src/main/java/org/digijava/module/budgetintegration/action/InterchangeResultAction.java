package org.digijava.module.budgetintegration.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpInterchangeableResult;
import org.digijava.module.budgetintegration.form.InterchangeResultForm;
import org.digijava.module.budgetintegration.util.BudgetIntegrationUtil;
import org.digijava.module.common.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.*;

/**
 * Created by esoliani on 20/06/16.
 */
public class InterchangeResultAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(InterchangeResultAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOGGER.info("interchange result");
        if (form instanceof InterchangeResultForm) {
            InterchangeResultForm interchangeResultForm = (InterchangeResultForm) form;
            if("true".equals(request.getParameter("delete"))) {
                deleteResult(request);
            }
            if("true".equals(request.getParameter("deleteAll"))) {
                deleteResults(request);
            }
            if(StringUtils.isNotBlank(request.getParameter("page"))) {
                interchangeResultForm.setCurrentPage(Integer.valueOf(request.getParameter("page")));
            }
            String order = "date desc";
            if(StringUtils.isNotBlank(request.getParameter("sortBy"))) {
                if("dateAscending".equals(request.getParameter("sortBy"))) {
                    order = "date asc";
                } else if("dateDescending".equals(request.getParameter("sortBy"))) {
                    order = "date desc";
                }
            }
            String strFilterDate = interchangeResultForm.getFilterDate();
            Date filterDate = StringUtils.isNotBlank(strFilterDate) 
                    ? new Date(DateTimeUtil.parseDate(strFilterDate).getTime()) : null;
            Integer offset = interchangeResultForm.getOffset();
            Integer pageSize = interchangeResultForm.getPageSize();
            List<AmpInterchangeableResult> result = 
                    BudgetIntegrationUtil.getInterchangeResult(filterDate, offset, pageSize, order);
            Integer totalResults = BudgetIntegrationUtil.getInterchangeResultCount(filterDate);
            Integer lastPage = interchangeResultForm.getPageSize() == -1 
                    ? 1 : Math.floorDiv(totalResults, interchangeResultForm.getPageSize()) + 1;
            Integer currentPage = interchangeResultForm.getCurrentPage();
            int numberOfPages = Integer.min(lastPage, interchangeResultForm.getPagesToShow());
            if(currentPage > interchangeResultForm.getPagesToShow()) {
                numberOfPages += interchangeResultForm.getPagesToShow();
            }
            int pageOffset = Integer.max(1, currentPage - interchangeResultForm.getPagesToShow() + 1);
            List<Integer> pages = new ArrayList<>(numberOfPages);
            for(int i = pageOffset; i <= numberOfPages; i++) {
                pages.add(i);
            }
            interchangeResultForm.setResults(result);
            interchangeResultForm.setLastPage(lastPage);
            interchangeResultForm.setPages(pages);
        }
        return mapping.findForward("forward");
    }

    private void deleteResults(HttpServletRequest request) {
        String strIds = request.getParameter("ids");
        if(StringUtils.isNotBlank(strIds)) {
            String[] idsArray = strIds.split(",");
            Set<AmpInterchangeableResult> results = new HashSet<>();
            for(String id: idsArray) {
                results.add(new AmpInterchangeableResult(Long.valueOf(id)));
            }
            BudgetIntegrationUtil.deleteResult(results);
        }
    }

    private void deleteResult(HttpServletRequest request) {
        String strId = request.getParameter("id");
        if(StringUtils.isNotBlank(strId)) {
            Long id = Long.valueOf(strId);
            BudgetIntegrationUtil.deleteResult(Collections.singleton(new AmpInterchangeableResult(id)));
        }
    }
}
