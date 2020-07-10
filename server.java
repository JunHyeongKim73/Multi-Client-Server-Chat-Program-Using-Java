import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class server {
    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7777);
            ArrayList<PrintWriter> outList = new ArrayList<PrintWriter>();
            while (true) {
                System.out.println("client waiting...");
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, outList);
                serverThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;
    private String ID;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<PrintWriter> outList;

    public ServerThread(Socket socket, ArrayList<PrintWriter> outList) {
        this.socket = socket;
        this.outList = outList;
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ID = in.readLine();
            String start = ID + " entered!";
            System.out.println(start);
            synchronized (outList) {
                outList.add(out);
            }
            SendMsgtoAll(start);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String msg = "";
            while (true) {
                msg = in.readLine();
                if (msg == null)
                    break;
                String message = ID + " : " + msg;
                System.out.println(message);
                SendMsgtoAll(message);
            }
            String end = ID + " exited!";
            System.out.println(end);
            SendMsgtoAll(end);
            for (PrintWriter outs : outList) {
                if (outs.equals(out)) {
                    outList.remove(outs);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendMsgtoAll(String msg) {
        for (PrintWriter outs : outList) {
            outs.println(msg);
            outs.flush();
        }
    }
}