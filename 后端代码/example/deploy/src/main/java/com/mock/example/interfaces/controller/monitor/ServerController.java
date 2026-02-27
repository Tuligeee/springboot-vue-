package com.mock.example.interfaces.controller.monitor;

import com.mock.example.common.component.server.Server;
import com.mock.example.common.entity.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author: Mock
 * @date: 2025-01-05 08:44:11
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    /**
     * 获取服务信息
     *
     * @return 服务信息
     * @throws Exception
     */
    @GetMapping()
    public Response<Server> getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return new Response(server);
    }
}
