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

	/*
	 * Returns true if the graph contains key name
	*/
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
	 * Recursive function that updates a vertex's timestamp iff any of 
	 * its dependacies have a timestamp greater than its own.
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
