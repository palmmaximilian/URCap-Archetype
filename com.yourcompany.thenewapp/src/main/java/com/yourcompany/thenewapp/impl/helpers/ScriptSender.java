package com.yourcompany.thenewapp.impl.helpers;

// Import the Real Time client reader class to this class
import com.palmsoftware.keyencevision.impl.helpers.RobotRealtimeReader;
import java.io.DataOutputStream;
import java.io.IOException;
// import java.io.InputStreamReader;
import java.net.Socket;

// import java.nio.charset.StandardCharsets;

public class ScriptSender {

  // IP of the robot
  private final String TCP_IP;
  // Port for secondary client
  private final int TCP_port = 30002;
  private Socket sc;
  private Socket dashboardsocket;
  private Socket interpreterSocker;
  DataOutputStream out;
  DataOutputStream dashboardOut;
  DataOutputStream interpreterOut;

  // Creating handle to getRobotRealtimeData class
  RobotRealtimeReader realtimeReader = new RobotRealtimeReader();

  /**
   * Default constructor, using localhost IP (127.0.0.1)
   */
  public ScriptSender() {
    this.TCP_IP = "127.0.0.1";
  }

  /**
   * Constructor for IP different from localhost
   * @param IP the IP address of the robot
   */
  public ScriptSender(String IP) {
    this.TCP_IP = IP;
  }

  /**
   * Method used to send a ScriptCommand as a primary program to the Secondary Client Interface.
   * If called while a program is already running, the existing program will halt.
   * @param command the ScriptCommand object to send.
   */
  public String sendScriptCommand(ScriptCommand command) {
    return (sendToSecondary(command.toString()));
  }

  public boolean OpenConnection() {
    try {
      // Create a new Socket Client
      dashboardsocket = new Socket(TCP_IP, 29999);
      if (dashboardsocket.isConnected()) {
        dashboardOut = new DataOutputStream(dashboardsocket.getOutputStream());
      }

      sc = new Socket(TCP_IP, TCP_port);
      if (sc.isConnected()) {
        out = new DataOutputStream(sc.getOutputStream());
        System.out.println("Connected to secondary client");
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      System.out.println(e);
      return false;
    }
  }

  public boolean CloseConnection() {
    try {
      dashboardsocket.close();

      sc.close();
      out.close();
      System.out.println("Closed connection to secondary client");
      return true;
    } catch (IOException e) {
      System.out.println(e);
      return false;
    }
  }

  // Internal method that sends script to client
  private String sendToSecondary(String command) {
    realtimeReader.readNow();

    Integer robot_mode = (realtimeReader.getRobotMode()).intValue();

    if (robot_mode != 7) {
      try {
        dashboardOut.flush();
        command = "popup " + "Please turn the Robot on first!" + "\n";
        dashboardOut.write(command.getBytes("US-ASCII"));
        dashboardOut.flush();
      } catch (IOException e) {
        System.out.println(e);
      }

      return "Robot was not turned on!";
    }

    String response = null;
    // System.out.println("Sending command: " + command);
    try {
      if (sc.isConnected()) {
        out.flush();
        // // System.out.println(command);
        // out.write(command.getBytes("US-ASCII"));
        // out.flush();

        // System.out.println("Command sent");

        String[] allLines = command.split("\n"); // Split into individual lines

        // Send 100 lines at a time
        for (int i = 0; i < allLines.length; i += 100) {
          // Build chunk of next 100 lines
          StringBuilder chunk = new StringBuilder();
          int end = Math.min(i + 100, allLines.length);

          for (int j = i; j < end; j++) {
            chunk.append(allLines[j]).append("\n"); // Add line with newline
          }

          // Send this chunk
          if (sc.isConnected()) {
            out.write(chunk.toString().getBytes("US-ASCII"));
            out.flush();
            System.out.println("Sent lines " + i + "-" + (end - 1));
          }
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {}
        }
      }
    } catch (IOException e) {
      System.out.println(e);
    }
    return response;
  }
}
