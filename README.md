# OS Zero-Copy File Transfer Project

This project demonstrates a server-client file transfer system leveraging zero-copy mechanisms in Java. The primary objective is to efficiently transfer large files between a server and multiple clients using Java NIO (New Input/Output) functionalities.

## Project Overview

The implementation focuses on optimizing file transfer by minimizing the data copying between user space and kernel space. Zero-copy techniques such as `FileChannel.transferTo` and `FileChannel.transferFrom` are utilized to achieve this efficiency.

### Features
- **Server-Side Functionality:**
  - Listens for client connections on a configurable port.
  - Provides a list of available files in a specified directory.
  - Handles file transfer requests from clients using zero-copy mechanisms.

- **Client-Side Functionality:**
  - Connects to the server and retrieves a list of available files.
  - Allows users to select a file to download.
  - Downloads the file efficiently using zero-copy techniques.

## File Structure
- `Server_zerocopy.java`: Implements the server-side application, responsible for managing client connections and transferring files using zero-copy.
- `Client_zerocopy.java`: Implements the client-side application, handling server interaction and downloading files.

## How It Works
### Server Process:
1. Initializes a server socket channel and binds it to a specified port.
2. Provides a list of files from a predefined directory.
3. Uses `FileChannel.transferTo` to transfer the selected file directly to the client's socket channel.

### Client Process:
1. Connects to the server using a socket channel.
2. Retrieves and displays the list of available files.
3. Requests a file by index and uses `FileChannel.transferFrom` to download the file efficiently.

## Usage Instructions
### Prerequisites
- Java Development Kit (JDK) 8 or later.
- A directory containing files to be served by the server.

### Setup
1. Compile the Java files:
   ```bash
   javac Server_zerocopy.java Client_zerocopy.java
   ```

2. Start the server:
   ```bash
   java Server_zerocopy
   ```

3. Start the client:
   ```bash
   java Client_zerocopy
   ```

### Interaction
- The server provides a list of files located in the directory specified in the code (e.g., `/home/woranop29/Videos`).
- The client selects a file index to download the corresponding file.

## Benefits of Zero-Copy
- Reduces CPU overhead by avoiding redundant data copies.
- Improves file transfer speed, especially for large files.
- Optimized for high-performance network applications.

## Improvements and Future Work
- Implement a graphical user interface (GUI) for easier interaction.
- Add authentication and encryption for secure file transfers.
- Extend support for multiple concurrent clients with robust error handling.
---

Feel free to raise any issues or suggestions in the "Issues" section of the repository!

