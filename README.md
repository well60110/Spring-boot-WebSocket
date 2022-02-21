Spring WebSocket
===

[TOC]

---

什麼是WebSocket?
---
![](https://img-blog.csdn.net/20180510225115144?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21vc2hvd2dhbWU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
> WebSocket協定是基於TCP的一種新的網絡協定。它實現了瀏覽器與伺服器全雙工(full-duplex)通信——允許伺服器主動發送信息給客戶端。

為什麼需要 WebSocket？
---
因為 HTTP 協定有一個缺陷：==只能由客戶端發起請求，無法由伺服器主動向客戶端推送信息。==
![](https://img-blog.csdn.net/20180510223926952?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21vc2hvd2dhbWU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
> 舉例來說，我們想要查詢當前的排隊情況，只能由頁面輪詢向伺服器發出請求，伺服器返回查詢結果。輪詢的效率低，非常浪費資源（因為必須不停連接，或者 HTTP 連接始終打開）。因此WebSocket 就是這樣發明的。

什麼是SockJs?
---
SockJS是一個JavaScript庫，為了應對許多瀏覽器不支持WebSocket協議的問題，設計了備選SockJs。 SockJS 是 WebSocket 技術的一種模擬。 SockJS會盡可能對應 WebSocket API，但如果WebSocket 技術不可用的話，會自動降為輪詢的方式。

三大類實作方式
---
* 使用Java提供的@ServerEndpoint註解實作
* 使用Spring提供的低層級WebSocket API實作
    * 發送和接收消息的低層級API；
    * 發送和接收消息的高級API；
    * 用來發送消息的模板；
    * 支持SockJS，用來解決瀏覽器端、伺服器以及代理不支持WebSocket的問題。
* 使用STOMP消息實作
    所謂STOMP(Simple Text Oriented Messaging Protocol)，就是在WebSocket基礎之上提供了一個基於幀的線路格式（frame-based wire format）層。它對發送簡單文本消息定義了一套規範格式（STOMP消息基於Text，當然也支持傳輸二進制數據），目前很多服務端消息隊列都已經支持STOMP，比如：RabbitMQ、 ActiveMQ等。
> 原生Java及Spring WebSocket較適合業務邏輯較複雜的情形
> Spring WebSocket又支援SockJS
> 最推薦使用 ==Spring WebSocket==實作


DEMO
---
|URL  |說明
|------|-------------------|
|http://127.0.0.1:8081/ |首頁


參考資料
===
* [Spring boot 集成 websocket 的四种方式](https://cloud.tencent.com/developer/article/1530872)
* [Spring Boot中使用WebSocket总结（一）：几种实现方式详解](https://www.zifangsky.cn/1355.html)
* [SpringBoot2.0集成WebSocket，实现后台向前端推送信息](https://zhengkai.blog.csdn.net/article/details/80275084?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-5.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-5.pc_relevant_default&utm_relevant_index=9)
* [SpringBoot - 第二十八章 | WebSocket簡介及應用](https://morosedog.gitlab.io/springboot-20190416-springboot28/)
* [websocket+sockjs+stompjs详解及实例](https://segmentfault.com/a/1190000017204277)

###### tags: `java` `spring` `spring websocket`