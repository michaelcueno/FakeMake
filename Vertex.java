import java.util.ArrayList;

public class Vertex<T>{
	
	T value;
	public ArrayList<T> dependencies;
	public boolean visited;
	int timeStamp; 
	int indegree;

	public Vertex(T value){
		dependencies = new ArrayList<T>();
		this.value = value;
		this.timeStamp = 0;
		this.indegree = 0;
		this.visited = false;
	}

	public T getValue(){
		return this.value;
	}

	public void addAdj(T adj){

		dependencies.add(adj);
	}

	@Override
	public String toString(){
	
		return(value +" : "+ dependencies );

	}

	/*
	 * Returns true if the vertex has an indegree of zero
	 */
	public boolean isBasic(){
		return (dependencies.size() == 0);
	}

	public void setTime( int time ){
		timeStamp = time;
	}

	public static void main( String[] args ){
		Vertex<String> v = new Vertex<String>("test");
		System.out.println("Test cases, filename = " + v.getValue());
		
		if(v.isBasic())
			System.out.println("I'm basic ");
		
		v.addAdj("dep1");

		if(!v.isBasic())
			System.out.println("working");

		v.setTime(3);
		System.out.println("my timestamp reads: " + v.timeStamp);
	}
}