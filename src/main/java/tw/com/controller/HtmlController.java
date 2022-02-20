package tw.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HtmlController
{

    @RequestMapping("/")
    public String webSocketClientPage()
    {
        return "webSocketClientPage";
    }
}
