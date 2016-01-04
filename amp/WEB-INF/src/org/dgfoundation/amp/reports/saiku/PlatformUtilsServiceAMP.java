package org.dgfoundation.amp.reports.saiku;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.mondrian.util.AmpMondrianSchemaProcessor;
import org.saiku.service.PlatformUtilsService;
import org.saiku.service.util.dto.Plugin;

public class PlatformUtilsServiceAMP extends PlatformUtilsService {
	@Override
	public ArrayList getAvailablePlugins() {

		ArrayList l = new ArrayList<Plugin>();
		String filePath = PlatformUtilsServiceAMP.class.getResource(this.getPath()).getPath();
		File f = new File(filePath);

		String[] directories = f.list(new FilenameFilter() {
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		if (directories != null && directories.length > 0) {
			for (String d : directories) {
				File subdir = new File(filePath + "/" + d);
				File[] subfiles = subdir.listFiles();

				/**
				 * TODO use a metadata.js file for alternative details.
				 */
				if (subfiles != null) {
					for (File s : subfiles) {
						if (s.getName().equals("plugin.js")) {
							Plugin p = new Plugin(s.getParentFile().getName(),
									"", "js/saiku/plugins/"
											+ s.getParentFile().getName()
											+ "/plugin.js");
							l.add(p);
						}
					}
				}
			}
		}
		return l;
	}

}
