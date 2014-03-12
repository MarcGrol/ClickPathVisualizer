package nl.grol.trafficanalysis;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class Graph {
	private String name;
	private Map<String,Node> nodes = new HashMap<String, Node>();
	private double relevanceLimit = 0.005;
	
	public Graph( String name ) {
		this.name = name;
	}
	
	public Graph( String name, double relevanceLimit ) {
		this.name = name;
		this.relevanceLimit = relevanceLimit;
	}

	public String getName() {
		return name;
	}

	public Node getNodeOnName( String name ) {
		
		Preconditions.checkArgument(name != null);

		return this.nodes.get(name);
	}
	
	private Node addNode( String name ) {
		Node node = null;
		
		Preconditions.checkArgument(name != null);
		
		String cleanName = name.replaceAll(" ", "");
		
		node = this.getNodeOnName( cleanName) ;
		if( node == null ) {
			node = new Node(cleanName);
			this.nodes.put(cleanName, node);
		}
		
		return node;
	}
	
	public void addEdgeWithValue( String from, String to, long value ) {
		
		Preconditions.checkArgument(from != null);
		Preconditions.checkArgument(to != null);
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("value", Long.valueOf(value));
		addEdge( from, to, properties );
	}

	public void addEdge( String from, String to, Map<String, Object> properties ) {
		
		Preconditions.checkArgument(from != null);
		Preconditions.checkArgument(to != null);
		Preconditions.checkArgument(properties != null);
		
		Node fromNode = this.addNode(from);
		Node toNode = this.addNode(to);
		
		Edge relationShip = new Edge(toNode, properties);
		fromNode.addEdge(relationShip);
	}
	
	private long getMaxNodeValue() {
		long maxValue = 0;
		
		for( String nodeName : this.nodes.keySet() ) {
			Node node = this.nodes.get(nodeName);
			if( node != null && node.getValue() > maxValue ) {
				maxValue = node.getValue();
			}
		}
		
		return maxValue;
	}
	
	public String toGraphViz() {
		StringBuffer sb = new StringBuffer();
		
		/*
		 * Open directed graph
		 */
		sb.append( "digraph G {" + "\n" );
			sb.append( "rankdir=TD;"+ "\n" );
			sb.append( "\n" );
		
			/*
			 * Prepare subgraphs
			 */
			sb.append( "subgraph clusterregistration {" + "\n" );
			sb.append( "\t"+ "label = \"Registration\""  + "\n");
			sb.append( "}" + "\n" );
			sb.append( "\n" );

			sb.append( "subgraph clusterbalance {" + "\n" );
			sb.append( "\t"+ "label = \"Balance\""  + "\n");
			sb.append( "}" + "\n" );
			sb.append( "\n" );
	
			sb.append( "subgraph clusteraccountstransactions {" + "\n" );
			sb.append( "\t"+ "label = \"Accounts\""  + "\n");
	    	sb.append( "}" + "\n" );
			sb.append( "\n" );
	
	    	sb.append( "subgraph clusterpayment {" + "\n" );
			sb.append( "\t"+ "label = \"Payment\""  + "\n" );
			sb.append( "}" + "\n" );
			sb.append( "\n" );
	
			sb.append( "subgraph clustercreditcard {" + "\n" );
			sb.append( "\t"+ "label = \"Creditcard\""  + "\n" );
			sb.append( "}" + "\n" );
			sb.append( "\n" );
	
			/*
			 * Need total sum of visits in order to be able to determine importance of edge
			 */
			long maxFrequency = getMaxNodeValue();
			
			/*
			 * Dump all nodes
			 */
			for( String nodeName : this.nodes.keySet() ) {
				Node node = this.nodes.get(nodeName);
				if( node != null ) {
					sb.append( node.toGraphViz( maxFrequency, this.relevanceLimit ) );
				}
			}

			/*
			 * Dump all edges of all nodes
			 */
			for( String nodeName : this.nodes.keySet() ) {
				Node node = this.nodes.get(nodeName);
				if( node != null ) {
					sb.append( node.edgesToGraphViz( maxFrequency, this.relevanceLimit ) );
				}
			}

		/* 
		 * Close directed graph
		 */
		sb.append( "}" + "\n" );
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
	    return Objects.toStringHelper(this)
	    		.add("name", this.name)
	            .add("nodeCount", this.nodes.size())
	            .toString();
	}
	

}
