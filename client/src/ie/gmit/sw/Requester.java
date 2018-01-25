package ie.gmit.sw;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message="", option="";
 	String ipaddress;
 	boolean check = false;
 	boolean logged = false;
 	Scanner stdin;
	Requester(){}
	void run()
	{
		stdin = new Scanner(System.in);
		try{
			//1. creating a socket to connect to the server
			System.out.println("Please Enter your IP Address");
			ipaddress = stdin.next();
			requestSocket = new Socket(ipaddress, 2004);
			System.out.println("Connected to "+ipaddress+" in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			System.out.println("Hello");
			//3: Communicating with the server
			do {		
				try	{
					//Takes in user input and sends response
					message = (String)in.readObject();			
					System.out.println(message);				
					message = stdin.next();						
					sendMessage(message);						
					
					// Register a user
					if(message.compareToIgnoreCase("1")==0){
						do {
							//Reads in username
							message = (String)in.readObject();
							System.out.println(message);
							message = stdin.next();
							sendMessage(message);
							
							//Reads in password
							message = (String)in.readObject();
							System.out.println(message);
							message = stdin.next();
							sendMessage(message);
							
							// Check if username is available
							message = (String)in.readObject();
							System.out.println(message);
							message = (String)in.readObject();
							if (message.equals("true"))
								logged = true;
							else 
								logged = false;
							
							/*//reads in address
							message = (String)in.readObject();
							System.out.println(message);
							message = stdin.next();
							sendMessage(message);
							//reads in ppsn
							message = (String)in.readObject();
							System.out.println(message);
							message = stdin.next();
							sendMessage(message);
							//reads in age
							message = (String)in.readObject();
							System.out.println(message);
							message = stdin.next();
							sendMessage(message);
							//reads in weight
							message = (String)in.readObject();
							System.out.println(message);
							message = stdin.next();
							sendMessage(message);
							//reads in height
							message = (String)in.readObject();
							System.out.println(message);
							message = stdin.next();
							sendMessage(message);*/
						} while (logged == false);
					}
					// Log in
					else if(message.compareToIgnoreCase("2")==0){
						//Reads in username
						message = (String)in.readObject();
						System.out.println(message);
						message = stdin.next();
						sendMessage(message);
						
						//Reads in password
						message = (String)in.readObject();
						System.out.println(message);
						message = stdin.next();
						sendMessage(message);
					
						// check if logged in
						message = (String)in.readObject();			
						System.out.println("Confirm this: "+message);
						if (message.equals("true"))	
						{
							System.out.println("Logging in");
							
							do {
								//menu display
								message = (String)in.readObject();		
								System.out.println(message);
								
								//take in menu option
								option = stdin.next();
								sendMessage(option);
								
								// 1) add fitness record
								if (option.equals("1")){

									message = (String)in.readObject();		
									System.out.println(message);
									message = (String)in.readObject();	
									System.out.println(message);
									
									// user input mode
									message = stdin.next();
									sendMessage(message);
																		
									// user input duration
									message = (String)in.readObject();	
									System.out.println(message);
									
									message = stdin.next();
									sendMessage(message); 
									
								}
								// 2) add meal record
								else if (option.equals("2")){
									
									message = (String)in.readObject();		
									System.out.println(message);
									message = (String)in.readObject();	
									System.out.println(message);
									
									// user input meal
									message = stdin.next();
									sendMessage(message);
																		
									// user input meal description
									message = (String)in.readObject();	
									System.out.println(message);
									
									message = stdin.next();
									sendMessage(message); 
								}
								// 3) view 10 records
								else if (option.equals("3")){
									
									message = (String)in.readObject();		
									System.out.println(message);
									
									//Loop through to display exactly 10 items
									for(int i = 0; i < 10; i++){
										message = (String)in.readObject();		
										System.out.println(message);
									}
								}
								// 4) view 10 fitness records
								else if (option.equals("4")){
									
									message = (String)in.readObject();		
									System.out.println(message);
									
									//Loop through to display exactly 10 items
									for(int i = 0; i < 10; i++){
										message = (String)in.readObject();		
										System.out.println(message);
									}
								}
								// 5) delete a fitness record
								else if (option.equals("5")){
									//Incomplete
									message = (String)in.readObject();		
									System.out.println(message);
								}
								else if (option.equals("6")){
									break;
								}
								// any other input
								else {
									
									message = (String)in.readObject();		
									System.out.println(message);
								}
								
							} while (!option.equals("6"));							
							
						}
						else if (message.equals("false")) {
							System.out.println("Username or password incorrect");
						}
					}
				}
				catch(ClassNotFoundException classNot){
					System.err.println("data received in unknown format");
				}
			}while(!message.equals("3"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
}