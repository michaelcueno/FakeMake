/*
 * @params
 * map - data structure that holds verticies, key values are verticies name
 * time - local 'clock' for simulating timestamp updates in touch and make methods
*/
import java.util.HashMap;
import java.util.Stack;

public class Graph{

	public HashMap<String,Vertex> map = new HashMap<String,Vertex>();	
	public int time = 1;	

	public boolean add( String fileName, Vertex v ){
	
		if(map.containsKey(fileName)){
			return false;
		}else{
			map.put( fileName, v );
			return true;

		}
	}

/*
 * Sets indegrees of all vertices. A vertex's indegree is determined by the
 * number of target files that rely on it.  
 * Runtime: Runs through all the vertecies and all edges at each vertex. O(V + E)
 */
	
	public void setIndegrees(){

		for( Vertex v : map.values() ){
			if(!v.isBasic()){
				for( String name : v.dependancies ){
					if( map.containsKey(name)){
						Vertex d = map.get(name);
						d.indegree++;
					}
				}
			}
		}	
	}

/*
 * Returns true if graph has a cycle
 * uses stack algorithm to determine if cycle exists:
 * 	1. Put any vertex with indegree == 0 on stack and increment count
 * 	2. While stack is not empty pop one vertex and
 * 		decrement the indegree of every dependancy of that vertex
 * 	3. if any dependancies' indegree fell to zero, push it to the stack
 * 
 * when the stack is empty, if no cycle, every vertex should have been placed 
 * on the stack at some point, thus count should be equal to map.size();
 *
 * Runtime: In the worst case, the algorithm runs through every vertex and then 
 * through every edge of that vertex. Thus the runtime is O(V + E)
 */ 	
	public boolean hasCycle(){
		
		int count = 0;
		Stack<Vertex> s = new Stack<Vertex>();

		for( Vertex v : map.values() ){
			if(v.indegree == 0)
				s.push(v);
		}

		while( !s.empty() ){
			
			Vertex v = (Vertex)s.pop();
			count++;
			for( String n : v.dependancies ){
				Vertex d = map.get(n);
				
				if(--d.indegree == 0)
					s.push(d);
			}	
		}

		if( count != map.size() ){
			return true;
		}else	
			return false;
	}

	public boolean contains(String fileName){
		if(map.containsKey(fileName)){
			return true;
		}else	
			return false;
	}

	public int getTime( String fileName ){
		if(map.containsKey(fileName)){
			return map.get(fileName).timeStamp;
		}else{
			System.out.println("File does not exist");
			return -1;
		}
	}
	
/*
 * Update the timestamp to the system time. Parameter must be a basic file
 */
	public void touch( String fileName ){

		Vertex v = map.get(fileName);
		
		if(map.containsKey(fileName)){
			if(v.isBasic() ){  
				v.setTime(time);
				System.out.println("File '" + fileName + "' has been modified");
				time++;
			}else
				System.out.println("cannot touch a target file");
		}else
			System.out.println("File does not exist");
	}

	public void make( String fileName ){
		
		if(map.get(fileName).isBasic()){

			System.out.println("Cannot make basic files");
		}else{

			update( fileName );
			resetVisited();
		}
	}

/*
 * Funtion: Recursive function that updates a vertex's timestamp iff any of 
 * 	it's dependacies have a timestamp greater than its own.
 * Alorithm: Recurse on every dependancy of the current vertex, 
 * 	if any dependancy has a greater timestamp than the current vertex,
 * 	update the current vertex. Once all dependancies are exhausted, 
 * 	mark the current vertex visited as to not revisit it in any other
 * 	target file dependacy lists.
 * Runtime: This method will always visit every vertex in the directed graph that
 * 	is connected to the target file directly and indirectly. It tries minimize the number
 * 	of edges it travels down by marking visited vertices, but it will in the worst case
 * 	therefore the runtime is O( V + E );
*/
	private void update( String fileName ){

		if(map.containsKey( fileName )){
			Vertex v = map.get( fileName );		

			//Base Case
			if( v.isBasic() ){
				v.visited = true;
				return;

			}else {
				if(!v.visited){
					boolean upToDate = true;
					for( String n : v.dependancies ){
						Vertex d = map.get(n);
						update(d.getName());
						if( d.timeStamp > v.timeStamp ){
							upToDate = false;
						}
					}
					v.visited = true;
					if(upToDate){
						System.out.println( v.getName() + " is up to date");
					}else{
						System.out.println("making " + v.getName() + "...done");
						v.setTime(time);
						time++;	
					}
				}
			}

		}else
			System.out.println("File does not exist");

	}

	public void resetVisited(){

		for( Vertex v : map.values() ){
			v.visited = false;
		}
	}
}
