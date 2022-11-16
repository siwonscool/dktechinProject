package chatClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Problem2Client extends Application {
    Socket socket;
    TextArea textArea;

    //문제2 Client 를 식별하기위한 문자열
    String userName = Clients.문제2.name();

    public void startClient(String ip, int port){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket(ip,port);
                    receive();
                }catch (Exception e){
                    if (!socket.isClosed()){
                        stopClient();
                        System.out.println("서버접속 실패");
                        Platform.exit();
                    }
                }
            }
        };

        thread.start();
    }

    public void stopClient(){
        try {
            if (socket != null && !socket.isClosed()){
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * 서버로부터 메세지를 받는 메소드
     * */
    public void receive(){
        while (true){
            try {
                InputStream input = socket.getInputStream();
                byte[] buffer = new byte[256];
                int length = input.read(buffer);
                if (length == -1) throw new IOException();
                String msg = new String(buffer,0,length,"UTF-8");
                Platform.runLater(() -> {
                    textArea.appendText(msg);
                });

            }catch (Exception e){
                stopClient();
                break;
            }
        }
    }


    /*
     * 서버로 메세지를 전송하는 메소드
     * */
    public void send(String msg){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    OutputStream output = socket.getOutputStream();
                    byte[] buffer = msg.getBytes("UTF-8");
                    output.write(buffer);
                    output.flush();
                }catch (Exception e){
                    stopClient();
                }
            }
        };
        thread.start();
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        String ip = "127.0.0.1";
        int port = 9000;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));

        textArea = new TextArea();
        textArea.setEditable(false);
        root.setCenter(textArea);

        TextField input = new TextField();
        input.setPrefWidth(Double.MAX_VALUE);
        input.setDisable(true);

        input.setOnAction(event -> {
            send(userName + input.getText() + "\n");
            input.setText("");
            input.requestFocus();
        });

        Button sendButton = new Button("보내기");
        sendButton.setDisable(true);

        sendButton.setOnAction(event -> {
            send(userName + input.getText() + "\n");
            input.setText("");
            input.requestFocus();
        });

        Button connectionButton = new Button("접속하기");
        connectionButton.setOnAction(event -> {
            if (connectionButton.getText().equals("접속하기")){
                startClient(ip,port);
                Platform.runLater(() -> {
                    textArea.appendText("서버 접속\n");
                });
                connectionButton.setText("종료하기");
                input.setDisable(false);
                sendButton.setDisable(false);
                input.requestFocus();
            }else {
                stopClient();
                Platform.runLater(() -> {
                    textArea.appendText("서버 퇴장\n");
                });
                connectionButton.setText("접속하기");
                input.setDisable(true);
                sendButton.setDisable(true);
            }
        });

        BorderPane pane = new BorderPane();

        pane.setLeft(connectionButton);
        pane.setCenter(input);
        pane.setRight(sendButton);

        root.setBottom(pane);
        Scene scene = new Scene(root,400,400);
        primaryStage.setTitle("문제 2 클라이언트");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> stopClient());
        primaryStage.show();

        connectionButton.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
