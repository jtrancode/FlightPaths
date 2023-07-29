import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {
	// lists of nodes and way to obtain list
    private static List<Node> list = new LinkedList<>();
    public static List<Node> listView()
    { 
        return Collections.unmodifiableList(Node.list);
    }
    
    // create map to edges for nodes
    private static Map<String, Node> nodes = new HashMap<>();
    public static Node of(String name) 
    {
        Node result = nodes.get(name);
        
        // if not null put into the map, and add to list of nodes
        if(result == null) 
        {
            result = new Node(name);
            
            nodes.put(name, result);
            Node.list.add(result);
        }
        
        return result;
    }
    
    // variable for name and keep list of edges for each node
	private String name;
	private LinkedList<Edge> edges;
	
	public Node()
	{
		name = "";
		edges = new LinkedList<>();
	}
	
	public Node(String name)
	{
		this.name = name;
		edges = new LinkedList<>();
	}
	
	public String getName()
	{
		return name;
	}
	
    public Iterator<Edge> edgeIter()
    {
        return edges.iterator();
    }
	
    // add edge
    public void addEdge(String name, int time, int cost)
    {
        Node dest = Node.of(name);
        addEdge(dest, time, cost);
        dest.addEdge(this, time, cost);
    }
    
    // add edge (with node)
    private void addEdge(Node dest, int time, int cost) 
    {
        edges.add(new Edge(dest, time, cost));
    }
    
    // for debugging
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(name);
        
        for(Edge edge : edges) {
            buffer.append("\n > ").append(edge.dest.name).append(", Time: ").append(edge.time).append(", Cost: ").append(edge.cost);
        }
        
        return buffer.toString();
    }
	
	static class Edge {
		public Node dest;
		public int cost, time;
		
		public Edge(Node dest, int cost, int time)
		{
			this.dest = dest;
			this.cost = cost;
			this.time = time;
		}
	}
}