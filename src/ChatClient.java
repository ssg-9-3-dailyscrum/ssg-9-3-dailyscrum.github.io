package workshop;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    public static void main(String[] args) {
        int port = 5000;
        String host = "127.0.0.1";

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true
                );
                BufferedReader keyboard = new BufferedReader(
                        new InputStreamReader(System.in, StandardCharsets.UTF_8))
        ) {
            System.out.println("[Client] Connected to " + host + ":" + port);
            String greet = in.readLine();
            if (greet != null) System.out.println(greet);

            System.out.print("Enter your Nickname: ");
            String nickname = keyboard.readLine();
            out.println(nickname);

            // 클라이언트에서 서버 메시지를 별도 스레드로 읽기
            // 서버에서 보내는 메시지 -> 계속 읽고 바로 출력 && 동시에 키보드 입력을 받는 구조
            // 서버 메시지를 계속 읽고있는 스레드 만들기
            new Thread(() -> {
                try {
                    String line;
                    while (true) {

                        line = in.readLine();
                        if (line == null) break;
                        System.out.println("\r"+line);
                        System.out.print("You> ");

                    }
                } catch (IOException e) {
                    System.out.println("[Client] Server disconnected: " + e.getMessage());
                }
            }).start();
//                Thread thread = new Thread() {}; thread.start();
//                Thread thread1 = new Thread(()->{}); thread1.start();
//                new Thread(() -> {}).start();

            // 키보드 입력 처리
            String msg;
            while (true) {
//                System.out.print("You> ");
                msg = keyboard.readLine();
                if (msg == null) break;
                out.println(msg);
                // 서버 응답을 기다림                

                // 서버가 보내는 메시지를 in -> resp로 받는것을 대기하고있음
                // 동시에 msg = keyboard.readLine(); 키보드 입력을 대기하고있음
                // 서버에서 broadcast가 있어도 클라인트가 readline을 호출할때 까지 표시되지 않음
                /*
                String resp = in.readLine();
                if (resp == null) {
                    System.out.println("[Client] Server terminated");
                    break;
                }
                System.out.println(resp);
                */

                if ("quit".equalsIgnoreCase(msg.trim())) break;
            }
            System.out.println("[Client] Disconnected");
        } catch (IOException e) {
            System.err.println("[Server] I/O error: " + e.getMessage());
        }
    }
}
