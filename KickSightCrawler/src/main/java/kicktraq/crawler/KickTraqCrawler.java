package kicktraq.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import kicktraq.crawler.entities.KickProject;

import org.apache.log4j.Logger;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class KickTraqCrawler extends WebCrawler {
		private static final String ARCHIVED_PATH = "/archive/";
		private static final String PROJECTS_PATH_PREFIX = "/projects";
		
		Map<Integer, String> m_pages = new ConcurrentHashMap<Integer, String>();
	
		Map<KickProject, KickProject> m_projects = new ConcurrentHashMap<KickProject, KickProject>();
		
		private Logger LOG = Logger.getLogger(KickTraqCrawler.class);
	
        private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + 
                                                                      "|png|tiff?|mid|mp2|mp3|mp4"
                                                                    + "|wav|avi|mov|mpeg|ram|m4v|pdf" + 
                                                                      "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

        /**
         * You should implement this function to specify whether the given url
         * should be crawled or not (based on your crawling logic).
         */
        @Override
        public boolean shouldVisit(WebURL url) {
                String href = url.getURL().toLowerCase();
                return !FILTERS.matcher(href).matches() && href.startsWith("http://www.kicktraq.com/projects/");
        }

        /**
         * This function is called when a page is fetched and ready to be processed
         * by your program.
         */
        @Override
        public void visit(Page page) {
                int docid = page.getWebURL().getDocid();
                String url = page.getWebURL().getURL();
                String path = page.getWebURL().getPath();

                HtmlParseData parseData = (HtmlParseData) page.getParseData();
                String html = parseData.getHtml();
                
                
                
                if (path.equals(ARCHIVED_PATH)) {
                	String[] split = url.split("//");
                	String last = split[split.length -1];
                	String[] pageNumberString = last.split("=");
                	String string = pageNumberString[pageNumberString.length - 1];
                	int pageNumber = Integer.valueOf(string);
                	m_pages.put(pageNumber, url);
                }
                else if (path.startsWith(PROJECTS_PATH_PREFIX)) {
                	registerProject(page.getWebURL());
                }
                
                printOut("Docid: " + docid);
                printOut("URL: " + url);
                printOut("Path: '" + path + "'");
                               
                printOut("=============");
        }
        
        private void printOut(String str) {
        	LOG.info(str);
        }
        
        @Override
        public void onBeforeExit() {
        	LOG.info("=====================================================");
        	LOG.info("Finishing Off");
        	LOG.info("=====================================================");
        	LOG.info("Have Read: " + m_pages.size() + " Pages");
        	LOG.info("They Are:");
        	
        	Set<Integer> keySet = m_pages.keySet();
        	
        	List<Integer> pages = new ArrayList<Integer>(keySet);
        	Collections.sort(pages);
        	
        	for (Integer pageIndex : pages) {
        		String string = m_pages.get(pageIndex);
        		LOG.info(string);
        	}
        	LOG.info("=====================================================");
        }
        
        @Override
    	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        	registerProject(webUrl);
    	}

		private void registerProject(WebURL webUrl) {
			KickProject project = new KickProject(webUrl.getURL(), KickProject.ARCHIVED_TYPE);
        	
        	if (m_projects.containsKey(project)) {
        		LOG.error("DuplicatProject: " + project.toString());
        	}

        	m_projects.put(project, project);
		}

        @Override
    	protected void onContentFetchError(WebURL webUrl) {
        	System.out.println("ContentFetchError URL: " + webUrl);
    	}
        
        @Override
    	public Object getMyLocalData() {
    		return m_projects;
    	}
}
