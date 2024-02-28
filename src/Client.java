import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private final String username;

    public Client(String username) {
        this.username = username;
        connect();
    }

    private void connect() {
        boolean firstAttempt = true;
        while (true) {
            try {
                this.socket = new Socket("localhost", 1234);
                System.out.println("Connected to server!");
                break;
            } catch (IOException e) {
                if (firstAttempt) {
                    System.out.println("Server down. Reconnecting...");
                    firstAttempt = false;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendMessage(Scanner scanner) throws IOException {
        try (PrintStream out = new PrintStream(socket.getOutputStream())) {
            out.println(username);
            while (socket.isConnected() && scanner.hasNextLine())
                out.println(username + ": " + scanner.nextLine());
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String messageFromGroupChat;
                while (true) {
                    if (socket.isConnected() && (messageFromGroupChat = in.readLine()) != null)
                        System.out.println(messageFromGroupChat);
                }
            } catch (SocketException e) {
                System.out.println("Server down. Exiting...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username: ");
        Client client = new Client(scanner.nextLine());
        client.listenForMessage();
        client.sendMessage(scanner);
    }
}