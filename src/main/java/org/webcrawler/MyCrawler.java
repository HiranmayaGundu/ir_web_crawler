package org.webcrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mp3|mp4"
            + "|zip|gzmid|mp2|wav|avi|mov|mpeg|ram|m4v|rm|smil|wmv|swf|wma|rar|gz))$");
    private final LocalStats localStats;


    public MyCrawler() {
        localStats = new LocalStats();
    }



    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL();
//        logger.info("{}, {}", url.getSubDomain(), url.getDomain());
        boolean startsWith =
            (url.getSubDomain().isBlank() || url.getSubDomain().equalsIgnoreCase("www"))
                && url.getDomain().equalsIgnoreCase(Constants.DOMAIN);
        localStats.addNewURlSite(href, startsWith ? InWebsite.OK : InWebsite.N_OK);
        if(!FILTERS.matcher(href).matches()) {
            return startsWith;
        }
        return false;
    }

    @Override
    public void visit(Page webPage) {
        String url = webPage.getWebURL().getURL();
        logger.info("Crawler {} >> URL: {}", getMyId(), url);

        long numberOfOutlinks = 0;
        if (webPage.getParseData() instanceof HtmlParseData htmlParseData) {
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            numberOfOutlinks = links.size();
        }
        localStats.addNewVisitSite(url, webPage.getContentData().length, numberOfOutlinks,
            webPage.getContentType().split(";")[0] );
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        localStats.addNewNewsSite(webUrl.getURL(), statusCode);
    }

    @Override
    public Object getMyLocalData() {
        return localStats;
    }
}
