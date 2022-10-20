package simple_curl;

public class Main {


    public static void main(String[] args) {
        Request request = new Request();

        Parse parse = new Parse();
        parse.addtionOptions();
        parse.split();
        parse.findUrl();
        parse.parsing();

        /*서버에게 요청 메시지 전송*/
        request.sendRequest(parse.getUrl());
        request.printReceiveMessage();
        request.closeSocket();
    }
}
