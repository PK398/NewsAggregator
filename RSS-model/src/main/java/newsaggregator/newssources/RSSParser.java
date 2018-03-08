package newsaggregator.newssources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public abstract class RSSParser {

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
      Element title = story.getChild("title");
      System.out.println("title: " + title.getText());
      Element description = story.getChild("description");
      System.out.println("description: " + description.getText());
      Element link = story.getChild("link");
      System.out.println("link: " + link.getText());
      Element guid = story.getChild("guid");
      System.out.println("guid: " + guid.getText());
      Element pubDate = story.getChild("pubDate");
      System.out.println("pubDate: " + pubDate.getText());
      String image = getImage(story);
      System.out.println(image);
      String author = getAuthor(story);
      System.out.println(author);
      FeedStructure structure = new FeedStructure(title.getText(), description.getText(),
          link.getText(), image, author,
          guid.getText(), "", pubDate.getText(), topic);
      structures.add(structure);
    }

    return structures;
  }

  public List<FeedStructure> parseRSS(URL url) {
    List<FeedStructure> structures = new ArrayList<FeedStructure>();
    try {
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(url);
      Element rootElem = document.getRootElement();
      Element channel = rootElem.getChildren().get(0);
      System.out.println(channel.getChildren("title").get(0).getValue());
      structures = getStories(channel);
    } catch (JDOMException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return structures;
  }

  public abstract String getTopic(String topic);

  public abstract String getImage(Element story);

  public abstract String getAuthor(Element story);

}