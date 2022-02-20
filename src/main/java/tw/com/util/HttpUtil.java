package tw.com.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class HttpUtil
{
    public static Map<String, String> queryStrToMap(String queryStr)
    {

        if (StringUtils.isBlank(queryStr))
            return null;

        Map<String, String> result = new HashMap<>();
        for (String param : StringUtils.split(queryStr, "&"))
        {
            String[] entry = StringUtils.split(param, "=");
            if (entry.length > 1)
            {
                result.put(entry[0], entry[1]);
            } else
            {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
