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
	 * Recursive function that updates a vertex's timestamp iff any of 
	 * its dependacies have a timestamp greater than its own.
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
