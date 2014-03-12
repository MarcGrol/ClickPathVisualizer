package nl.grol.trafficanalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GraphDataLoader {
	
	public static void main( String[] args ) {
		GraphDataLoader loader = new GraphDataLoader();
		Graph graph = loader.load( "src/traffic_20140102_day.txt"); //src/traffic.txt"); //src/rob_traffic_20140102.txt");  / 
		System.out.print( graph.toGraphViz() );
	}

	public  Graph load( String filename ) {
		Graph graph = new Graph("Mobile banking traffic analysis", 0.00001 );
		
		BufferedReader reader = null;
	 
		try {	
			int lineNumber = 1;
			String line = null;
			reader = new BufferedReader(new FileReader(filename));
			while (( line = reader.readLine()) != null) {
	 
				String[] fields = line.split(";");
	 
				if( fields.length < 4 ) {
					System.err.println("ignoring line=" + lineNumber + 
							": too short" );
				} else {
					try {
						/*
						 * Ignore second and third field
						 */
						Long frequency = Long.valueOf(fields[0]  );
						String rawNodeList = fields[3];
						
						/*
						 * Artificially add a start and end node
						 */
						String nodeList = String.format("start > %s end", rawNodeList );
						
						if( frequency == null  || nodeList == null ) {
							System.err.println("ignoring line=" + lineNumber + 
									": frequency or node list missing" );
						} else {
							String[] nodes  = nodeList.split(">");
							for( int idx = 0; idx<nodes.length; idx++ ) {
								if( idx < nodes.length -1) {
									String current = nodes[idx];
									String next = nodes[idx+1];
									
									/*
									 * Popupate each transition as edge in graph
									 */
									graph.addEdgeWithValue(current, next, frequency );
									
								} else {
									//System.err.println("ignoring line=" + lineNumber + ": ignore last field " +   nodes[idx]);
								}
							}
						}
					} catch( NumberFormatException numberFormatException ) {
						System.err.println("ignoring line=" + lineNumber + 
								": Error parsing frequency " + numberFormatException );
					}
				}
				
				lineNumber++;
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		return graph;
	}

	
	public static Graph testGraph( String filename ) {
		Graph graph = new Graph("test graph");
		
		graph.addEdgeWithValue("parent","child", 3L);
		graph.addEdgeWithValue("parent", "child", 2L);
		graph.addEdgeWithValue("parent","child", 5L);
		graph.addEdgeWithValue("child","grantchild1", 10L);
		graph.addEdgeWithValue("child","grantchild1", 15L);
		graph.addEdgeWithValue("child","grantchild2", 100L);
		
		return graph;
	}
}
