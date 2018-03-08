package newsaggregator.newssources;

import org.jdom2.Element;
import org.jdom2.Namespace;

public class Guardian extends RSSParser {

  @Override
  public String getTopic(String topic) {
    return null;
  }

  @Override
  public String getImage(Element story) {
    Element image = story
        .getChild("content", Namespace.getNamespace("http://search.yahoo.com/mrss/"));
    return image.getAttributeValue("url");
  }

  @Override
  public String getAuthor(Element story) {
    Element author = story
        .getChild("creator", Namespace.getNamespace("http://purl.org/dc/elements/1.1/"));
    return author.getText();
  }
}
