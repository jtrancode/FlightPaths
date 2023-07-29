import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class FlightPaths {
	public static void main(String[] args) throws IOException {
		
		// check if info is given
		if(args.length < 1)
		{
			System.err.println("No files specified.");
			return;
		}
		else if(args.length < 2)
		{
			System.err.println("No file to calculate flights specified.");
			return;
		}
		
		// check if files exist
		File dataFile = new File(args[0]);
		if(!dataFile.exists())
		{
			System.err.println("Flight data file does not exist.");
			return;
		}
		
		File requestFile = new File(args[1]);
		if(!requestFile.exists())
		{
			System.err.println("Paths to calcuate file does not exist");
		}
		
		// scanners to read files
		Scanner data = new Scanner(new BufferedInputStream(new FileInputStream(dataFile)));
		Scanner requests = new Scanner(new BufferedInputStream(new FileInputStream(requestFile)));
		
		PrintStream out;
		
		// create the output file or if no output file specified, get ready to print data
		if(args.length >= 3)
		{
			File outputFile = new File(args[2]);
			if(outputFile.exists())
			{
				outputFile.delete();
			}
			
			outputFile.createNewFile();
			
			out = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
		}
		else
		{
			System.out.println("Output file not specified. Printing.\n");
			out = System.out;
		}
		
		// read data
		int lines = data.nextInt();
		data.nextLine();
		for(int i = 0; i < lines; i++)
		{
			String[] flightData = data.nextLine().trim().split("\\|");
			
			int cost = Integer.parseInt(flightData[2]);
            int time = Integer.parseInt(flightData[3]);
            
            // put node into list of nodes and add to map and add edge
            Node.of(flightData[0]).addEdge(flightData[1], time, cost);
		}
		
		data.close();
		
		// process requests for flight paths
		lines = requests.nextInt();
		requests.nextLine();
		for(int i = 0; i < lines; i++)
		{
			String[] requ = requests.nextLine().trim().split("\\|");
			
			Node source = Node.of(requ[0]);
            Node dest = Node.of(requ[1]);
            
            // create list for paths
            List<Path> paths = new ArrayList<Path>();
            
            // stack to keep track of current path by edges
            Stack<Node.Edge> currentPath = new Stack<>();
            
            // make sure the source and destination aren't the same
            if(source != dest) 
            {
            	// stack to hold the states
                Stack<Iterator<Node.Edge>> states = new Stack<>();
                // iterator to iterate through edges
                Iterator<Node.Edge> state = source.edgeIter();
                
                // while there are edges to go through
                while(state.hasNext() || !states.empty())
                {
                	// while there are no edges left for reaching a particular city
                	// pop to backtrack
                    while(!state.hasNext() && !states.empty()) 
                    {
                        state = states.pop();
                        currentPath.pop();
                    }
                    
                    // sometimes above while loop will end when the stack of states is empty
                    // but there is no next edge. break out of loop if there are no edges left
                    if(!state.hasNext())
                        break;
                    
                    // set nextEdge to what edge was in state and move to the next edge
                    Node.Edge nextEdge = state.next();
                    
                    // make sure the destination of the edge doesn't go back to where we started
                    if(nextEdge.dest == source)
                        continue;
                    
                    // more edge checks to make sure we don't loop
                    boolean destExists = false;
                    for(Node.Edge e : currentPath)
                    {
                        if(e.dest == nextEdge.dest)
                        {
                            destExists = true;
                            continue;
                        }
                    }
                    
                    // if we aren't looping
                    if(!destExists) 
                    {
                    	// add edge to stack
                        currentPath.push(nextEdge);
                        
                        // if edge destination is the destination for a path
                        // add the path to the list of paths
                        if(nextEdge.dest == dest) 
                        {
                            paths.add(new Path(source, currentPath));
                            // pop to backtrack back to previous city
                            currentPath.pop();
                        }
                        else // otherwise put the current state of iterator on to the stack of iterators to go through later
                        {
                            states.push(state);
                            // set the iterator of edge to the next city's iterator for its list of edges
                            state = nextEdge.dest.edgeIter();
                        }
                    }
                }
            }
            
            // switch case to sort by time or cost or throw error if the prompt for sort is not valid
            switch(requ[2]) 
            {
            	case "T":
            		paths.sort(Path.compareByTime);
            		break;
            	case "C":
            		paths.sort(Path.compareByCost);
            		break;
            	default:
            		throw new IllegalArgumentException("Invalid query. Cannot sort by " + requ[2]);
            }
        
            //System.out.printf("Figure %d: %s, %s (%s)%n", i, source.getName(), dest.getName(), "T".equals(requ[2]) ? "Time" : "Cost");
            
            // printing out formatting
            System.out.print("Figure " + (i+1) + ": " + source.getName() + ", " + dest.getName() + " ");
            if("T".equals(requ[2]))
            {
            	System.out.println("(Time)");
            }
            else
            {
            	System.out.println("(Cost)");
            }
            
            // if flight not possible, print message that says it's not possible
            if(paths.size() == 0)
            {
            	System.out.println("Flight not possible");
            }
            else if(paths.size() < 3) // if the number of paths is less than 3 print all paths
            {
                for(int t = 0; t < paths.size(); t++) 
                {
                	System.out.println("Path "+ (t+1) + ": " + paths.get(t).toString());
                }
            }
            else // otherwise print the 3 shortest paths
            {
            	for(int t = 0; t < 3; t++) 
                {
                	System.out.println("Path "+ (t+1) + ": " + paths.get(t).toString());
                }
            }
            
            System.out.println();
		}
		
		requests.close();
		
		if(out != System.out)
		{
            out.close();
		}
	}
}
