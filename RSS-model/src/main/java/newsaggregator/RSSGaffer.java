package newsaggregator;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import newsaggregator.newssources.BBC;
import newsaggregator.newssources.FeedStructure;
import uk.gov.gchq.gaffer.accumulostore.AccumuloProperties;
import uk.gov.gchq.gaffer.commonutil.StreamUtil;
import uk.gov.gchq.gaffer.commonutil.iterable.CloseableIterable;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.graph.GraphConfig;
import uk.gov.gchq.gaffer.operation.OperationChain;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.operation.impl.generate.GenerateElements;
import uk.gov.gchq.gaffer.operation.impl.get.GetAllElements;
import uk.gov.gchq.gaffer.user.User;

public class RSSGaffer {


  private void run() throws OperationException, IOException {

    final GraphConfig config = new GraphConfig.Builder()
        .graphId(getClass().getSimpleName())
        .build();

    final AccumuloProperties storeProperties = AccumuloProperties.loadStoreProperties(
        "/Users/pranav/Hack-a-Project/NewsAggregator/RSS-rest/src/main/resources/accumulo/store.properties");

    final Graph graph = new Graph.Builder()
        .config(config)
        .addSchemas(StreamUtil.openStreams(getClass(), "schema/"))
        .storeProperties(storeProperties)
        .build();

    System.out.println("\nschema loaded & graph built");

    final User user = new User(storeProperties.getUser());

    BBC bbc = new BBC();
    URL url = bbc.getURL("http://feeds.bbci.co.uk/news/rss.xml");
    List<FeedStructure> structures = bbc.parseRSS(url);
    for (FeedStructure structure : structures) {
      System.out.println(structure);
    }

    final OperationChain<Void> addOpChain = new OperationChain.Builder()
        .first(new GenerateElements.Builder<FeedStructure>()
            .input(structures)
            .generator(new RSSFullElementGenerator())
            .build())
        .then(new AddElements.Builder()
            .skipInvalidElements(true)
            .validate(false)
            .build())
        .build();

    graph.execute(addOpChain, user);

    final CloseableIterable<? extends Element> edges = graph.execute(new GetAllElements(), user);
    System.out.println("\nAll edges:");
    for (final Element edge : edges) {
      System.out.println(edge.toString());
    }

  }

  public static void main(String[] args) throws OperationException, IOException {
    RSSGaffer rssGaffer = new RSSGaffer();
    rssGaffer.run();
  }
}
