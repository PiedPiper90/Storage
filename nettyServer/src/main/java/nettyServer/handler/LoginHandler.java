package nettyServer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettyServer.dao.UserDao;
import nettyServer.model.User;
import nettyServer.util.Request;
import nettyServer.util.Response;

import java.util.Optional;

public class LoginHandler extends SimpleChannelInboundHandler<Request> {
    UserDao dao;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request s) throws Exception {
        Response response = new Response();
        if (s.getCommand().startsWith("/log")) {
            String login = s.getCommand().split(" ", 3)[1];
            String pass = s.getCommand().split(" ", 3)[2];
            Optional<User> user = dao.login(login, pass);
            if (user.isPresent()) {
                System.out.println("Login Successful");
                response.setCommand("/log_ok");
                channelHandlerContext.channel().writeAndFlush(response);
                channelHandlerContext.channel().pipeline().addLast("main", new MainHandler(user.get()));
                channelHandlerContext.channel().pipeline().addLast("file", new FileHandler());
                channelHandlerContext.pipeline().remove(this);
            } else {
                throw new Exception();
            }
        }
    }

    public LoginHandler(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Response response = new Response();
        System.out.println("Login Failed");
        response.setCommand("/log_fail");
        ctx.channel().writeAndFlush(response);
    }
}
