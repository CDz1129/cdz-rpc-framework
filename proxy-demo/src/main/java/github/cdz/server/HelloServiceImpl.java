package github.cdz.server;

import github.cdz.HelloService;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: CDz
 * Create: 2020-09-21 23:47
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String s) {
        System.out.println("server 方法.....");
        return "say:"+s;
    }
}
