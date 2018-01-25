import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
  public static void main(String[] args) throws Exception {
    ServerSocket m_ServerSocket = new ServerSocket(2004,10);
    int id = 0;
    while (true) {
      Socket clientSocket = m_ServerSocket.accept();
      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
      cliThread.start();
    }
  }
}

class ClientServiceThread extends Thread {
  Socket clientSocket;
  String message, option;
  int clientID = -1;
  boolean running = true;
  boolean check = false;
  boolean logged = false;
  boolean valid = false;
  ObjectOutputStream out;
  ObjectInputStream in;

  ClientServiceThread(Socket s, int i) {
    clientSocket = s;
    clientID = i;
  }

  void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
  public void run() {
    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
        + clientSocket.getInetAddress().getHostName());
    try 
    {
    	out = new ObjectOutputStream(clientSocket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
		
		
		do{
			try
			{
				File file = new File("users.txt");
				//Input for menu select
				sendMessage("1)Register\n2)Log In\n3)Exit");
				message = (String)in.readObject();
				
				if(message.compareToIgnoreCase("1")==0)
				{
					do{
						//Buffered reader
						//read users.txt
						//check username if exists, if it does then don't create user
						//else create user
						
						
						//client 1
						sendMessage("Please enter your Username: ");
						String username = (String)in.readObject();
	
						//client 2
						sendMessage("Please enter your password: ");
						String password = (String)in.readObject();
						
						//read in file
						BufferedReader br = null;
						br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
						
						//loop through txt file and check if username has been used
						String ln = "";
						while((ln = br.readLine()) != null){
							String[] info = ln.split("\\s");
							//if username is there then prompt the user again
							if (info[0].equals(username)){
								check = false;						// Set boolean
								sendMessage("Username has already been used!");		// Confirmation message (false)
								sendMessage("false");						// Send Confirmation message
								break;
							}
							//else set check to true
							else{
								check = true;
							}
						}
						//close buffered reader
						br.close();
						
						//if check is true, write new user to file
						if(check == true){							
							BufferedWriter bw = null;
							bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
							
							//append to users.txt file
							bw.append(username + " " + password);
							bw.newLine();
							
							bw.close();
							
							/*BufferedWriter userR = null;
							userR = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(username + ".txt", true)));
							
							sendMessage("Please enter your Address: ");
							String address = (String)in.readObject();
							sendMessage("Please enter your PPSN: ");
							String ppsn = (String)in.readObject();
							sendMessage("Please enter your Age: ");
							String age = (String)in.readObject();
							sendMessage("Please enter your Weight: ");
							String weight = (String)in.readObject();
							sendMessage("Please enter your Height: ");
							String height = (String)in.readObject();
							userR.append(username + " " + address + " " + ppsn + " " + age + " " + weight + " " + height);
							
							userR.close();*/
							//client 3+4
							sendMessage("User profile has been registered");
							sendMessage("true");
						}
						
					}while(check == false);
				}
				
				else if(message.compareToIgnoreCase("2")==0)
				{
					//Buffered reader
					//read users.txt
					//check username and password if exists, if do, YES!
					//else no
										
					//client 1
					sendMessage("Please enter your Username: ");
					String username = (String)in.readObject();
					
					//client 2
					sendMessage("Please enter your password: ");
					String password = (String)in.readObject();
					
					BufferedReader br = null;
					
					try{
						//set up buffered reader to read users.txt
						br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
						
						//Loop through each line to check is the user can log in
						String ln = "";
						while((ln = br.readLine()) != null){
							String[] info = ln.split("\\s");
							//if username and password match then user signs in
							if(info[0].equals(username) && info[1].equals(password)){
								System.out.println("Ding");
								logged = true;
							}
							if (logged == true){
								//client 3
								sendMessage("true");
								break;
							}
						}
						br.close();
						
						if (logged == false){
							//client 3
							sendMessage("false");
						}
						
						if (logged == true) {
							// logged in menu
							do {
								option = "";
								sendMessage("\n1) Add a fitness record\n2) Add a meal record\n3) View last 10 records of user\n4) View last 10 fitness records of user\n5) Delete a user fitness record\n6) Logout");
								//send user option
								option = (String)in.readObject();
								
								// 1) add fitness record
								if (option.equals("1")){
									sendMessage("Add fitness record selected");
									sendMessage("Enter activity (Running/Walking/Cycling)");					 
									String mode = (String)in.readObject();	
										
									// Assign Duration (Needs error handling)
									sendMessage("Enter duration of exercise in minutes");
									String duration = (String)in.readObject();
									BufferedWriter bw = null;
									bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(username+".txt", true)));
									
									bw.append("Fit - Mode :" + mode + "Duration :" + duration);
									bw.newLine();
									
									bw.close();
								}
								// 2) add meal record
								else if (option.equals("2")){
									sendMessage("Add meal record selected");
									sendMessage("Enter meal (breakfast/lunch/dinner): ");					 
									String meal = (String)in.readObject();	
										
									// Assign Duration (Needs error handling)
									sendMessage("Enter a description of the meal: ");
									String desc = (String)in.readObject();
									BufferedWriter bw = null;
									bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(username+".txt", true)));
									
									//Append info to the username txt file
									bw.append("Meal - Meal :" + meal + " Description :" + desc);
									bw.newLine();
									
									bw.close();
								}
								// 3) view 10 records
								else if (option.equals("3")){
									sendMessage("View the last 10 records selected");
									String[] lines = new String[10];
									int count = 0;
									String line = null;
									BufferedReader viewRec = null;
									viewRec = new BufferedReader(new InputStreamReader(new FileInputStream(username + ".txt")));
									//Loops through user file and take in last 10 entered records
									while ((line = viewRec.readLine()) != null) {
								    	lines[count % lines.length] = line;
									    count++;
									}
									viewRec.close();
									int start = count - 10;
									if (start < 0) {
									    start = 0;
									}
									//displays last 10 lines by looping through array
									for (int i = start; i < count; i++) {
									    sendMessage(lines[i % lines.length]);
									}
								}
								// 4) view 10 fitness records
								else if (option.equals("4")){
									sendMessage("View the last 10 fitness records selected");
									String[] lines = new String[10];
									int count = 0;
									String line = null;
									BufferedReader viewRec = null;
									viewRec = new BufferedReader(new InputStreamReader(new FileInputStream(username + ".txt")));
									//Loops through user file and take in last 10 entered fitness records
									while ((line = viewRec.readLine()) != null) {
										//Searches for the codeword "Fit" on the line to 
									    if(line.contains("Fit")){
									    	lines[count % lines.length] = line;
										    count++;
									    }
									}
									viewRec.close();
									int start = count - 10;
									if (start < 0) {
									    start = 0;
									}
									//displays last 10 lines by looping through array
									for (int i = start; i < count; i++) {
									    sendMessage(lines[i % lines.length]);
									}
								}
								// 5) delete a fitness record
								else if (option.equals("5")){
									//Incomplete
									sendMessage("Delete a fitness record selected");
								}
								// any other input
								else {
									sendMessage("Invalid choice, try again");
								}
								
							} while(!option.equals("6"));
							
						}
						
					}
					catch(FileNotFoundException e){
						e.printStackTrace();
						if(!file.isFile()){
						  file.createNewFile();
						}
					}
				}	
			}
			catch(ClassNotFoundException classnot){
				System.err.println("Data received in unknown format");
			}
			
    	}while(!message.equals("3"));
      
		System.out.println("Ending Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
