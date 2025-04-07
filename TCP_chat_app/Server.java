import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Server{
    private static ArrayList<ClientHandler> clientList = new ArrayList<>();
    public static void main(String args[]){
        try{
            ServerSocket serverSocket = new ServerSocket(6666, 0, InetAddress.getByName("192.168.5.60"));
            System.out.println("Server is running...");

            //Thread to handle scanning and sending messages from server to client
            new Thread (() -> {
                Scanner scanner = new Scanner(System.in);
                while(true){
                    String message = scanner.nextLine();
                    broadcast("[Server]: " + message, null);
                }
            }).start();

            while(true){
                Socket clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket);
                clientList.add(client);
                new Thread(client).start();
            }
            
        } catch(IOException e){ e.printStackTrace(); }

    }

    public static void broadcast(String message, ClientHandler sender){
        for(ClientHandler client: clientList){
            if(client != sender){
                client.send(message);
            }
        }
    }

    private static class ClientHandler implements Runnable{
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket){
            this.clientSocket = socket;

            try{
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e){ e.printStackTrace(); }
        }

        @Override
        public void run(){
            try{
                //Ask for username
                out.println("What's your username?");
                username = in.readLine();
                System.out.println("User " + username + " has connected.");
                out.println("Welcome to the chat " + username +", print your message and hit enter!");
                
                String message;
                while((message = in.readLine()) != null){
                    System.out.println("["+ username + "]: " + message);
                    broadcast("["+ username + "]: " + message, this);
                }

                //Once connection is done, disconnect and close
                clientList.remove(this);
                System.out.println("User " + username + " has disconnected.");
            } catch(IOException e){
                e.printStackTrace();
            } finally{
                try{
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch(IOException e){ e.printStackTrace(); }
            }
        }

        public void send(String message){
            out.println(message);
        }
    }
}

