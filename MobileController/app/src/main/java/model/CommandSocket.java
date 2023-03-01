package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommandSocket {

    Socket socket;
    BufferedReader in;

    /**
     * @brief Constructor of Command Socket. No socket is assigned.
     */
    public CommandSocket() {
        socket = null;
        in = null;
    }

    /**
     * @brief Establish a connection to a socket.
     * @param host IP of the host of socket.
     * @param port Port of the socket.
     * @return True if connection is established successfully. False otherwise.
     */
    public boolean connect(String host, int port) {
        boolean status = true;

        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            status = false;
        }

        return status;
    }

    /**
     * @brief Gets message from socket. It reads entire lines, separated by newlines.
     * @return String of message. In case of IO error, empty string is returned.
     */
    public String getMessage() {
        String data = null;

        try {
            data = in.readLine();
        } catch (IOException e) {
            data = "";
        }

        return data;
    }

    /**
     * @brief Return the status of socket.
     * @return True if connection is closed. False otherwise.
     */
    public boolean isClosed() {
        return socket.isClosed();
    }
}
