package javacloud.client.tests;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javacloud.shared.request.RequestLs;
import javacloud.shared.response.Response;
import javacloud.shared.response.ResponseLs;

import java.io.IOException;
import java.net.Socket;

public class TestLs {
    private static ObjectEncoderOutputStream os;
    private static ObjectDecoderInputStream is;

    public static void main(String[] args) throws IOException {
        init();

        os.writeObject(new RequestLs("./"));
    }

    private static void init() {
        try {
            Socket socket = new Socket(Test.HOST, Test.PORT);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        Response response = (Response) is.readObject();
                        System.out.println(((ResponseLs)response).getFiles());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

