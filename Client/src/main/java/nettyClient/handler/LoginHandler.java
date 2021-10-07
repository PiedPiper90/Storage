package nettyClient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettyClient.util.Response;

public class LoginHandler extends SimpleChannelInboundHandler<Response> {
    private boolean log = false;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response s) throws Exception {
        if (!log) {
            if (s.getCommand().equals("/log_ok")) {
                log = true;
                System.out.println("Login successful");
                channelHandlerContext.pipeline().addLast(new FileHandler());
            } else
                System.out.println("Login failed");
        } else {
            channelHandlerContext.fireChannelRead(s);
        }
    }
}
