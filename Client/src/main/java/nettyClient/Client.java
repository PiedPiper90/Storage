package nettyClient;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import nettyClient.handler.FileHandler;
import nettyClient.handler.JsonDecoder;
import nettyClient.handler.JsonEncoder;
import nettyClient.handler.LoginHandler;
import nettyClient.util.Request;

import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        new Client().start();

    }

    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(); // обертка над threadpool
        try {
            Bootstrap client = new Bootstrap(); //запускает клиент-серверное взаимодействие на стороне клиента
            client.group(group)
                    .channel(NioSocketChannel.class)   // создаем канал для отправки\приема
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024,
                                            0,
                                            8,
                                            0,
                                            8),
                                    new LengthFieldPrepender(8),
                                    new ByteArrayDecoder(),
                                    new ByteArrayEncoder(),
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new LoginHandler());
                        }
                    });
            ChannelFuture future = client.connect("localhost", 9000).sync();

            System.out.println("Client started");
            System.out.println("Please authorize");
            while (true) {
                String cmd = scanner.nextLine();
                if (cmd.equals("/exit"))
                    break;
                Request request = new Request();
                if (cmd.startsWith("/download")) {
                    request.setFilename(cmd.split(" ", 2)[1]);
                    request.setCommand("/download");
                } else {
                    request.setCommand(cmd);
                }
                future.channel().writeAndFlush(request);
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
