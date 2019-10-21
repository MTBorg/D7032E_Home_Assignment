package src.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Stream {
  private Socket socket;
  private DataOutputStream out;
  private BufferedReader in;

  public Stream(Socket socket) throws Exception {
    this.socket = socket;
    this.out = new DataOutputStream(this.socket.getOutputStream());
    this.in =
      new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public String readLine() {
    String result = "";
    try {
      result = in.readLine();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return result;
  }

  public void writeBytes(String bytes) {
    try {
      out.writeBytes(bytes);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
