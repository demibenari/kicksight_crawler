package kicktraq.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import kicktraq.crawler.entities.KickProject;
import kicktraq.crawler.util.URLEntryHandler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	private static List<String> m_seedURLs = new ArrayList<String>();
	
	static {
		m_seedURLs.add("http://www.kicktraq.com/archive/?page=");
	}
	
    public static void main(String[] args) throws Exception {
            String crawlStorageFolder = "/data/crawl/root";
            int numberOfCrawlers = 5;

            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setMaxDepthOfCrawling(1);

            /*
             * Instantiate the controller for this crawl.
             */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            
            for(int index = 1; index < 300 ; index++) {
            	controller.addSeed("http://www.kicktraq.com/archive/?page=" + index);	
            }
            
            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(KickTraqCrawler.class, numberOfCrawlers);   
            
            List<Object> crawlersLocalData = controller.getCrawlersLocalData();
            
            controller.shutdown();
            Map<KickProject, KickProject> resultMap = new ConcurrentHashMap<KickProject, KickProject>();
            
            for (Object map : crawlersLocalData) {
            	@SuppressWarnings("unchecked")
				Map<KickProject, KickProject> currMap  = (Map<KickProject, KickProject>) map;
            	
            	for (Entry<KickProject, KickProject> entry : currMap.entrySet()) {
            		resultMap.put(entry.getKey(), entry.getValue());
            	}
            }
            
            URLEntryHandler handler = new URLEntryHandler();
            
            handler.writeURLInfoToFile(new File("c:/temp/logs/output.csv"), resultMap);
    }
}