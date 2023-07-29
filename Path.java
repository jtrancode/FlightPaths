import java.util.Collection;
import java.util.Comparator;

public class Path {
	// comparators to sort by time or cost
    public static final Comparator<Path> compareByTime = (a, b) -> a.totalTime - b.totalTime;
    public static final Comparator<Path> compareByCost = (a, b) -> a.totalCost - b.totalCost;
    
    // variables for time, cost, and print
    private int totalTime, totalCost;
    private String print;
    
    public Path(Node source, Collection<Node.Edge> path) 
    {
    	// set up print and start with source name
        StringBuffer out = new StringBuffer(source.getName());
        // go through paths and add up times and costs
        for(Node.Edge edge : path) 
        {
            totalTime += edge.time;
            totalCost += edge.cost;
            
            // adding on to output for path
            out.append(" -> ").append(edge.dest.getName());
        }
        
        // adding on for output
        out.append(". Time: ").append(totalTime).append(", Cost: ").append(totalCost);
        // put into print
        print = out.toString();
    }
    
    public String toString() 
    {
        return print;
    }
}
