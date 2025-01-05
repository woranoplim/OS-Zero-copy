import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.file.*;

public class Client_zerocopy{
    private static final String SERVER_ADDRESS = "xxx.xxx.xxx.xxx";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT))) {
            DataOutputStream dos = new DataOutputStream(Channels.newOutputStream(socketChannel));
            DataInputStream dis = new DataInputStream(Channels.newInputStream(socketChannel));

            int fileCount = dis.readInt();
            System.out.println("Available files:");
            for (int i = 0; i < fileCount; i++) {
                System.out.println(i + ": " + dis.readUTF());
            }

            System.out.print("Enter the file index to download: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int fileIndex = Integer.parseInt(reader.readLine());
            dos.writeInt(fileIndex);

            String fileName = dis.readUTF();
            long fileSize = dis.readLong();
            System.out.println("Downloading " + fileName + " ...");

            long startTime = System.currentTimeMillis();
            Path outputPath = Paths.get("downloaded_" + fileName);
            try (FileChannel fileChannel = FileChannel.open(outputPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                long position = 0;
                while (position < fileSize) {
                    position += fileChannel.transferFrom(socketChannel, position, fileSize - position);
                }
            }

            System.out.println("Downloaded " + fileName + " in " + ((System.currentTimeMillis() - startTime)/1000) + " sec");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}