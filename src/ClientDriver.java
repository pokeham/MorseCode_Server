import javax.swing.*;

/**
 * Class ClientDriver is used to instantiate a Client object and call its waitForPackets function
 */
public class ClientDriver {
    /**
     * void main: used to run the client
     * @param args the argument used to run main
     */
    public static void main(String[] args){
        Client test = new Client();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.waitForPackets();
    }
}
