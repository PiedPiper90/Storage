package nettyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import nettyServer.dao.UserDaoImpl;
import nettyServer.handler.JsonDecoder;
import nettyServer.handler.JsonEncoder;
import nettyServer.handler.LoginHandler;

public class Server {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.start();
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1); // threadpool для соедениней
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(); // threadpool для сообщений
        try {
            ServerBootstrap server = new ServerBootstrap(); // абстракция запускающая серверное приложение
            server
                    .group(bossGroup, workerGroup) // устанавливаем группы
                    .channel(NioServerSocketChannel.class) // инициалирзируем каналы
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
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
                                    new LoginHandler(new UserDaoImpl()));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // препятствует закрытию соединения
            ChannelFuture sync = server.bind(9000).sync();
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
