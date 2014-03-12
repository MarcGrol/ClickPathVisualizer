package nl.grol.trafficanalysis;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class Edge  {
	private Node targetNode = null;
	private Map<String, Object> properties = new HashMap<String, Object>();

	public Edge( Node targetNode, Map<String,Object> properties ) {
		
		Preconditions.checkArgument(targetNode != null);
		Preconditions.checkArgument(properties != null);

		this.targetNode = targetNode;
		this.properties = properties;
	}
	
	public Edge( Node targetNode ) {

		Preconditions.checkArgument(targetNode != null);

		this.targetNode = targetNode;
		this.setValue( 0L );
	}

	public Edge( Node targetNode, long value ) {

		Preconditions.checkArgument(targetNode != null);

		this.targetNode = targetNode;
		this.setValue( value );
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public Object getPropertyByName(String name) {

		Preconditions.checkArgument(name != null);

		return this.properties.get(name);
	}

	public boolean hasValue() {
		boolean status = false;
		
		Object value = this.getPropertyByName("value");
		if( value instanceof Long ) {
			status = true;
		}

		return status;
	}
	
	public long getValue() {
		long longValue = 0L;
		
		Object value = this.getPropertyByName("value");
		if( value instanceof Long ) {
			longValue = ((Long)value).longValue();
		}
		
		return longValue;
	}
	
	private void setValue( long value ) {
		setProperty("value", value);
	}
	
	public void incrementValue( long toBeAdded ) {
		Long longValue = this.getValue();
		if( longValue == null ) {
			setValue(toBeAdded);
		} else {
			setValue(longValue + toBeAdded );
		}
	}

	public void setProperty(String name, Object value) {
		
		Preconditions.checkArgument(name != null);
		Preconditions.checkArgument(value != null);

		this.properties.put(name, value);
	}

	private String edgeStyle( double importance ) {
		String style = "solid";
		
		Preconditions.checkArgument(importance >= 0 );

		if( importance < 0.01 ) {
			style = "dotted";
		}
		
		return style;
	}
	
	private int penWidth( double importance ) {
		int penWidth = 1;
		
		Preconditions.checkArgument(importance >= 0 );

		if( importance > 0.8 ) {
			penWidth = 25;
		} else if( importance >= 0.6 && importance < 0.8 ) {
			penWidth = 20;
		} else if( importance >= 0.4 && importance < 0.6 ) {
			penWidth = 15;
		} else if( importance >= 0.2 && importance < 0.4 ) {
			penWidth = 10;
		} else if( importance >= 0.05 && importance < 0.2 ) {
			penWidth = 5;
		} 

		return penWidth;
	}

	private String color( double importance ) {
		String color = "black";
		
		Preconditions.checkArgument(importance >= 0 );

		if( importance >=  0.2 ) {
			color = "red";
		} 

		return color;
	}
	
	public String toGraphViz( Node originatingNode, long maxFrequency,
			double relevanceLimit) {
		
		Preconditions.checkArgument(originatingNode != null);
		Preconditions.checkArgument(maxFrequency >= 0 );

		double relevance = (double)this.getValue() / maxFrequency;

		if( relevance > relevanceLimit ) {
			String style = edgeStyle(relevance); 
			int edgeWidth = penWidth(relevance);
			String color = color(relevance);
			
			String edgeString = 
				String.format("\"%s\" -> \"%s\" [ label=\"%s%%\", style=\"%s\", penwidth=%d, color=\"%s\" ];", 
					originatingNode.getName(),
					this.getTargetNode().getName(),
					Util.formatDouble(relevance*100),
					style,
					edgeWidth,
					color
					);
			
	//		String comment = String.format("value=%d, max=%d, weight=%f, width=%d",
	//				getValue(), maxFrequency, importance*100, edgeWidth );
		
			return // "/* " + comment + " */\n" +
					edgeString + "\n";
		} else {
			return "";
		}
	}

	@Override
	public String toString() {
	    return Objects.toStringHelper(this)
	            .add("target", this.targetNode.getName())
	            .add("value", (Long)this.properties.get("value"))
	            .toString();
	}

}
