package nettyServer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettyServer.model.User;
import nettyServer.model.UserRequest;
import nettyServer.util.Request;

public class MainHandler extends SimpleChannelInboundHandler<Request> {
    private User user;

    public User getUser() {
        return user;
    }

    public MainHandler(User user) {
        this.user = user;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request s) throws Exception {
        UserRequest com = new UserRequest(user, s);
        channelHandlerContext.fireChannelRead(com);
    }


}
