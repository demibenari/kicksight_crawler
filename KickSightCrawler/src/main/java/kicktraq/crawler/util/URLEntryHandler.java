package kicktraq.crawler.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;

import kicktraq.crawler.entities.KickProject;

public class URLEntryHandler {
	private Logger LOG = Logger.getLogger(URLEntryHandler.class);
	
	public void writeURLInfoToFile(File outPutFile, Map<KickProject, KickProject> projectsMap) {
		try {
			LOG.info("Got Map With " + projectsMap.size() + " entries");
			LOG.info("=================================================");
			
			Writer projectWriter = new FileWriter(outPutFile);
			
			for(KickProject project : projectsMap.keySet()) {
				projectWriter.write(project.getProjectKickStarterURL() + "," + project.getProjectType() + "\n");
			}
			
			projectWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<KickProject, KickProject> readURLInfoFromFile(File inputFile) {
		return null;
	}
}
