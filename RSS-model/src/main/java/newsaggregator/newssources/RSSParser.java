package newsaggregator.newssources;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public abstract class RSSParser {

  private CacheMap<String, Object> cache;
  private String EMPTY_STRING = "";

  public RSSParser(CacheMap<String, Object> cache){
    this.cache = cache;
  }

  public URL getURL(String surl) {
    URL url = null;
    try {
      url = new URL(surl);
    } catch (MalformedURLException e) {
      System.out.println("Bad URL " + e);
    }
    return url;
  }


  private List<FeedStructure> getStories(Element channel) {
    List<FeedStructure> structures = new ArrayList<FeedStructure>();

    String topic = getTopic(channel.getChildren("title").get(0).getValue());
    List<Element> stories = channel.getChildren("item");

    for (Element story : stories) {
      String guid = getGUID(story);

      if(!guid.equals(EMPTY_STRING)) {

        if(!cache.containsKey(guid)) {
          String title = getTitle(story);
          String description = getDescription(story);
          String link = getLink(story);
          String pubDate = getPubDate(story);
          String image = getImage(story);
          String author = getAuthor(story);
          String source = getSource();
//          List<String> tags = getEntities(title, description);
//          List<String> extraTags = getTags(story);
//          Optional.ofNullable(extraTags).ifPresent(tags::addAll);
          FeedStructure structure = new FeedStructure(title, description, link, image, author, guid,
                                                   source, pubDate, topic, null);
          structures.add(structure);
          cache.put(guid, null);
        }
      }
    }
    return structures;
  }

//  private List<String> getEntities(String title, String desc) {
//
//    String entitySting = title.concat(" ").concat(desc);
//
//    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
//    try  {
//      LanguageServiceClient language = LanguageServiceClient.create();
//      com.google.cloud.language.v1.Document doc = com.google.cloud.language.v1.Document.newBuilder()
//          .setContent(entitySting)
//          .setType(Type.PLAIN_TEXT)
//          .build();
//      AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
//          .setDocument(doc)
//          .setEncodingType(EncodingType.UTF16)
//          .build();
//
//      AnalyzeEntitiesResponse response = language.analyzeEntities(request);
//
//      return response.getEntitiesList().stream().map(Entity::getName).collect(Collectors.toList());
//    } catch (Exception e) {
//      System.out.println("Entity Recognition Failed." + e);
//    }
//    return new ArrayList<>();
//  }

  public List<FeedStructure> parseRSS(URL url) {
    List<FeedStructure> structures = new ArrayList<FeedStructure>();
    try {
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(url);
      Element rootElem = document.getRootElement();
      Element channel = rootElem.getChildren().get(0);
      System.out.println(channel.getChildren("title").get(0).getValue());
      structures = getStories(channel);
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
    return structures;
  }

  public String getTitle(Element story) {
    Element title = story.getChild("title");
    if(title != null) {
      return title.getText();
    }
    return EMPTY_STRING;
  }

  public String getDescription(Element story) {
    Element description = story.getChild("description");
    if(description != null){
      return description.getText();
    }
    return EMPTY_STRING;
  }

  public String getLink(Element story) {
    Element link = story.getChild("link");
    if(link != null) {
      return link.getText();
    }
    return EMPTY_STRING;
  }

  public String getGUID(Element story) {
    Element guid = story.getChild("guid");
    if(guid != null) {
      return guid.getText();
    }
    return EMPTY_STRING;
  }

  public String getPubDate(Element story) {
    Element pubDate = story.getChild("pubDate");
    if (pubDate != null) {
      return pubDate.getText();
    }
    return EMPTY_STRING;
  }

  public abstract String getSource();

  public abstract String getTopic(String topic);

  public abstract String getImage(Element story);

  public abstract String getAuthor(Element story);

  public abstract List<String> getTags(Element story);

}