package org.webcrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {
  private static final Logger logger = LoggerFactory.getLogger(Controller.class);
  public static void main() throws Exception {
    CrawlConfig config = new CrawlConfig();

//    Set the temp dir to the windows temp
    config.setCrawlStorageFolder(System.getProperty("java.io.tmpdir"));
    config.setPolitenessDelay(500);
    config.setMaxDepthOfCrawling(16);
    config.setMaxPagesToFetch(20_000);
//    Revist this depending on what hw wants.
    config.setIncludeBinaryContentInCrawling(true);

    config.setResumableCrawling(false);

    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    controller.addSeed(Constants.NEWS_URL);

    int numberOfCrawlers = 8;

    CrawlController.WebCrawlerFactory<MyCrawler> factory = MyCrawler::new;

    controller.start(factory, numberOfCrawlers);

    List<Object> data = controller.getCrawlersLocalData();
    List<FetchNewsSite> fetchNewsSites = new ArrayList<>();
    List <VisitNewsSite> visitNewsSites = new ArrayList<>();
    List<UrlNewsSite> urlNewsSites = new ArrayList<>();
    for (Object d: data) {
      LocalStats s = (LocalStats) d;
      fetchNewsSites.addAll(s.getFetchNewsSites());
      visitNewsSites.addAll(s.getVisitNewsSites());
      urlNewsSites.addAll(s.getUrlNewsSites());
    }

    File fetchFoxNews = new File("./FetchFoxNews.csv");
    fetchFoxNews.createNewFile();
    File visitFoxNews = new File("./VisitFoxNews.csv");
    visitFoxNews.createNewFile();
    File urlFoxNews = new File("./UrlFoxNews.csv");
    urlFoxNews.createNewFile();

    try (FileWriter fetchFoxNewsReader = new FileWriter(fetchFoxNews);
        FileWriter visitFoxNewsReader = new FileWriter(visitFoxNews); FileWriter urlFoxNewsReader = new FileWriter(urlFoxNews))
    {
      CSVPrinter fetchPrinter = new CSVPrinter(fetchFoxNewsReader,
          CSVFormat.DEFAULT.withHeader("URL", "Status Code"));
      CSVPrinter visitPrinter = new CSVPrinter(visitFoxNewsReader,
          CSVFormat.DEFAULT.withHeader("URL", "Size (in bytes)", "Number of Outgoing Links", "Content Type"));
      CSVPrinter urlPrinter = new CSVPrinter(urlFoxNewsReader, CSVFormat.DEFAULT.withHeader("URL", "In Website"));

      for(FetchNewsSite fetchNewsSite : fetchNewsSites) {
        fetchPrinter.printRecord(fetchNewsSite.URL(), fetchNewsSite.httpStatus());
      }
      fetchPrinter.flush();

      for (var visitNewsSite: visitNewsSites) {
        visitPrinter.printRecord(visitNewsSite.URL(), visitNewsSite.sizeInBytes(),
            visitNewsSite.numberOfOutlinks(), visitNewsSite.contentType());
      }
      visitPrinter.flush();

      for (var urlNewsSite : urlNewsSites) {
        urlPrinter.printRecord(urlNewsSite.getURL(), urlNewsSite.getInWebsite().getCode());
      }
      urlPrinter.flush();

    }
  }

}
