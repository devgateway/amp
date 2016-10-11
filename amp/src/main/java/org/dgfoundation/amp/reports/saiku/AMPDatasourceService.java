package org.dgfoundation.amp.reports.saiku;

import java.util.List;

import org.digijava.kernel.ampapi.saiku.util.SaikuUtils;
import org.digijava.module.aim.dbentity.AmpAnalyticalReport;
import org.saiku.repository.IRepositoryObject;
import org.saiku.service.datasource.DatasourceService;

public class AMPDatasourceService extends DatasourceService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3899359749341692312L;
    public List<IRepositoryObject> getFiles(String type, String username, List<String> roles) {
    	List<IRepositoryObject> list = SaikuUtils.getReports();
        return list;
    }

    public String getFileData(String name, String username, List<String> roles){
    	AmpAnalyticalReport report = SaikuUtils.getReports(name);
    	return report.getData();
    }

    public String saveFile(String data, String id, String author, List<String> roles) { 
    	AmpAnalyticalReport report = SaikuUtils.createReport(id, data);
    	report.setName(id.replace(".saiku", ""));
    	SaikuUtils.saveReport(report);
    	return data; 
    }
}
