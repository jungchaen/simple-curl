package simple_curl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MessageMaker {
    private static StringBuilder startLineStringBuilder = new StringBuilder();
    private static StringBuilder headerStringBuilder = new StringBuilder();
    private static StringBuilder bodyStringBuilder = new StringBuilder();

    private static String host;
    private final String httpVersion = "HTTP/1.0";

    public void setHost() {
        headerStringBuilder.append("Host: " + host + "\r\n");
    }

    public static String assembleStratLine() {
        return startLineStringBuilder.toString();
    }

    public static String assembleHeader() {
        return headerStringBuilder.toString();
    }

    public static String assembleBody() {
        return bodyStringBuilder.toString();
    }

    public String findHost(String url) throws MalformedURLException {
        if (!url.startsWith("http") && !url.startsWith("https")) {
            url = "http://" + url;
        }
        URL netUrl = new URL(url);
        host = netUrl.getHost();
        if (host.startsWith("www")) {
            host = host.substring("www".length() + 1);
        }
        return host;
    }

    public void decideMethod(String method) {
        startLineStringBuilder.append(method.toUpperCase() + " /" + method.toLowerCase() + " " + httpVersion + "\r\n");
    }

    public void defaultDecideMethod() {
        startLineStringBuilder.append("GET /get " + httpVersion + "\r\n");
    }

    public void redirect(String url) {
        String[] splitUrl = url.split("/");
        int flag = 0;
        for (int i = 0; i < splitUrl.length; i++) {
            if (splitUrl[i].equals("status")) {
                flag = i;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = flag; i < splitUrl.length; i++) {
            stringBuilder.append(splitUrl[i] + "/");
        }
        String result = stringBuilder.toString();

        String str = "GET /" + result.substring(0, result.length() - 1) + " " + httpVersion + "\r\n";
//        System.out.println(str);
        startLineStringBuilder.append(str);
    }

    public void vervose() {
        System.out.println("==========Request Header===========");
        System.out.println(assembleHeader().trim());
        System.out.println("===================================");
    }

    public List<Integer> findHOption(String[] splitUsage) {
        List<Integer> hIndexList = new ArrayList<>();
        for (int i = 0; i < splitUsage.length; i++) {
            if (splitUsage[i].equals("-H")) {
                hIndexList.add(i);
            }
        }
        return hIndexList;
    }

    public void additionalHeader(String[] splitUsage, List<Integer> hInex) {
        for (Integer index : hInex) {
            String name = splitUsage[index + 1];
            String value = splitUsage[index + 2];
            headerStringBuilder.append(name.substring(1, name.length()) + " " + value.substring(0, value.length() - 1) + "\n");
        }
    }

    public void additionalBody(String method, List<String> valueList) {
        int flag = 0;
        if (method.equals("POST") || method.equals("PUT")) {
            for (int i = 0; i < valueList.size(); i++) {
                if (valueList.get(i).contains("}")) {
                    flag = i;
                }
            }

            bodyStringBuilder.append("{ ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < flag; i++) {
                bodyStringBuilder.append(valueList.get(i));
                stringBuilder.append(" " + valueList.get(i));
            }
            bodyStringBuilder.append(" }");

            headerStringBuilder.append("Content-Length: " + stringBuilder.toString().length() + "\n\n");
        }
    }
}
