package nl.grol.trafficanalysis;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGraph {
	private static Graph graph = null;

	@Before
	public void setUp() throws Exception {
		graph = new Graph("test graph");
	}

	@After
	public void tearDown() throws Exception {
		graph = null;
	}

	@Test
	public void testName() {		
		assertNotNull(graph);
		assertEquals("test graph", graph.getName() );
	}
	
	@Test
	public void testAddTwoIdenticalEdges() {	
		
		assertNotNull(graph);
		assertNull( graph.getNodeOnName("parent") );
		assertNull( graph.getNodeOnName("child") );
		
		/*
		 * Add two edges between two same nodes
		 */
		graph.addEdgeWithValue("parent","child", 3L);
		graph.addEdgeWithValue("parent","child", 4L);
		{
			Node child = graph.getNodeOnName("child");
			assertNotNull(child);
			assertEquals("child", child.getName() );
		}
		{
			Node parent = graph.getNodeOnName("parent");
			assertNotNull(parent.getEdges());
			// expect single edge with value of two edges summarized
			assertEquals(1, parent.getEdges().size());
			assertNotNull(parent.getEdges().get(0));
			{
				Edge edge = parent.getEdgeOnTargetNode("child");
				assertNotNull(edge);
				assertEquals("child", edge.getTargetNode().getName() );
				assertEquals(3L + 4L, edge.getValue() );
			}
		}
	}
	
	@Test
	public void testAddTwoDifferentEdges() {		

		assertNotNull(graph);
		assertNull( graph.getNodeOnName("parent") );
		assertNull( graph.getNodeOnName("child") );
		
		/*
		 * Add another edge with different destination
		 */
		graph.addEdgeWithValue("parent","child", 3L);
		graph.addEdgeWithValue("parent","anotherchild", 9L);
		
		{
			Node child = graph.getNodeOnName("child");
			assertNotNull(child);
			assertEquals("child", child.getName() );
		}
		{
			Node child = graph.getNodeOnName("anotherchild");
			assertNotNull(child);
			assertEquals("anotherchild", child.getName() );
		}

		{
			Node parent = graph.getNodeOnName("parent");
			assertNotNull(parent.getEdges());
			assertEquals(2, parent.getEdges().size());
			{
				Edge edge = parent.getEdgeOnTargetNode("child");
				assertNotNull(edge);
				assertEquals("child", edge.getTargetNode().getName() );
				assertEquals(3L, edge.getValue() );
			}
			{
				Edge edge = parent.getEdgeOnTargetNode("anotherchild");
				assertNotNull(edge);
				assertEquals("anotherchild", edge.getTargetNode().getName() );
				assertEquals(9L, edge.getValue() );
			}
		}

	}

}
