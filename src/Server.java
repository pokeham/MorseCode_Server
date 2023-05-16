import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The Server Class extends JFrame and is used to receive packets from the client class convert them then send them back to the CLient
 */
public class Server extends JFrame {
    /**
     * DatagramPacket request: private member variable used to store the received packet from the Client
     */
    private DatagramPacket request;
    /**
     * DatagramPacket response: private member variable used to store the response packet to be sent to the Client
     */
    private DatagramPacket response;
    /**
     * DatagramSocekt socket: private member variable used to send and recieve packets between the Client
     */
    private DatagramSocket socket;
    /**
     * MTE: a hashmap used to convert from Morse to English
     */
    private final HashMap<String,Character> MTE;
    /**
     * ETM: a hashmap used to convert from English to Morse
     */
    private final HashMap<Character,String> ETM;
    /**
     * JTextArea displayArea: an area used to house text on the JFrame
     */
    private final JTextArea displayArea;

    /**
     * The Server Constructor: initializes the hashmaps with their respective values and the socket;
     */
    public Server(){
        super("Server");
        MTE = new HashMap<String,Character>(){{
           put("._",'A');
           put("_...",'B');
           put("_._.",'C');
           put("_..",'D');
           put(".",'E');
           put(".._.",'F');
           put("__.",'G');
           put("....",'H');
           put("..",'I');
           put(".___",'J');
           put("_._",'K');
           put("._..",'L');
           put("__",'M');
           put("_.", 'N');
           put("___",'O');
           put(".__.",'P');
           put("__._",'Q');
           put("._.",'R');
           put("...",'S');
           put("_",'T');
           put(".._",'U');
           put("..._",'V');
           put(".__",'W');
           put("_.._",'X');
           put("_.__",'Y');
           put("__..",'Z');
           put(".____", '1');
           put("..___",'2');
           put("...__",'3');
           put("...._",'4');
           put(".....",'5');
           put("_....",'6');
           put("__...",'7');
           put("___..",'8');
           put("____.",'9');
           put("_____",'0');
           put("._._._",'.');
           put("__..__",',');
           put("..__..",'?');
        }};
        ETM = new HashMap<Character,String>(){{
            put('A',"._");
            put('B',"_...");
            put('C',"_._.");
            put('D',"_..");
            put('E',".");
            put('F',".._.");
            put('G',"__.");
            put('H',"....");
            put('I',"..");
            put('J',".___");
            put('K',"_._");
            put('L',"._..");
            put('M',"__");
            put('N',"_.");
            put('O',"___");
            put('P',".__.");
            put('Q',"__._");
            put('R',"._.");
            put('S',"...");
            put('T',"_");
            put('U',".._");
            put('V',"..._");
            put('W',".__");
            put('X',"_.._");
            put('Y',"_.__");
            put('Z',"__..");
            put('1',".____");
            put('2',"..___");
            put('3',"...__");
            put('4',"...._");
            put('5',".....");
            put('6',"_....");
            put('7',"__...");
            put('8',"___..");
            put('9',"____.");
            put('0',"_____");
            put('.',"._._._");
            put(',',"__..__");
            put('?',"..__..");
        }};
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300,300);
        setVisible(true);
        try{
            socket = new DatagramSocket(23615);
        }catch(SocketException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * void receiveRequests: Runs until the JFrame is closed and waits for any incoming requests from the client and then class the sendResponse method to send a response back to the Client
     */

    public void receiveRequests(){
        while(true){
            try {
                byte[] data = new byte[256];
                request = new DatagramPacket(data, data.length);
                displayMessage("\nWaiting...");
                socket.receive(request);
                displayMessage("\nReceived!");
                sendResponse(request);
            }catch (IOException e){
                System.out.println("IoException");
            }
        }

    }

    /**
     * String convertEtM: this function converts a String parsed from a packet from english to Morse code using the private member variable ETM
     * @return returns a string of Morse code
     */
    public String convertEtM(){
        String string = new String(request.getData(),0, request.getLength());
        StringBuilder result = new StringBuilder();
        string = string.toUpperCase();
        for(int i = 0; i < string.length();i++){
            if(string.charAt(i) == ' '){
                result.append("    ");
            }else{
                result.append(ETM.get(string.charAt(i)));
                result.append(" ");
            }
        }
        return result.toString();
    }
    /**
     * String convertMtE: this function converts a String parsed from a packet from Morse code to english using the private member variable MTE
     * @return returns a string of English
     */
    public String convertMtE(){
        String string = new String(request.getData(),0, request.getLength());
        String[] split = string.split(" ");
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (String s : split) {
            if (s.trim().length() == 0) {
                count += 1;
                if (count == 3) {
                    count = 0;
                    result.append(" ");
                }
            } else {
                result.append(MTE.get(s));
            }
        }
        return  result.toString();
    }

    /**
     * void sendResponse: takes a packet as a parameter and parses a string from it and determines if it is morse code or english then calls the respective function (either EtM or MtE).
     * it then uses the private member variable socket to send this new packet to the client
     * @param packet a request packet that is to be converted
     * @throws IOException in case socket.send fails
     */
    public void sendResponse(DatagramPacket packet) throws IOException{
        String test = new String(request.getData(),0, request.getLength());
        byte[] buf;
        if(test.indexOf('_')==-1){
            buf = convertEtM().getBytes();
        }else{
            buf = convertMtE().getBytes();
        }
        response = new DatagramPacket(buf,buf.length,packet.getAddress(), packet.getPort());
        socket.send(response);
        displayMessage("\nPacket Sent!");
    }

    /**
     * void displayMessage: appends a passed String to the JFrame
     * @param message a String that is to be appended to the JFrame
     */
    private void displayMessage(final String message){
        SwingUtilities.invokeLater(
                () -> displayArea.append(message)
        );
    }
}
