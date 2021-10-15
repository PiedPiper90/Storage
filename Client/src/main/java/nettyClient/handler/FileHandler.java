package nettyClient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettyClient.util.Response;

import java.io.RandomAccessFile;

public class FileHandler extends SimpleChannelInboundHandler<Response> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        if (msg.getCommand().equals("/fne")) {
            System.out.println("File not exist");
        } else if (msg.getCommand().equals("/getlist")) {
            System.out.println(msg.getFilename());
        } else if (msg.getCommand().equals("/download")) {
            try (RandomAccessFile accessFile = new RandomAccessFile("D:\\client\\" + msg.getFilename(), "rw")) {
                accessFile.seek(msg.getPosition());
                accessFile.write(msg.getFile());
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
