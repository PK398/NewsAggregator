package newsaggregator.newssources;

import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class BBC extends RSSParser {

  public BBC(CacheMap<String, Object> cache) {
    super(cache);
  }

  @Override
  public String getSource() {
    return "BBC News";
  }

  @Override
  public String getTopic(String topic) {
    String[] strings = topic.split("- ");
    //System.out.println(strings[1]);
    return strings[1];

  }

  @Override
  public String getImage(Element story) {
    Element image = story
        .getChild("thumbnail", Namespace.getNamespace("http://search.yahoo.com/mrss/"));
    if(image != null) {
      image.getAttributeValue("url");
    }
    return "";
  }

  @Override
  public String getAuthor(Element story) {
    return "BBC News";
  }

  @Override
  public List<String> getTags(Element story) {
    return new ArrayList<>();
  }
}
