import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootTest
public class CoCBusinessTests {

    @Test
    public void bioDemo() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            // accept方法会一直阻塞到一个连接建立
            Socket clientSocket = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request, response;
            while((request = in.readLine()) != null){
                // 客户端发送“Done”才中断处理循环
                if ("Done".equals(request)) {
                    break;
                }

                // 处理客户端的请求
//                response = processRequest(request);

                // 响应
//                out.println(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
