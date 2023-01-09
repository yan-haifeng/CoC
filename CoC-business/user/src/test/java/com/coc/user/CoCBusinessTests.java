package com.coc.user;

import cn.hutool.core.text.UnicodeUtil;
import com.coc.user.pay.core.exception.PayException;
import com.coc.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootTest
@Slf4j
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
            while ((request = in.readLine()) != null) {
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

    @Resource
    private UserService userService;

    @Test
    public void pay() {
        try {
            System.out.println(userService.payTest());
        } catch (PayException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void test() {
        String s = "{\"operator_name\": \"李建波\",\"operator_phone\": \"18615501228\",\"order_status\": 12,\"push_time\": 1669908673,\"rider_lat\": \"39.943297999999998638\",\"rider_lng\": \"116.33507500000000334\",\"sf_order_id\": \"JS4159036256804\",\"shop_id\": \"3243279847393\",\"shop_order_id\": \"1024\",\"status_desc\": \"配送员已到店\",\"url_index\": \"rider_status\"}";
        System.out.println(UnicodeUtil.toUnicode(s));
    }
}
