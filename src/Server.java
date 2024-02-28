import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server up! Awaiting...");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        while (true) {
            executorService.execute(new ClientHandler(serverSocket.accept()));
            System.out.println("Client connected!");
        }
    }

    private void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public static void main(String[] args) throws IOException {
        new Server(1234);
    }
}

class ClientHandler implements Runnable {
    private final String username;
    private static final List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;

    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        clientHandlers.add(this);
        this.username = bufferedReader.readLine();
        broadcastMessage(username + " joined!");
    }

    @Override
    public void run() {
        try {
            String messageFromClient;
            while ((messageFromClient = bufferedReader.readLine()) != null) {
                broadcastMessage(messageFromClient);
            }
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeEverything();
        }
    }

    public void broadcastMessage(String messageToSend) {
        clientHandlers.stream().filter(clientHandler -> !clientHandler.equals(this)).forEach(clientHandler -> {
            try {
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void closeEverything() {
        clientHandlers.remove(this);
        broadcastMessage(username + " left!");
        try {
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}