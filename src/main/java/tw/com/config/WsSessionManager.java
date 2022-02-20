package tw.com.config;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WsSessionManager
{
    /**
     * 用來記錄目前連線數
     */
    private static int onlineCount = 0;

    /**
     * 儲存連線 session的地方
     */
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

    /**
     * 發送訊息給Client端
     */
    public static void sendInfo(String key, String message) throws IOException
    {
        if (StringUtils.isNotBlank(key) && SESSION_POOL.containsKey(key))
        {
            WebSocketSession wsSession = WsSessionManager.get(key);
            wsSession.sendMessage(new TextMessage("server發送訊息給" + key + " ,內容: " + message));
        }
        else
        {
            log.error("找不到[{}]", key);
        }
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
