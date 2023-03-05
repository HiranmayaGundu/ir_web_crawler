package org.webcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

enum InWebsite {
  OK("OK"),
  N_OK("N_OK");

  private final String code;

  InWebsite(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return code;
  }
}

record FetchNewsSite(String URL, int httpStatus) {

  @Override
  public String toString() {
    return "NewsSite{" +
        "URL='" + URL + '\'' +
        ", httpStatus=" + httpStatus +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FetchNewsSite fetchNewsSite = (FetchNewsSite) o;
    return Objects.equals(URL, fetchNewsSite.URL) && Objects.equals(httpStatus,
        fetchNewsSite.httpStatus);
  }


}

record VisitNewsSite(String URL, Long sizeInBytes, Long numberOfOutlinks, String contentType) {

  @Override
  public String toString() {
    return "VisitNewsSite{" +
        "URL='" + URL + '\'' +
        ", sizeInBytes=" + sizeInBytes +
        ", numberOfOutlinks=" + numberOfOutlinks +
        ", contentType='" + contentType + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VisitNewsSite that = (VisitNewsSite) o;
    return Objects.equals(URL, that.URL) && Objects.equals(sizeInBytes,
        that.sizeInBytes) && Objects.equals(numberOfOutlinks, that.numberOfOutlinks)
        && Objects.equals(contentType, that.contentType);
  }

}

class UrlNewsSite {

  private final String URL;
  private final InWebsite inWebsite;

  UrlNewsSite(String url, InWebsite inWebsite) {
    this.URL = url;
    this.inWebsite = inWebsite;
  }

  @Override
  public String toString() {
    return "UrlNewsSite{" +
        "URL='" + URL + '\'' +
        ", inWebsite=" + inWebsite +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UrlNewsSite that = (UrlNewsSite) o;
    return Objects.equals(URL, that.URL) && inWebsite == that.inWebsite;
  }

  @Override
  public int hashCode() {
    return Objects.hash(URL, inWebsite);
  }

  public String getURL() {
    return URL;
  }

  public InWebsite getInWebsite() {
    return inWebsite;
  }
}

public class LocalStats {

  private final List<FetchNewsSite> fetchNewsSites;
  private final List<VisitNewsSite> visitNewsSites;

  private final List<UrlNewsSite> urlNewsSites;

  public LocalStats() {
    urlNewsSites = new ArrayList<>();
    fetchNewsSites = new ArrayList<>();
    visitNewsSites = new ArrayList<>();
  }

  public List<UrlNewsSite> getUrlNewsSites() {
    return urlNewsSites;
  }

  public List<FetchNewsSite> getFetchNewsSites() {
    return fetchNewsSites;
  }

  public List<VisitNewsSite> getVisitNewsSites() {
    return visitNewsSites;
  }

  public void addNewNewsSite(String URL, int httpStatus) {
    FetchNewsSite fetchNewsSite = new FetchNewsSite(URL, httpStatus);
    fetchNewsSites.add(fetchNewsSite);
  }

  public void addNewVisitSite(String URL, long sizeInBytes, long numberOfOutlinks, String contentType){
    VisitNewsSite visitNewsSite = new VisitNewsSite(URL, sizeInBytes, numberOfOutlinks, contentType);
    visitNewsSites.add(visitNewsSite);
  }

  public void addNewURlSite(String URL, InWebsite inWebsite){
    UrlNewsSite urlNewsSite = new UrlNewsSite(URL, inWebsite);
    urlNewsSites.add(urlNewsSite);
  }


}
