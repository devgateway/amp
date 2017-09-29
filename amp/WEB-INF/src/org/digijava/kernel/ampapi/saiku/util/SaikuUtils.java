/*
* Copyright 2012 OSBI Ltd
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.digijava.kernel.ampapi.saiku.util;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedSet;

import mondrian.util.Format;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.HTMLUtil;
import org.digijava.module.aim.dbentity.AmpAnalyticalReport;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.Position;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.dto.resultset.DataCell;
import org.saiku.olap.dto.resultset.MemberCell;
import org.saiku.olap.query2.ThinQuery;
import org.saiku.olap.util.OlapResultSetUtil;
import org.saiku.olap.util.SaikuProperties;
import org.saiku.repository.IRepositoryObject;
import org.saiku.repository.RepositoryFileObject;
import org.saiku.service.olap.totals.AxisInfo;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.TotalsListsBuilder;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;
import org.saiku.service.util.exception.SaikuServiceException;
import org.saiku.web.rest.objects.resultset.Total;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Stores any utility methods that are Saiku specific (e.g. use Saiku API)
 * @author Nadejda Mandrescu
 */
public class SaikuUtils {
    
      /*
       * 
       */
    public static List<IRepositoryObject> getReports() {
        Session session = null;
        try {
            session = PersistenceManager.getCurrentSession().getSessionFactory().openSession(); // is there any reason the current request's session won't do?
            AmpTeamMember ampTeamMember = getLoggedUser();
            Query query = session.createQuery("from AmpAnalyticalReport where owner = :owner ")
                    .setParameter("owner", ampTeamMember);

            List<AmpAnalyticalReport> list = query.list();
            List<IRepositoryObject> listFiles = new ArrayList<IRepositoryObject>();
            for(AmpAnalyticalReport report : list) {
                listFiles.add(new RepositoryFileObject(report.getName(), report.getName(), "", report.getName(), null));
            }
            return listFiles;
        }
        finally {
            PersistenceManager.closeSession(session);
        }
    }

    public static AmpAnalyticalReport getReport(int id) {
        Session session = PersistenceManager.getSession();
        AmpAnalyticalReport report = (AmpAnalyticalReport)session.get(AmpAnalyticalReport.class, id);
        return report;
    }

    public static void saveReport(AmpAnalyticalReport report) {
        Session session = PersistenceManager.getSession();
        AmpAnalyticalReport existingReport = getReports(report.getName());
        if(existingReport != null) {
            existingReport.setData(report.getData());
            report = existingReport;
        }
        
        if(report.getId() != null) {
            session.merge(report);
        }
        else
        {
            session.save(report);
        }
        session.flush();
    }

    public static AmpAnalyticalReport getReports(String name) {
        Session session = PersistenceManager.getSession();
        Query query = session.createQuery("from AmpAnalyticalReport where name = :name and owner = :owner ");
        query.setParameter("name", name);
        query.setParameter("owner", getLoggedUser());
        if(!query.list().isEmpty()) {
            AmpAnalyticalReport report = (AmpAnalyticalReport)query.list().get(0);
            return report;
        }
        return null;
    }

    public static AmpAnalyticalReport createReport(String name, String content) {
        AmpAnalyticalReport report = new AmpAnalyticalReport();
        report.setName(name);
        report.setData(content);
        report.setOwner(getLoggedUser());
        return report;
    }

    private static AmpTeamMember getLoggedUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        TeamMember tm = (TeamMember) attr.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
        return ampTeamMember;
    }
}
