package nettyServer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettyServer.model.UserRequest;
import nettyServer.util.Response;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler extends SimpleChannelInboundHandler<UserRequest> {
    private final String path = "D:\\storage\\";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, UserRequest s) throws Exception {
        Path path = Paths.get(this.path + s.getUser().getId() + "\\");
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
        if (s.getRequest().getCommand().equals("/list")) {
            List<String> list = Files.walk(path).map((path1) -> path1.toString().substring(path.toString().length())).filter((path1) -> !path1.isEmpty()).collect(Collectors.toList());
            Response response = new Response();
            response.setCommand("/getlist");
            response.setFilename(list.toString());
            channelHandlerContext.writeAndFlush(response);
        }
        if (s.getRequest().getCommand().equals("/download")) {
            String filename = this.path + s.getUser().getId() + "\\" + s.getRequest().getFilename();
            Path downloadFile = Paths.get(filename);
            if (Files.exists(downloadFile)) {
                byte[] buffer = new byte[1024 * 512];
                try (RandomAccessFile accessFile = new RandomAccessFile(filename, "r")) {
                    while (true) {
                        Response response = new Response();
                        response.setFilename(s.getRequest().getFilename());
                        response.setPosition(accessFile.getFilePointer());
                        int read = accessFile.read(buffer);
                        response.setCommand("/upload");
                        if (read < buffer.length - 1) {
                            byte[] tempBuffer = new byte[read];
                            System.arraycopy(buffer, 0, tempBuffer, 0, read);
                            response.setFile(tempBuffer);
                            channelHandlerContext.writeAndFlush(response);
                            break;
                        } else {
                            response.setFile(buffer);
                            channelHandlerContext.writeAndFlush(response);
                        }
                        buffer = new byte[1024 * 512];
                    }
                }
            } else {
                Response response = new Response();
                response.setCommand("/fne");
                channelHandlerContext.writeAndFlush(response);
            }
        }
    }

}
