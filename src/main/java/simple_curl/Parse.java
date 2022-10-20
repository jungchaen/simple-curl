package simple_curl;

import org.apache.commons.cli.*;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Scanner;

public class Parse {
    private static final Options options = new Options();
    CommandLineParser commandLineParser = new DefaultParser();
    MessageMaker messageMaker=new MessageMaker();
    String[] splitUsage;
    private String url;
    String method=null;

    public String getUrl() {
        return url;
    }

    public void addtionOptions() {
        options.addOption("v", false,"verbose, 요청, 응답 헤더를 출력합니다.");
        options.addOption("H", true,"임의의 헤더를 서버로 전송합니다.");
        options.addOption("d", true,"POST, PUT 등에 데이터를 전송합니다.");
        options.addOption("X", true, "사용할 method 를 지정합니다. 지정되지 않은 경우 기본값은 GET 입니다.");
        options.addOption("L", false,"서버의 응답이 30x 계열이면 다음 응답을 따라 갑니다.");
        options.addOption("F", true, "multipart/form-data 를 구성하여 전송합니다. content 부분에 @filename 을 사용할 수 있습니다.");
    }
    public void split() {
        Scanner scanner = new Scanner(System.in);
        String usage = scanner.nextLine();

         splitUsage= usage.split(" ");
    }

    public void findUrl() {
        int usageLength=splitUsage.length;
        url = splitUsage[usageLength - 1];
    }

    public void parsing() {
        try {
            messageMaker.findHost(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            CommandLine commandLine = commandLineParser.parse(options, splitUsage);
            List<String> valueList=commandLine.getArgList();

            messageMaker.setHost();

            if(commandLine.hasOption("H")){
                List<Integer> hIndex = messageMaker.findHOption(splitUsage);
                messageMaker.additionalHeader(splitUsage,hIndex);
            }

            if(commandLine.hasOption("X")){
                method = commandLine.getOptionValue("X");
                messageMaker.decideMethod(method);
            } else if (commandLine.hasOption("L")) {
                messageMaker.redirect(url);
            } else {
                messageMaker.defaultDecideMethod();
            }

            if(commandLine.hasOption("d")){
                messageMaker.additionalBody(method,valueList);
            }

            if(commandLine.hasOption("v")){
                Request.setCheckVOption(true);
                messageMaker.vervose();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
