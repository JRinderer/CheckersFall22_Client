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

    public static void main(String[] args) {
        /*
        {
            key: value, key: value
        }
        //get board
            type:get_board
         */

        Player thisPLayer = new Player();
        //ask for player color
        setColor(thisPLayer);
        String currnt_board = "";
        Main client = new Main("127.0.0.1", 42691);
        try {
            //sending our color to the server
            client.data_out.writeUTF(thisPLayer.color);
            //receive the servers response
            client.response = client.in_server.readUTF();
            //check to make sure our server accepted our clients color
            while (!client.parseServerResponse("error").equals("")) {
                client.sock.close();
                client = new Main("127.0.0.1", 42691);
                setColor(thisPLayer);
                client.data_out.writeUTF(thisPLayer.color);
                //receive the servers response
                client.response = client.in_server.readUTF();
                client.sock.close();
            }
            thisPLayer.setBoard(client.parseServerResponse("board"));
            System.out.println(thisPLayer.getBoard());
            System.out.println("The players color is: " + thisPLayer.getColor_str());
            while (true) {
                client = new Main("127.0.0.1", 42691);
                client.data_out.writeUTF("type:get_turn");
                client.response = client.in_server.readUTF();
                System.out.println("Player " + client.parseServerResponse("player_turn") + " is up");
                System.out.println(client.parseServerResponse("board"));
                if ((client.parseServerResponse("player_turn")).equals(thisPLayer.getColor_str())) {
                    client.sock.close();
                    client = new Main("127.0.0.1", 42691);
                    thisPLayer.ask_piece();
                    thisPLayer.ask_x();
                    thisPLayer.ask_y();
                    //client.user_input = "type:move,color:R,piece_name:R2-3,x_cord:3,y_cord:2";
                    thisPLayer.set_send_message(thisPLayer.getPiece_input() + "," + thisPLayer.getX_input() + "," + thisPLayer.getY_input() + ",color:" + thisPLayer.getColor_str());
                    try {
                        client.data_out.writeUTF(thisPLayer.client_messg);
                        client.response = client.in_server.readUTF();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }else if(!client.parseServerResponse("error").equals("")){
                    System.out.println(client.parseServerResponse("error"));
                }
                thisPLayer.setBoard(client.parseServerResponse("board"));
                System.out.println(thisPLayer.getBoard());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception ex) {
                    System.out.println("error in sleep");
                }
            }
            //==================ORIGINAL CLIENT
            //sends user message
            /*
            client.data_out.writeUTF(client.user_input);
            client.response = client.in_server.readUTF();
            System.out.println(client.response);
            //moving pieces
            /*
                this.color = parseClientMessage("color");
                piece_name = parseClientMessage("piece_name");
                x_cord = parseClientMessage("x_cord");
                y_cord = parseClientMessage("y_cord");
             */


            /*
            client.user_input = "type:move,color:R,piece_name:R2-3,x_cord:3,y_cord:2";
            client.data_out.writeUTF(client.user_input);
            client.response = client.in_server.readUTF();
            System.out.println(client.response);

             */

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }
}