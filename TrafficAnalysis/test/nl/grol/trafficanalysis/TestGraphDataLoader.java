package nl.grol.trafficanalysis;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGraphDataLoader {
	private static GraphDataLoader loader = null;
	@Before
	public void setUp() throws Exception {
		loader = new GraphDataLoader();
	}

	@After
	public void tearDown() throws Exception {
		loader = null;
	}

	@Test
	public void testLoad() {
		Graph graph = loader.load("test/traffic_test.txt");
		assertNotNull(graph);
		{
			Node node =  graph.getNodeOnName("start");
			assertNotNull(node);
			
			// test individual edges starting from this node
			assertNotNull(node.getEdges());
			assertEquals(2, node.getEdges().size());
			{
				Edge edge = node.getEdgeOnTargetNode("balanceview_v1");
				assertNotNull(edge);
				assertEquals( 18L, edge.getValue() );
			}
			{
				Edge edge = node.getEdgeOnTargetNode("productoverview_v4");
				assertNotNull(edge);
				assertEquals( 5L, edge.getValue() );
			}
		}
		{
			Node node =  graph.getNodeOnName("balanceview_v1");
			assertNotNull(node);
			
			// sum of edge-values ending in this node 
			assertEquals(18L, node.getValue() );
			
			// test individual edges starting from this node
			assertNotNull(node.getEdges());
			assertEquals(2, node.getEdges().size());
			{
				Edge edge = node.getEdgeOnTargetNode("productoverview_v4");
				assertNotNull(edge);
				assertEquals( 8L, edge.getValue() );
			}
			{
				Edge edge = node.getEdgeOnTargetNode("end");
				assertNotNull(edge);
				assertEquals( 10L, edge.getValue() );
			}
		}
		{
			Node node =  graph.getNodeOnName("productoverview_v4");
			assertNotNull(node);
			
			// sum of edge-values ending in this node 
			assertEquals(13L, node.getValue() );

			// test individual edges starting from this node
			assertNotNull(node.getEdges());
			assertEquals(1, node.getEdges().size());
			{
				Edge edge = node.getEdgeOnTargetNode("end");
				assertNotNull(edge);
				assertEquals( 13L, edge.getValue() );
			}
		}
		{
			Node node =  graph.getNodeOnName("end");
			assertNotNull(node);

			// sum of edge-values ending in this node 
			assertEquals(23L, node.getValue() );

		}
		
	}

}
