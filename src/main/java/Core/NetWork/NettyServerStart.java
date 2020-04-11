package Core.NetWork;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


public class NettyServerStart {
    private int port;

    public NettyServerStart(int port){
        this.port = port;
    }
    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(50);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("LineBasedDecoder", new LineBasedFrameDecoder(2048,true,false));
                            ch.pipeline().addLast(new StringDecoder());



                            ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new IdleStateTrigger());


                            ch.pipeline().addLast(new NettyServerHandler());
                        };
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
