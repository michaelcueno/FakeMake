/*
 * @params
 * map - data structure that holds verticies, key values are verticies name
 * time - local 'clock' for simulating timestamp updates in touch and make methods
*/
import java.util.HashMap;
import java.util.Stack;

public class Graph <T> {

	public HashMap<T,Vertex<T>> map = new HashMap<T,Vertex<T>>();	
	public int time = 1;	

	public boolean add( T value, Vertex<T> v ){
	
		if(map.containsKey(value)){
			return false;
		}else{
			map.put( value, v );
			return true;

		}
	}

/*
 * Sets indegrees of all vertices. A vertex's indegree is determined by the
 * number of target files that rely on it.  
 * Runtime: Runs through all the vertecies and all edges at each vertex. O(V + E)
 */
	
	public void setIndegrees(){

		for( Vertex<T> v : map.values() ){
			if(!v.isBasic()){
				for( T name : v.dependancies ){
					if( map.containsKey(name)){
						
						Vertex<T> d = map.get(name);
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
		Stack<Vertex<T>> s = new Stack<Vertex<T>>();

		for( Vertex<T> v : map.values() ){
			if(v.indegree == 0)
				s.push(v);
		}

		while( !s.empty() ){
			
			Vertex<T> v = (Vertex<T>)s.pop();
			count++;
			for( T n : v.dependancies ){
				Vertex<T> d = map.get(n);
				
				if(--d.indegree == 0)
					s.push(d);
			}	
		}
		
		if( count != map.size() ){
			return true;
		}else	
			return false;
	}

	/*
	 * Returns true if the graph contains key name
	*/
	public boolean contains(T value){
		if(map.containsKey(value)){
			return true;
		}else	
			return false;
	}

	public int getTime( T value ){
		if(map.containsKey(value)){
			return map.get(value).timeStamp;
		}else{
			System.out.println("File does not exist");
			return -1;
		}
	}
	
	public void touch( T value ){

		Vertex<T> v = map.get(value);
		
		if(map.containsKey(value)){
			if(v.isBasic() ){  
				v.setTime(time);
				System.out.println("File '" + value + "' has been modified");
				time++;
			}else
				System.out.println("cannot touch a target file");
		}else
			System.out.println("File does not exist");
	}

	public void make( T value ){
		
		if(map.get(value).isBasic()){

			System.out.println("Cannot make basic files");
		}else{

			update( value );
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
	private void update( T value ){

		if(map.containsKey( value )){
			Vertex<T> v = map.get( value );		

			//Base Case
			if( v.isBasic() ){
				v.visited = true;
				return;

			}else {
				if(!v.visited){
					boolean upToDate = true;
					for( T n : v.dependancies ){
						Vertex<T> d = map.get(n);
						update(d.getValue());
						if( d.timeStamp > v.timeStamp ){
							upToDate = false;
						}
					}
					v.visited = true;
					if(upToDate){
						System.out.println( v.getValue() + " is up to date");
					}else{
						System.out.println("making " + v.getValue() + "...done");
						v.setTime(time);
						time++;	
					}
				}
			}

		}else
			System.out.println("File does not exist");

	}

	public void resetVisited(){

		for( Vertex<T> v : map.values() ){
			v.visited = false;
		}
	}
}
