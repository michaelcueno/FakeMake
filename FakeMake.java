/*********************************************************
 * FakeMake: A toy program based on the linux 'make'	  *
 * By: Michael Cueno					  *
 * Class: CS 202 (Data Structures II)			  *
 * 							  *
 * Function:  Driver class for running application	  *
 * Dependancies:					  *
 * 	- Graph.java 					  *
 * 	- Vertex.java					  *
 *							  *
 **********************************************************/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class FakeMake{
	

	public static void main(String[] args) throws FileNotFoundException{
		
		Scanner fileScanner = new Scanner(new File(args[0]));
		Scanner keyboard = new Scanner(System.in);
		String file = null;
		Graph<String> graph = new Graph<String>();
		
		// Initialize Graph with adjacency list implementation  

		while(fileScanner.hasNextLine()){

			String line = fileScanner.nextLine();	
			Scanner lineScanner = new Scanner(line);
			
			if(lineScanner.hasNext()){

				file = lineScanner.next();
				Vertex<String> v = new Vertex<String>(file);
				if(graph.contains(file)){
					System.out.println("Make file contains repeated file Names. Program will exit");
					System.exit(0);
				}

				if(lineScanner.hasNext()){
					if(lineScanner.next().equals(":")){
					
						while(lineScanner.hasNext()){
							String vert = lineScanner.next();
							v.addAdj( vert );
						}
					}
				}
				graph.add(file, v);
			}
		}	

		graph.setIndegrees();
			
		// Take user input for commands and run respective methods


		if(graph.hasCycle()){
			System.out.println("This make file has a cycle. Program will exit");
		}else{
			System.out.println("Welcome to FakeMake. For command list, enter 'help'");
		
			String menu = keyboard.next();
		
			while(!menu.equals("quit")){
				if(menu.equals("time")){
					System.out.println(graph.time);	
				}else if(menu.equals("touch")){
					if(keyboard.hasNext()){
						file = keyboard.next();
						graph.touch( file );
					}else
						System.out.println("incorrect input, type help for commands...");
				}else if(menu.equals("timestamp")){
					   if(keyboard.hasNext()){
						file = keyboard.next();
						System.out.println(graph.getTime(file));
					   }else
						System.out.println("Incorrect input");
				}else if(menu.equals("make")){
					if(keyboard.hasNext()){
						file = keyboard.next();
						graph.make(file);
					}else
						System.out.println("incorrect input, type help for commands...");
				}else if(menu.equals("help")){
					System.out.println();
					System.out.println("time               Display current time");
					System.out.println("touch <file>       Modify a basic file");
					System.out.println("timestamp <file>   Display a file's timestamp");
					System.out.println("make <file>        Compile target file");
					System.out.println("quit               exit program");
					System.out.println();
				}else{
					System.out.println("incorrect input, type help for commands...");
				}			

				menu = keyboard.next();
			}

		}
	}
}