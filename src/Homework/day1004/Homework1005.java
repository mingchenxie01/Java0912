package Homework.day1004;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Homework1005 {
}
 /**   a. create customized annotation @Component , @Inject
 *      b. @Component
 *         class A {}
 *         your application should load dynamic proxy instance of A into concurrentHashMap
         *      c.  @Component
 *          class B {
 *              @Inject
 *              private A a
 *         }
 *         inject the proxy instance A into field a from concurrent hashmap
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface Component{

}
interface AInterface{

}
@Component
class A implements AInterface{
}
@Component
class B{
    @Inject
    private A a;
    public B(A a) {
        this.a = a;
    }
}
class DynamicProxy{
    public static void main(String[] args) {
        AInterface proxy = (AInterface) Proxy.newProxyInstance(
                DynamicProxy.class.getClassLoader(),
                new Class[]{AInterface.class},
                new AInterfaceInvocationHandler(new A())
        );
    }
}
class AInterfaceInvocationHandler implements InvocationHandler{
    private final AInterface A;
    AInterfaceInvocationHandler(AInterface a) {
        A = a;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}