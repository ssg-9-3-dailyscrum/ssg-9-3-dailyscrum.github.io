package workshop;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {
    private static final int PORT = 5000;   // 포트번호지정
    private static final ExecutorService POOL = Executors.newCachedThreadPool();
    private static final AtomicInteger CLIENT_SEQ = new AtomicInteger(1);
    
    // 현재 연결된 클라이언트들을 저장할 ArrayList 선언
    private static final ArrayList<PrintWriter> CLIENTS_LIST = new ArrayList<>();
    // 닉네임 중복검사를 위해 닉네임을 저장할 HashSet 선언 -> 중복을 저장하지 못하게
    private static final Set<String> NICKNAMES = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Server starting on port " + PORT);

        // 서버를 Ctrl+C로 종료할 때 스레드 풀을 정리하도록 훅 등록
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[Server] Shutting down...");
            POOL.shutdownNow();
        }));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                // 클라이언트에게 번호 할당
                int id = CLIENT_SEQ.getAndIncrement();
                System.out.println("[Server] Client#" + id + " connected from " + socket.getRemoteSocketAddress());
                POOL.submit(new ClientHandler(socket, id));
            }
        } catch (IOException e) {
            System.err.println("[Server] Error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final int clientId;

        ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            String nickname = null;
            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
            ) {

                out.println("Welcome! You are Client#" + clientId + ". Type [NICK nickname] to set nickname. Type 'quit' to quit.");


                String nicknameInput = in.readLine();
                if (nicknameInput != null && nicknameInput.startsWith("NICK ")) {
                    nickname = nicknameInput.substring(5).trim();
                    // NICKNAMES 동시에 여러 스레드가 접근할 수 있으므로 synchronized로 락
                    synchronized (NICKNAMES) {
                        if (NICKNAMES.contains(nickname)) {
                            out.println("ERR ... Nickname already used.");
                            socket.close();
                            return;
                        }
                        NICKNAMES.add(nickname);
                    }
                    System.out.println("[Server] Client #" + clientId + ": " + nickname);
                } else {
                    out.println("ERR ... 잘못된 접근 방법입니다.");
                    socket.close();
                    return;
                }

                // CLIENTS_LIST에 동시에 여러 스레드가 접근할 수 있으므로 synchronized로 락
                synchronized (CLIENTS_LIST) {
                    CLIENTS_LIST.add(out);  // 입장 시
                }
                broadcast("[System] " + nickname + " joined");


                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("[Server] From Client#" + nickname + ": " + line);
                    if ("/quit".equalsIgnoreCase(line.trim())) {
                        out.println("Bye!");
                        break;
                    } else if ("/who".equalsIgnoreCase(line.trim())) {
                        synchronized (CLIENTS_LIST) { // 동시에 여러 스레드가 접근할 수 있으므로 리스트를 락 객체로 사용
                            for(PrintWriter client: CLIENTS_LIST) out.println(client);
                        }
                    }
                    broadcast("[" + nickname + "] " + line);
                }
            } catch (IOException e) {
                System.err.println("[Server] ERR ... Client#" + clientId + " I/O error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                System.out.println("[Server] Client#" + clientId + " disconnected.");
            }

        }

        private void broadcast(String input) {
            synchronized (CLIENTS_LIST) { // 동시에 여러 스레드가 접근할 수 있으므로 리스트를 락 객체로 사용
                for (PrintWriter writer : CLIENTS_LIST) {
                    writer.println(input);  // 접속된 모든 클아이언트에게 메시지 전송
                }
            }
        }


    }
}
