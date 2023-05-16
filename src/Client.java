import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * The Client Class extends JFrame and is used to communicate with the Server Class
 */
public class Client extends JFrame {
    /**
     * final JTextArea displayArea: private member variable used to house and display text to the JFrame Client
     */
    private final JTextArea displayArea;
    /**
     * final JTextField MorseField: private member variable used to take user input in morse to be sent as a package to the Server Class
     */
    private final JTextField MorseField;
    /**
     * final JTextField MorseField: private member variable used to take user input in english to be sent as a package to the Server Class
     */
    private final JTextField EnglishField;
    /**
     * DatagramSocket socket: private member variable used to send and receive packets and communicate with the Server Class
     */
    private DatagramSocket socket;

    /**
     * the Client Class constructor initializes the JFrame and adds action listeners to the private member textfeilds and initializes the socket.
     */
    public Client(){
        super("Client");
        MorseField = new JTextField("Enter Morse Code Here!");
        MorseField.addActionListener(
                new ActionListener() {
                    /**
                     * void actionPerformed: is called when an action is performed on the MorseField text-field, it gets the message housed in the text-field and converts it
                     * to a packet that is then sent thru the socket. It also displays to the JFrame that the packet has been sent.
                     * @param event the action event
                     */
                    public void actionPerformed(ActionEvent event) {
                        try{
                            String message = event.getActionCommand();
                            displayArea.append("\nSending packet containing: " + message + "\n\t(to be converted to English)");
                            byte[] data = message.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(data, data.length,InetAddress.getLocalHost(),23615);
                            socket.send(sendPacket);
                            displayArea.append("\nPacket sent \n");
                            displayArea.setCaretPosition(displayArea.getText().length());
                        }catch (IOException ioException){
                            displayMessage( ioException +"\n");
                            ioException.printStackTrace();
                        }
                    }
                }
        );
        EnglishField = new JTextField("Enter English Here!");
        EnglishField.addActionListener(
                new ActionListener() {
                    @Override
                    /**
                     * void actionPerformed: is called when an action is performed on the EnglishField text-field, it gets the message housed in the text-field and converts it
                     * to a packet that is then sent thru the socket. It also displays to the JFrame that the packet has been sent.
                     * @param event the action event
                     */
                    public void actionPerformed(ActionEvent actionEvent) {
                        try{
                            String message = actionEvent.getActionCommand();
                            displayArea.append("\nSending Packet containing: " + message +"\n\t(to be converted to Morse Code)");
                            byte[] data = message.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(data,data.length,InetAddress.getLocalHost(),23615);
                            socket.send(sendPacket);
                            displayArea.append("\nPacketSent \n");
                            displayArea.setCaretPosition(displayArea.getText().length());
                        }catch(IOException e){
                            displayMessage(e + "\n");
                            e.printStackTrace();
                        }
                    }
                }
        );
        setResizable(false);
        add(EnglishField,BorderLayout.NORTH);
        add(MorseField, BorderLayout.SOUTH);
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea),BorderLayout.CENTER);
        setSize(500,600);
        setVisible(true);
        try{
            socket = new DatagramSocket();
        }catch (SocketException socketException){
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * void waitForPackets: runs until the JFrame is closed and waits to receive a packet thru the socket it then displays the packets contents along with some additional data
     */
    public void waitForPackets(){
        while(true){
            try{
                byte[] data = new byte[256];
                DatagramPacket recievePacket = new DatagramPacket(data,data.length);
                socket.receive(recievePacket);
                displayMessage("\nPacket Received: " + "\nFrom host: " + recievePacket.getAddress()
                                + "\nPort: " + recievePacket.getPort() + "\nLength: " + recievePacket.getLength() +
                                "\nContaining:\n\t" + new String(recievePacket.getData(),0, recievePacket.getLength())+"\n\n");
            }catch (IOException e){
                displayMessage(e + "\n");
                e.printStackTrace();
            }

        }
    }

    /**
     * void displayMessage: is used to display messages to the JFrame
     * @param message a string message to be appended to the JFrame
     */
    private void displayMessage(final String message){
        SwingUtilities.invokeLater(
                () -> displayArea.append(message)
        );
    }
}
