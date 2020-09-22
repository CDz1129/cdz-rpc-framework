package github.cdz;

import lombok.extern.slf4j.Slf4j;

/**
 * HelloServiceImpl
 *
 * @author chendezhi
 * @date 2020/9/22 18:29
 * @since 1.0.0
 */
@Slf4j
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(Hello hello) {
        return hello.toString();
    }
}
