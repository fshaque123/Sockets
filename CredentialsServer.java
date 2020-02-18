import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class CredentialsServer {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static String output = "";
    private static String eor = "[EOR]"; // a code for end-of-response
    
    // establishing a connection
    private static void setup() throws IOException {
        
        serverSocket = new ServerSocket(0);
        toConsole("Server port is " + serverSocket.getLocalPort());
        
        clientSocket = serverSocket.accept();

        // get the input stream and attach to a buffered reader
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        // get the output stream and attach to a printwriter
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        toConsole("Accepted connection from "
                 + clientSocket.getInetAddress() + " at port "
                 + clientSocket.getPort());
            
        sendGreeting();
    }
    
    // the initial message sent from server to client
    private static void sendGreeting() throws IOException
    {
        appendOutput("Welcome to Dognet!\n");
       
        appendOutput("Please enter your username: ");
    	sendOutput();
    	
        
    }
    private static void check() throws IOException //checks credentials
    {
    	String inputLine = in.readLine();
    	String pass;
    	int i;
    	for(i=0; i<=5;i++) {
    		
    		if (inputLine.equals("Daisy")){
    			appendOutput("Please enter the password: ");
    			sendOutput();
    			pass= in.readLine();
    			
    			if (pass.equals("woof")){
    				appendOutput("Welcome Daisy!");
    				appendOutput("Type a message and I will echo it");
    				sendOutput();
    				break;
    				
    			}
    			else {
    				appendOutput("Password not recognized");
    				inputLine="";
    				
    			}
    		}
    		
    		
    		else {
    			appendOutput("Username not recognized.");
    			if(i!=4) {
    				appendOutput("\nEnter username: ");
    				sendOutput();
    				inputLine=in.readLine();
    			}
    		}
    		
    		
    		if (i==5) {
        		disconnect();
        	}
    		
    		
    	}
    	
    
    	
    }
    
    
    // what happens while client and server are connected
    private static void talk() throws IOException {
        /* placing echo functionality into a separate private method allows it to be easily swapped for a different behaviour */
        check();
    	echoClient();
        disconnect();
    }
    
    // repeatedly take input from client and send back in upper case
    private static void echoClient() throws IOException
    {
    	
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            appendOutput(inputLine.toUpperCase());
            sendOutput();
            toConsole(inputLine);
        }
    }
    
    private static void disconnect() throws IOException {
        out.close();
        toConsole("Disconnected.");
        System.exit(0);
    }
    
    // add a line to the next message to be sent to the client
    private static void appendOutput(String line) {
        output += line + "\r";
    }
    
    // send next message to client
    private static void sendOutput() {
        out.println( output + "[EOR]");
        out.flush();
        output = "";
    }
    
    // because it makes life easier!
    private static void toConsole(String message) {
        System.out.println(message);
    }
    
    public static void main(String[] args) {
        try {
            setup();
            talk();
            
            
        }
        catch( IOException ioex ) {
            toConsole("Error: " + ioex );
        }
    }
}

