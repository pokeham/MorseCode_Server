import javax.swing.*;

/**
 * The ServerDrive class is used to instantiate a Server object and call the receiveRequests Method inorder to communicate with the Client
 */
public class ServerDriver {
    /**
     * static void main: used to run the Server class
     * @param args the argument used to run main
     */
    public static void main(String[] args){
        Server test = new Server();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.receiveRequests();
    }
}
