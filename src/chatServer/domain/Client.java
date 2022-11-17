package chatServer.domain;

import chatClient.enumeration.ProblemType;
import chatServer.ServerApplication;

import chatServer.enumeration.MaxDataSize;
import chatServer.vo.Answer4Vo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class Client {

    public Socket socket;

    //Socket 을 활용한 TCP 통신
    public Client(Socket socket) {
        this.socket = socket;
        receive();
    }

    //클라이언트로부터 메시지를 받는 메소드
    public void receive() {
        StringBuilder stringForLog = new StringBuilder();
        StringBuilder stringForData = new StringBuilder();

        //메세지를 받는 쓰레드 구현
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        stringForLog.setLength(0);
                        stringForData.setLength(0);

                        //getInputStream() 메소드로 socket 의 데이터를 InputStream 타입으로 변환
                        InputStream input = socket.getInputStream();

                        //Stream 을 담을 byte 배열
                        byte[] buffer = new byte[512];

                        int length = input.read(buffer);
                        if (length == -1) {
                            throw new IOException();
                        }
                        stringForLog.append("메세지 수신 성공");
                        stringForLog.append(socket.getRemoteSocketAddress());
                        stringForLog.append(Thread.currentThread());

                        System.out.println(stringForLog);

                        String msg = new String(buffer, 0, length, "UTF-8");

                        if (msg.contains(ProblemType.문제2.name())) {
                            solveProblem2(msg, stringForData);
                        }

                        if (msg.contains(ProblemType.문제3.name())) {
                            solveProblem3(msg, stringForData);
                        }

                        if (msg.contains(ProblemType.문제4.name())) {
                            solveProblem4(msg, stringForData);
                        }
                    }
                } catch (Exception e) {
                    try {
                        stringForLog.append("메세지 수신오류 ");
                        stringForLog.append(socket.getRemoteSocketAddress());
                        stringForLog.append(Thread.currentThread());
                        System.out.println(stringForLog);
                        ServerApplication.clients.remove(Client.this);
                        socket.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        };
        Singleton.getInstance().threadPool.submit(thread);
    }

    //클라이언트로 메시지를 보내는 메소드
    public void send(String msg) {

        //메세지를 전송하는 쓰레드
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {

                    //getOutputSteam() 메소드로 Socket 의 데이터를 전송하기위한 OutputStream 객체로 반환
                    OutputStream output = socket.getOutputStream();

                    //전송할 메세지를 byte 형 배열로 변환
                    byte[] buffer = msg.getBytes("UTF-8");
                    output.write(buffer);
                    output.flush();
                } catch (Exception e) {
                    try {
                        System.out.println("메세지 송신 오류 " + socket.getRemoteSocketAddress() + " : "
                            + Thread.currentThread());
                        ServerApplication.clients.remove(Client.this);
                        socket.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

            }
        };
        Singleton.getInstance().threadPool.submit(thread);
    }

    private void solveProblem2(String msg, StringBuilder stringForData) {
        String data = msg.replace(ProblemType.문제2.name(), "");
        data = data.replace("\n", "");
        if (!Pattern.matches("^[a-zA-Z]*$", data) || data.length() > 4 || data.equals("")) {
            send("============================\n잘못된 문자열을 입력하였습니다. (4자리 이하 영대소문자 가능)\n");
        } else {
            long beforeTime = System.currentTimeMillis();
            int result = Singleton.getInstance().wordsMapBasket.findMatchingNumber(data);
            long afterTime = System.currentTimeMillis();
            long diffTime = (afterTime - beforeTime);

            stringForData.append("============================\n");
            if (result == -1) {
                stringForData.append("문자열에 매칭된 숫자가 없습니다.\n");
            } else {
                stringForData.append("- 입력된 값\n");
                stringForData.append(data);
                stringForData.append("\n\n- 추출된 값\n");
                stringForData.append(result);
                stringForData.append("\n\n- 소요시간\n ");
                stringForData.append(diffTime);
                stringForData.append(" ms \n");
            }
            send(String.valueOf(stringForData));
        }
    }

    private void solveProblem3(String msg, StringBuilder stringForData) {
        String data = msg.replace(ProblemType.문제3.name(), "");
        data = data.replace("\n", "");
        if (!Pattern.matches("^[0-9]*$", data) || data.equals("")) {
            send("============================\n잘못된 숫자를 입력하였습니다. (700만 이하 숫자 가능)\n");
        } else {
            int intData = Integer.parseInt(data);
            if (intData > MaxDataSize.MAX_DATA_SIZE.getValue()) {
                send("============================\n잘못된 숫자를 입력하였습니다. (700만 이하 숫자 가능)\n");
            }
            long beforeTime = System.currentTimeMillis();
            String result = Singleton.getInstance().wordsListBasket.findMatchingWords(intData);
            long afterTime = System.currentTimeMillis();
            long diffTime = (afterTime - beforeTime);

            stringForData.append("============================\n");
            if (result.equals("")) {
                stringForData.append("숫자에 매칭된 문자열이 없습니다.\n");
            } else {
                stringForData.append("- 입력된 값\n");
                stringForData.append(data);
                stringForData.append("\n\n- 추출된 결과\n");
                stringForData.append(result);
                stringForData.append("\n\n- 소요시간\n");
                stringForData.append(diffTime);
                stringForData.append(" ms \n");
            }
            send(String.valueOf(stringForData));
        }
    }

    private void solveProblem4(String msg, StringBuilder stringForData) {
        String data = msg.replace(ProblemType.문제4.name(), "");
        data = data.replace("\n", "");
        if (!Pattern.matches("^[a-zA-Z]*$", data) || data.equals("")) {
            send("============================\n잘못된 문자열을 입력하였습니다. 영대소문자 가능)\n");
        } else {
            long beforeTime = System.currentTimeMillis();
            Answer4Vo answer4Vo = Singleton.getInstance().wordsMapBasket.findMatchingPrefix(data);
            long afterTime = System.currentTimeMillis();
            long diffTime = (afterTime - beforeTime);

            stringForData.append("============================\n");
            stringForData.append("- 입력된 값\n");
            stringForData.append(data);
            stringForData.append("\n\n- 추출된 결과\n");
            for (Entry<String, Integer> element : answer4Vo.getAnswer4Map().entrySet()) {
                stringForData.append(element.getKey());
                stringForData.append(" -> ");
                stringForData.append(element.getValue());
                stringForData.append("\n");
            }

            stringForData.append("\n");
            for (int i = 1; i < answer4Vo.getAnswer4Array().length; i++) {
                if (answer4Vo.getAnswer4Array()[i] > 0) {
                    stringForData.append(i);
                    stringForData.append(" : ");
                    stringForData.append(answer4Vo.getAnswer4Array()[i]);
                    stringForData.append("개");
                    stringForData.append("\n");
                }
            }
            stringForData.append("\n- 소요시간\n");
            stringForData.append(diffTime);
            stringForData.append(" ms \n");
            send(String.valueOf(stringForData));
        }
    }
}
