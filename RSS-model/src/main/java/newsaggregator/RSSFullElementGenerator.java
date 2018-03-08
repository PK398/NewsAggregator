package newsaggregator;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import java.util.ArrayList;
import java.util.List;
import newsaggregator.newssources.FeedStructure;
import uk.gov.gchq.gaffer.commonutil.iterable.TransformOneToManyIterable;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.data.generator.OneToManyElementGenerator;

public class RSSFullElementGenerator implements OneToManyElementGenerator<FeedStructure> {

  @Override
  public Iterable<? extends Element> apply(Iterable<? extends FeedStructure> domainObjects) {
    return new TransformOneToManyIterable<FeedStructure, Element>(domainObjects) {
      protected Iterable<Element> transform(FeedStructure item) {
        return RSSFullElementGenerator.this._apply(item);
      }
    };
  }

  @Override
  public Iterable<Element> _apply(FeedStructure feedStructure) {
    List<Element> elements = new ArrayList<Element>();

    final HyperLogLogPlus shllp = new HyperLogLogPlus(5, 5);
    shllp.offer(feedStructure.getGuid());
    final HyperLogLogPlus dhllp = new HyperLogLogPlus(5, 5);
    dhllp.offer(feedStructure.getTopic());

    elements.add(new Edge.Builder()
        .group("StoryRelation")
        .source(feedStructure.getTopic())
        .dest(feedStructure.getGuid())
        .directed(false)
        .property("count", 1L)
        .build());

    elements.add(new Entity.Builder()
        .group("Topic")
        .vertex(feedStructure.getTopic())
        .property("count", 1L)
        .property("hllp", shllp)
        .build());

    elements.add(new Entity.Builder()
        .group("Story")
        .vertex(feedStructure.getGuid())
        .property("title", feedStructure.getTitle())
        .property("description", feedStructure.getDescription())
        .property("pubDate", feedStructure.getPubDate())
        .property("link", feedStructure.getLink())
        .property("image", feedStructure.getLinkToImage())
        .property("author", feedStructure.getAuthor())
        .property("source", feedStructure.getSource())
        .property("count", 1L)
        .property("hllp", dhllp)
        .build());

    return elements;
  }

//  @Override
//  public Iterable<Element> _apply(final FeedStructure feedStructure) {
//

//  }
}
