package github.cdz;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * RpcServer
 *
 * @author chendezhi
 * @date 2020/9/22 15:26
 * @since 1.0.0
 * <p>
 * server端，需要注册实现得服务，
 * 然后可以使用反射调用
 */
@Slf4j
public class RpcServer {

    private static ExecutorService executorService;

    {
        //创建线程池
        int core = 3;
        int max = 100;
        int keepAliveTime = 1;

        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        executorService = new ThreadPoolExecutor(core, max, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }


    public void register(Object service, int port) {
        log.info("server启动");
        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("注册service：{}",service.getClass().getName());
                executorService.execute(new ClientMessageHandlerThread(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
