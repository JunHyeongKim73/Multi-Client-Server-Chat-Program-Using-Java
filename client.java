import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        BufferedReader ins = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", 7777);
            in = new BufferedReader(new InputStreamReader(System.in));
            ins = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String ID = "";
            System.out.print("Enter ID: ");
            ID = in.readLine();
            out.println(ID);
            out.flush();

            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            String msgFromUser = "";
            while (true) {
                msgFromUser = in.readLine();
                if (msgFromUser == null)
                    break;
                out.println(msgFromUser);
                out.flush();
            }

            in.close();
            ins.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader in;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = "";
            while (true) {
                msg = in.readLine();
                if (msg == null)
                    break;
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}