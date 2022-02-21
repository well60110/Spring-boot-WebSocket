package tw.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import tw.com.handler.MyWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry
            .addHandler(this.webSocketHandler(), "WebSocketServer")
            .withSockJS();
    }

    @Bean
    public TextWebSocketHandler webSocketHandler()
    {
        return new MyWebSocketHandler();
    }

}
