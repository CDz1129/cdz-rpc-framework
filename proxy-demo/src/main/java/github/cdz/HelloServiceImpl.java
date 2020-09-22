package github.cdz;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: CDz
 * Create: 2020-09-21 23:47
 **/
public class HelloServiceImpl implements HelloService {
    public String hello(String s) {
        System.out.println("原始方法.....");
        return "say:"+s;
    }
}
