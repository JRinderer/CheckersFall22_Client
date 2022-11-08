import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Main {

    private Socket sock = null;

    private DataInputStream in_server = null;
    private DataOutputStream data_out = null;
    private String user_input = "type:get_board";
    private String response = "";

    private Main(String ip_addr, int port){
        try{
            sock = new Socket(ip_addr,port);
            data_out = new DataOutputStream(sock.getOutputStream());
            in_server = new DataInputStream(sock.getInputStream());
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        /*
        {
            key: value, key: value
        }
        //get board
            type:get_board
         */
        Main client = new Main("127.0.0.1",42691);
        try{
            //sends user message
            client.data_out.writeUTF(client.user_input);
            client.response = client.in_server.readUTF();
            System.out.println(client.response);

        }catch (Exception ex){
            System.out.println(ex);
        }

    }
}