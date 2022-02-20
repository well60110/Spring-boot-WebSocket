package tw.com.handler;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;
import tw.com.config.WsSessionManager;
import tw.com.util.HttpUtil;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler
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
        log.info("連線建立开始");

        String key = this.getKey(session);

        // 連線建立成功
        if (StringUtils.isNoneBlank(key))
        {
            WsSessionManager.add(key, session);
            WsSessionManager.sendInfo(key, "websocket success");
        }
    }

    /**
     * 接收訊息事件
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

    private String getKey(WebSocketSession session)
    {
        // 取得請求參數
        log.info("http uri:{}", session.getUri().getQuery());
        Map<String, String> paramMap = HttpUtil.queryStrToMap(session.getUri().getQuery());
        String userId = MapUtils.getString(paramMap, "userId", "");
        return userId;
    }
}
