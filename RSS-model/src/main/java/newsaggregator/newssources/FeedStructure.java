package newsaggregator.newssources;

public class FeedStructure {

  private final String title;
  private final String description;
  private final String link;
  private final String linkToImage;
  private final String author;
  private final String guid;
  private final String source;
  private final String pubDate;
  private final String topic;

  public FeedStructure(String title, String description, String link, String linkToImage,
      String author, String guid, String source, String pubDate, String topic) {
    this.title = title;
    this.description = description;
    this.link = link;
    this.linkToImage = linkToImage;
    this.author = author;
    this.guid = guid;
    this.source = source;
    this.pubDate = pubDate;
    this.topic = topic;
  }

  public String getTopic() {
    return topic;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLink() {
    return link;
  }

  public String getLinkToImage() {
    return linkToImage;
  }

  public String getAuthor() {
    return author;
  }

  public String getGuid() {
    return guid;
  }

  public String getSource() {
    return source;
  }

  public String getPubDate() {
    return pubDate;
  }

  @Override
  public String toString() {
    return "RSS [title=" + title + ", description=" + description
        + ", link=" + link + ", linkToImage=" + linkToImage + ", author=" + author +
        ", guid=" + guid + ", source=" + source + ", pubDate=" + pubDate + "]";
  }
}
