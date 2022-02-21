package tw.com.handler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;
import tw.com.util.HttpUtil;

@Slf4j
@Service
public class MyWebSocketHandler extends TextWebSocketHandler
{

    /**
     * socket 連線建立成功事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        log.info("連線建立開始");

        String key = this.getKey(session);

        // 連線建立成功
        if (StringUtils.isNoneBlank(key))
        {
            WsSessionManager.add(key, session);
            this.sendInfo(key, "websocket success");
        }
    }

    /**
     * socket 結束連線事件
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        String key = this.getKey(session);
        WsSessionManager.remove(key);
    }

    /**
     * socket 連線異常事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception
    {
        String key = this.getKey(session);
        log.error("[{}]連線異常", key);

        if (WsSessionManager.get(key) != null)
            WsSessionManager.remove(key);
    }

    /**
     * socket 接收訊息事件
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
    {
        // 取得Client傳遞過來的訊息
        String payload = message.getPayload();
        String key = this.getKey(session);
        log.info("server 接收到 " + key + " 發送的訊息: " + payload);
    }

    /**
     * socket 發送訊息事件
     *
     * @param key
     * @param message
     * @throws Exception
     */
    public void sendInfo(String key, String message) throws IOException
    {

        WebSocketSession wsSession = WsSessionManager.get(key);
        if (wsSession != null)
        {
            log.info("server發送訊息給[{}], 內容:{}", key, message);
            wsSession.sendMessage(new TextMessage(message));
        } else
        {
            log.error("找不到[{}]", key);
        }
    }

    private String getKey(WebSocketSession session)
    {
        // 取得請求參數
        log.info("http uri:{}", session.getUri().getQuery());
        Map<String, String> paramMap = HttpUtil.queryStrToMap(session.getUri().getQuery());
        String userId = MapUtils.getString(paramMap, "userId", "");
        return userId;
    }

    private static class WsSessionManager
    {
        // 用來記錄目前連線數
        private static int onlineCount = 0;

        // 儲存連線 session的地方
        private static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

        public static void add(String key, WebSocketSession session)
        {
            if (SESSION_POOL.containsKey(key))
            {
                SESSION_POOL.remove(key);
                SESSION_POOL.put(key, session);
            } else
            {
                SESSION_POOL.put(key, session);
                addOnlineCount();
            }

            log.info("[{}]連線建立, 目前總連線數:{}", key, onlineCount);
        }

        public static void remove(String key)
        {
            if (SESSION_POOL.containsKey(key))
            {
                SESSION_POOL.remove(key);
                subOnlineCount();
            }
            log.info("[{}]連線退出, 目前總連線數:{}", key, onlineCount);
        }

        public static WebSocketSession get(String key)
        {
            return SESSION_POOL.get(key);
        }

        public static synchronized int getOnlineCount()
        {
            return onlineCount;
        }

        private static synchronized void addOnlineCount()
        {
            WsSessionManager.onlineCount++;
        }

        private static synchronized void subOnlineCount()
        {
            WsSessionManager.onlineCount--;
        }

    }

}
