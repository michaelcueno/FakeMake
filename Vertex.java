import java.util.ArrayList;

public class Vertex<T>{
	
	T value;
	public ArrayList<T> dependancies;
	public boolean visited;
	int timeStamp; 
	int indegree;

	public Vertex(T value){
		dependancies = new ArrayList<T>();
		this.value = value;
		this.timeStamp = 0;
		this.indegree = 0;
		this.visited = false;
	}

	public T getValue(){
		return this.value;
	}

	public void addAdj(T adj){

		dependancies.add(adj);
	}

	@Override
	public String toString(){
	
		return(value +" : "+ dependancies );

	}

	/*
	 * Returns true if the vertex has an indegree of zero
	 */
	public boolean isBasic(){
		return (dependancies.size() == 0);
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
