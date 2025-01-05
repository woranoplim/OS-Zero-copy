import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.file.*;

public class Server_zerocopy {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(PORT));
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try (SocketChannel clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected: " + clientSocket.getRemoteAddress());
                    handleClient(clientSocket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(SocketChannel clientSocket) throws IOException {
        DataInputStream dis = new DataInputStream(Channels.newInputStream(clientSocket));
        DataOutputStream dos = new DataOutputStream(Channels.newOutputStream(clientSocket));

        // Example: Send available files from the "files" directory
        File[] files = new File("/home/woranop29/Videos").listFiles();
        if (files == null || files.length == 0) {
            dos.writeInt(0);
            System.out.println("No files found to send.");
            return;
        }

        dos.writeInt(files.length);
        for (File file : files) {
            dos.writeUTF(file.getName());
        }

        // Receive file index from client
        int fileIndex = dis.readInt();
        if (fileIndex < 0 || fileIndex >= files.length) {
            System.out.println("Invalid file index received: " + fileIndex);
            return;
        }

        // Send file to client
        File fileToSend = files[fileIndex];
        dos.writeUTF(fileToSend.getName());
        dos.writeLong(fileToSend.length());

        try (FileChannel fileChannel = FileChannel.open(fileToSend.toPath(), StandardOpenOption.READ)) {
            long position = 0;
            while (position < fileToSend.length()) {
                position += fileChannel.transferTo(position, fileToSend.length() - position, clientSocket);
            }
        }
        System.out.println("File sent: " + fileToSend.getName());
    }
}