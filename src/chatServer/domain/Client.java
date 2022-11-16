package chatServer.domain;

import chatServer.ServerMain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Pattern;

public class Client {
    public Socket socket;

    /*
     * Socket 을 활용한 TCP 통신
     * */
    public Client(Socket socket) {
        this.socket = socket;
        receive();
    }

    /*
     * 클라이언트로부터 메시지를 받는 메소드
     * */
    public void receive(){
        StringBuilder stringForLog = new StringBuilder();
        StringBuilder stringForData = new StringBuilder();
        /*
         * 메세지를 받는 쓰레드 구현
         * */
        Runnable thread = new Runnable() {
            @Override
            public void run() {

                try {
                    while (true){
                        stringForLog.setLength(0);
                        stringForData.setLength(0);
                        /*
                         * getInputStream() 메소드로 socket 의 데이터를 InputStream 타입으로 변환
                         * */
                        InputStream input = socket.getInputStream();
                        /*
                         * Stream 을 담을 byte 배열
                         * */
                        byte[] buffer = new byte[512];

                        int length = input.read(buffer);
                        if (length == -1) throw new IOException();
                        stringForLog.append("메세지 수신 성공");
                        stringForLog.append(socket.getRemoteSocketAddress());
                        stringForLog.append(Thread.currentThread());

                        System.out.println(stringForLog);

                        String msg = new String(buffer,0,length,"UTF-8");

                        if (msg.contains("문제2")){
                            String data = msg.replace("문제2","");
                            data = data.replace("\n","");
                            if (!Pattern.matches("^[a-zA-Z]*$",data) || data.length() > 4){
                                send("잘못된 문자열을 입력하였습니다. (4자리 이하 영대소문자 가능)\n");
                            }else{
                                long beforeTime = System.currentTimeMillis();
                                int result = Singleton.getInstance().wordsMapBasket.findMatchingNumber(data);
                                long afterTime = System.currentTimeMillis();
                                long diffTime = (afterTime - beforeTime);

                                stringForData.append("============================\n");
                                stringForData.append("- 입력된 값\n");
                                stringForData.append(data);
                                stringForData.append("\n- 추출된 값\n");
                                stringForData.append(result);
                                stringForData.append("\n- 소요시간\n ");
                                stringForData.append(diffTime);
                                stringForData.append(" ms \n");
                                send(String.valueOf(stringForData));
                            }
                        }

                        if (msg.contains("문제3")){
                            String data = msg.replace("문제3","");
                            data = data.replace("\n","");
                            if (!Pattern.matches("^[0-9]*$",data)){
                                send("잘못된 숫자를 입력하였습니다. (700만 이하 숫자 가능)\n");
                            }else{
                                int intData = Integer.parseInt(data);
                                if (intData > MaxDataSize.MAX_DATA_SIZE.getValue()){
                                    send("잘못된 숫자를 입력하였습니다. (700만 이하 숫자 가능)\n");
                                }
                                long beforeTime = System.currentTimeMillis();
                                String result = Singleton.getInstance().wordsListBasket.findMatchingWords(intData);
                                long afterTime = System.currentTimeMillis();
                                long diffTime = (afterTime - beforeTime);

                                stringForData.append("============================\n");
                                stringForData.append("- 입력된 값\n");
                                stringForData.append(data);
                                stringForData.append("\n- 추출된 결과\n");
                                stringForData.append(result);
                                stringForData.append("\n- 소요시간\n");
                                stringForData.append(diffTime);
                                stringForData.append(" ms \n");
                                send(String.valueOf(stringForData));
                            }
                        }

                        if (msg.contains("문제4")){
                            float beforeTime = System.currentTimeMillis();
                            float afterTime = System.currentTimeMillis();
                            float diffTime = (afterTime - beforeTime)/1000;
                        }
                    }
                }catch (Exception e){
                    try{
                        System.out.println("메세지 수신오류 " + socket.getRemoteSocketAddress() + " : " + Thread.currentThread());
                        ServerMain.clients.remove(Client.this);
                        socket.close();
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                }
            }
        };
        ServerMain.threadPool.submit(thread);
    }

    /*
     * 클라이언트로 메시지를 보내는 메소드
     * */
    public void send(String msg){
        /*
         * 메세지를 전송하는 쓰레드
         * */
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    /*
                     * getOutputSteam() 메소드로 Socket 의 데이터를 전송하기위한 OutputStream 객체로 반환
                     * */
                    OutputStream output = socket.getOutputStream();
                    /*
                     * 전송할 메세지를 byte 형 배열로 변환
                     * */
                    byte[] buffer = msg.getBytes("UTF-8");
                    output.write(buffer);
                    output.flush();
                }catch (Exception e){
                    try{
                        System.out.println("메세지 송신 오류 " + socket.getRemoteSocketAddress() + " : " + Thread.currentThread());
                        ServerMain.clients.remove(Client.this);
                        socket.close();
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                }

            }
        };
        ServerMain.threadPool.submit(thread);
    }
}
