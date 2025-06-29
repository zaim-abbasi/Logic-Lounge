# Java Chat Application

A simple Java chat application for local network communication without internet access.

## What is this?

This is a basic chat room built with Java sockets. Multiple users can connect to a server and chat with each other in real-time. The entire system works on your local network, so you don't need internet access.

## How it works

The application has two main parts:

- **Server**: Runs on one computer and handles all the chat messages
- **Client**: Runs on each user's computer to send and receive messages

When you type a message, it goes to the server, which then sends it to everyone else in the chat room.

## What you need

- Java SDK installed on your computer
- All users must be on the same local network (same WiFi or ethernet)

## How to run it

### Step 1: Start the server

1. Open your IDE or terminal
2. Navigate to the `src` folder
3. Compile and run `Server.java`
4. You'll see "Server up! Awaiting..." when it's ready

### Step 2: Connect clients

1. Open another terminal or IDE window
2. Compile and run `Client.java`
3. Enter your username when prompted
4. Start chatting

You can run multiple clients to have several people in the same chat room.

## Important notes

- Keep the server running the entire time. If it stops, everyone gets disconnected.
- The client code currently connects to "Lenovo-ThinkPad" on port 1234. You might need to change this to your server's computer name or IP address.
- This works best when everyone is connected to the same WiFi network.

## Files in this project

- `Server.java`: The main server that handles all connections and message broadcasting
- `Client.java`: The client application that users run to join the chat
- `ClientHandler.java`: Part of the server code that manages individual client connections

## Troubleshooting

If the client can't connect to the server, check:
1. Is the server running?
2. Are both computers on the same network?
3. Is the server computer name or IP address correct in the client code?

## Making changes

To use this on your network, you'll probably need to change the server address in `Client.java` from "Lenovo-ThinkPad" to your actual server computer's name or IP address.
