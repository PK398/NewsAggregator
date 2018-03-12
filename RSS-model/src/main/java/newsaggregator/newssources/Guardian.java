package newsaggregator.newssources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class Guardian extends RSSParser {

  public Guardian(CacheMap<String, Object> cache) {
    super(cache);
  }

  @Override
  public String getSource() {
    return "Guardian";
  }

  @Override
  public String getTopic(String topic) {
    String[] s = topic.split(" \\|");
    if (s[0].equals("Politics")) {
      return "UK Politics";
    }
    return s[0];
  }

  @Override
  public String getImage(Element story) {
    Element image = story
        .getChild("content", Namespace.getNamespace("http://search.yahoo.com/mrss/"));
    if(image != null){
      return image.getAttributeValue("url");
    }
    return "";
  }

  @Override
  public String getAuthor(Element story) {
    Element author = story
        .getChild("creator", Namespace.getNamespace("http://purl.org/dc/elements/1.1/"));
    return author.getText();
  }

  @Override
  public List<String> getTags(Element story) {
    List<Element> elements = story.getChildren("category");
    if(elements != null){
      return elements.stream().map(Element::getText).collect(Collectors.toList());
    }
    return new ArrayList<>();
  }
}
