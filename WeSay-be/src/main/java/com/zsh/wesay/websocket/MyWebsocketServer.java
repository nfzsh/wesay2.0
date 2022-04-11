package com.zsh.wesay.websocket;

import com.alibaba.fastjson.JSON;
import com.zsh.wesay.dao.MessageEntity;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhushihao
 * @version 1.0
 * @date 2022/4/10 0:53
 */
@Log4j2
@ServerEndpoint("/test/{userName}")
@Component
public class MyWebsocketServer {
    /**
     * 存放所有在线的客户端
     */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private static Vector<MessageEntity> messages = new Vector<>();
    private String userName;
    private static final MessageEntity helloWord = new MessageEntity(LocalDateTime.now(), "服务器", "服务器", "欢迎接入服务器");
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        log.info("有新的客户端连接了: {}", session.getId());
        //将新用户存入在线的组
        this.userName = userName;
        clients.put(session.getId(), session);
        session.getAsyncRemote().sendText(JSON.toJSONString(new Vector<>().addAll(messages)));
        session.getAsyncRemote().sendText(JSON.toJSONString(helloWord));
    }

    /**
     * 客户端关闭
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("有用户断开了, id为:{}", session.getId());
        //将掉线的用户移除在线的组里
        clients.remove(session.getId());
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务端收到客户端发来的消息: {}", message);
        MessageEntity messageEntity = new MessageEntity(LocalDateTime.now(), this.userName, session.getId(), message);
        if (messages.size() >= 10) {
            messages.remove(0);
        }
        messages.add(messageEntity);
        this.sendAll(messageEntity);
        log.info("服务端收到客户端发来的消息汇总: {}", messages);
    }

    /**
     * 群发消息
     *
     * @param message 消息内容
     */
    private void sendAll(MessageEntity message) {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            sessionEntry.getValue().getAsyncRemote().sendText(JSON.toJSONString(message));
        }
    }
}
