package kicktraq.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	public static void main(String[] args) {
		try {
			Document doc = Jsoup.connect(
					"http://www.kickstarter.com/projects/cardboardcities/cardboardcities-illustrated-prints-and-cards/").get();
			
			Elements allElements = doc.getAllElements();
			
			Elements select = doc.select("h2[id=title]").select("a");
			
			for (Element elem : select) {
				System.out.println(elem.html());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
