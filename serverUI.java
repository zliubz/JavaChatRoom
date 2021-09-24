
import java.io.*;
import java.net.*;
import java.util.ArrayList;





class server{
    int num = 0;
    ArrayList<Socket> sockets = new ArrayList<Socket>();
    class serverinstance implements Runnable{
        @Override
        public void run() {
        try{   
            while(true){
                BufferedReader reader = null;
                PrintWriter writer = null;
                synchronized(this){
                    reader = new BufferedReader(new InputStreamReader(sockets.get(num).getInputStream()));
                    writer = new PrintWriter(sockets.get(num).getOutputStream());
                    num++;
                }
                writer.println("Server Message: Connection Constructed!");
                writer.flush();
                String text = null;
                while((text = reader.readLine())!=null){
                    tellAll(text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        void tellAll(String message){
            for(Socket sock:sockets){
                try {
                    PrintWriter writer = new PrintWriter(sock.getOutputStream());
                    writer.println(message);
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void go(){
            try {
                ServerSocket serverSocket = new ServerSocket(5000);
                Runnable clienthandle = new serverinstance();
                while(true){
                    sockets.add(serverSocket.accept());
                    Thread temp = new Thread(clienthandle);
                    temp.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
    }
}

public class serverUI {
    public static void main(String[] args){
        server s = new server();
        s.go();
    }      
}
