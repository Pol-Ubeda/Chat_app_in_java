import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client1 {
    public static void main(String args[]) throws IOException{
        try{
            Socket client = new Socket("localhost",6666);
        
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            new Thread(() -> {
                try{
                    String input;
                    while ((input = in.readLine()) != null){
                        System.out.println(input);
                    }
                } catch(IOException e){ e.printStackTrace();}
            }).start();

            Scanner scanner = new Scanner(System.in);
            String message;
            while(true){
                message = scanner.nextLine();
                out.println(message);
            }
        } catch(IOException e){ e.printStackTrace();}
    }
}