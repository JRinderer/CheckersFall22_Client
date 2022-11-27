package edu.lewisu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    private Socket sock = null;

    private DataInputStream in_server = null;
    private DataOutputStream data_out = null;
    private String user_input = "type:get_board";
    private String response = "";

    private Main(String ip_addr, int port) {
        try {
            sock = new Socket(ip_addr, port);
            data_out = new DataOutputStream(sock.getOutputStream());
            in_server = new DataInputStream(sock.getInputStream());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void setColor(Player p) {
        p.ask_color();
    }

    private String parseServerResponse(String key) {
        String return_val = "";
        Map<String, String> map = new HashMap<>();
        String[] pairs = this.response.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyVal = pair.split(":");
            map.put(keyVal[0], keyVal[1]);
        }
        if (map.get(key) == null) {
            return "";
        }
        System.out.println(map.get(key));
        return map.get(key);
    }

    public static Main reEstablishConnection(Main connection){

        Main return_client = null;

        try{
            connection.sock.close();
            return_client =new Main("127.0.0.1", 42691);
        }catch (Exception ex){
            System.out.println("Error in closing the connection");
        }

        return return_client;

    }

    public static void setPlayerColor(Main client, Player thisPLayer){
        try {
            client.data_out.writeUTF(thisPLayer.color);
            //receive the servers response
            client.response = client.in_server.readUTF();
            //check to make sure our server accepted our clients color
            while (!client.parseServerResponse("error").equals("")) {
                client = reEstablishConnection(client);
                setColor(thisPLayer);
                client.data_out.writeUTF(thisPLayer.color);
                //receive the servers response
                client.response = client.in_server.readUTF();
                //client.sock.close();
            }
        }catch (Exception ex){
            System.out.println("error in setting color " + ex);
        }
    }

    public static void playGame(Main client, Player thisPlayer){
        try {
            while (true) {
                client = new Main("127.0.0.1", 42691);
                client.data_out.writeUTF("type:get_turn");
                client.response = client.in_server.readUTF();
                System.out.println("Player " + client.parseServerResponse("player_turn") + " is up");
                System.out.println(client.parseServerResponse("board"));
                if ((client.parseServerResponse("player_turn")).equals(thisPlayer.getColor_str())) {
                    client.sock.close();
                    client = new Main("127.0.0.1", 42691);
                    thisPlayer.ask_piece();
                    thisPlayer.ask_x();
                    thisPlayer.ask_y();
                    //client.user_input = "type:move,color:R,piece_name:R2-3,x_cord:3,y_cord:2";
                    thisPlayer.set_send_message(thisPlayer.getPiece_input() + "," + thisPlayer.getX_input() + "," + thisPlayer.getY_input() + ",color:" + thisPlayer.getColor_str());
                    try {
                        client.data_out.writeUTF(thisPlayer.client_messg);
                        client.response = client.in_server.readUTF();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                } else if (!client.parseServerResponse("error").equals("")) {
                    System.out.println(client.parseServerResponse("error"));
                }
                thisPlayer.setBoard(client.parseServerResponse("board"));
                System.out.println(thisPlayer.getBoard());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception ex) {
                    System.out.println("error in sleep");
                }
            }
        }catch (Exception ex){
            System.out.println("Error in playing game " + ex);
        }
    }

    public static void printBoard(Main client, Player thisPlayer){
        thisPlayer.setBoard(client.parseServerResponse("board"));
        System.out.println(thisPlayer.getBoard());
        System.out.println("The players color is: " + thisPlayer.getColor_str());
    }

    public static void main(String[] args) {
        Player thisPLayer = new Player();
        //ask for player color
        setColor(thisPLayer);
        String currnt_board = "";
        //establish the initial connection
        Main client = new Main("127.0.0.1", 42691);
        //attempt to set the player color
        //we will only return from this function if the player is set
        setPlayerColor(client,thisPLayer);
        //if we're able to set the color display the board
        printBoard(client,thisPLayer);
        //play the game
        playGame(client,thisPLayer);
    }
}