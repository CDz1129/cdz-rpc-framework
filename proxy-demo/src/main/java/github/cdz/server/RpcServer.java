package github.cdz.server;

import github.cdz.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket;
            while ((socket = server.accept()) != null) {
                Socket finalSocket = socket;
                executorService.execute(() -> {
                    try (ObjectInputStream inputStream = new ObjectInputStream(finalSocket.getInputStream());
                         ObjectOutputStream outputStream = new ObjectOutputStream(finalSocket.getOutputStream())){
                        RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();
                        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                        Object ret = method.invoke(service, rpcRequest.getParameters());
                        outputStream.writeObject(ret);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
