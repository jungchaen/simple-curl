package simple_curl;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Request {
    static Socket socket=new Socket();
    String requestHeader;

    String responseStartLine;
    String responseHeader;
    String responseBody;

    static boolean checkVOption;

    public static void setCheckVOption(boolean option) {
        checkVOption = option;
    }

    public String assembleRequestMessage() {
        StringBuilder resultStringBuilder = new StringBuilder();
        resultStringBuilder.append(MessageMaker.assembleStratLine());
        requestHeader =MessageMaker.assembleHeader();
        resultStringBuilder.append(requestHeader);
        resultStringBuilder.append(MessageMaker.assembleBody());
        return resultStringBuilder.toString();
    }

    public  void sendRequest(String usageUrl) {
        try {
            URL url = new URL(usageUrl);
            socket = new Socket(url.getHost(), url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
            sendMessage(assembleRequestMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(message);
        splitReceiveMessage();
    }

    public void splitReceiveMessage() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Stream<String> lines = bufferedReader.lines();

            StringBuilder headerBuilder = new StringBuilder();
            StringBuilder bodyBuilder = new StringBuilder();

            List<String> responseList = lines.collect(Collectors.toList());

            /*start-line 추출*/
            responseStartLine = responseList.get(0);

            int flag=0;

            for (int i=1; i<responseList.size();i++) {  //
                headerBuilder.append(responseList.get(i)+"\n");
                if(responseList.get(i).length()==0) {
                    flag=i;
                    break;
                }
            }

            /*header 추출*/
            responseHeader=headerBuilder.toString().trim();

            for (int i = flag + 1; i < responseList.size(); i++) {
                bodyBuilder.append(responseList.get(i)+"\n");
            }

            /*body 추출*/
            responseBody=bodyBuilder.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printReceiveMessage() {
        System.out.println(responseStartLine);
        if (checkVOption) {
            System.out.println("==========Response Header===========");

            System.out.println(responseHeader);
            System.out.println("===================================");
        }
        System.out.println("\n"+responseBody);
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
