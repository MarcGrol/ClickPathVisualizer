package nl.grol.trafficanalysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class Node  {
	protected String name;
	private long value = 0l;
	private List<Edge> edges = new ArrayList<Edge>();
	
	public Node( String name ) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void incrementValueWith( long value ) {
		this.value += value;
	}
	
	public long getValue() {
		return this.value;
	}

	public List<Edge> getEdges() {
		return this.edges;
	}

	public Edge getEdgeOnTargetNode( String targetNodeName ) {
		Edge edge = null;
		
		Preconditions.checkArgument(targetNodeName != null);

		for( Edge e : this.edges ) {
			if( e.getTargetNode().getName().equals(targetNodeName) ) {
				edge = e;
				break;
			}
		}
		
		return edge;
	}
	
	public void addEdge( Edge newEdge ) {

		Preconditions.checkArgument(newEdge != null);
		Preconditions.checkArgument(newEdge.getTargetNode() != null);
	
		Edge existingRelationShip = this.getEdgeOnTargetNode(newEdge.getTargetNode().getName() );
		if( existingRelationShip != null ) {
			existingRelationShip.incrementValue(newEdge.getValue());
		} else {
			this.edges.add( newEdge );
		}
		
		/*
		 * Increment value of target node
		 */
		if( newEdge.hasValue() == true ) {
			Node targetNode = newEdge.getTargetNode();
			targetNode.incrementValueWith( newEdge.getValue() );
		}
	}
	

	public String toGraphViz( long maxFrequency, double relevanceLimit ) {
		StringBuffer sb = new StringBuffer();
		
		double relevance = (double)this.getValue() / maxFrequency;

		if( relevance > relevanceLimit ) {
	
			String nodeString = String.format("\"%s\" [label=\"%s\\n%d (%s%%)\"];", 
						getName(), 
						getName(), 
						getValue(), 
						Util.formatDouble(relevance*100));
			sb.append(nodeString + "\n" ); 
		}
		
		return sb.toString();
	}

	public String edgesToGraphViz( long maxFrequency, double relevanceFactor ) {
		StringBuffer sb = new StringBuffer();
		
		for( Edge edge: edges ) {
			sb.append( edge.toGraphViz(this, maxFrequency, relevanceFactor) );
		}
		
		return sb.toString();
	}

	@Override
	public String toString() {
	    return Objects.toStringHelper(this)
	    		.add("name", this.name)
	            .add("edgeCount", this.edges.size())
	            .toString();
	}

}
