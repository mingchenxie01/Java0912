package Week1;

import java.util.LinkedList;
import java.util.List;

public class Day1 {
}
/**
 *1. OOP
 *      class blue print
 *          attribute
 *          method
 *      instance
 *
 *      polymorphism
 *          instance
 *              Upcasting:
 *                  provide child type object to the parent type reference variable
 *          method
 *              compile time: method overloading
 *              run time: method overriding
 *      encapsulation
 *          access modifier:
 *          private
 *          default
 *          protected
 *          public
 *      abstraction
 *          abstract class
 *              similiar to class doesn't support multi inherit
 *          interface
 *              support multi inherit
 *              before java 8:
 *                  only abtract
 *              after:
 *                  default
 *                  static
 *                  private
 *      inheritance
 *          reuse
 */

class PolyTest {

    public static void main(String[] args){
        //reference variable
        List<Integer> list = new LinkedList<>();
        LinkedList<Integer> list1 = new LinkedList<>();
        PolyTest polyTest = new PolyTest();
        polyTest.dummy(2);
        polyTest.dummy(true);
    }
    void dummy(int i){
        System.out.println("hello1");
    }
    void dummy(boolean b){
        System.out.println("hello2");
    }
}
class PolyTest1{}
//    class PolyTestChild extends PolyTest{
//        @Override
//        void dummy(int i){
//            System.out.println("hello1 in the child class");
//        }
//        public static void main(String[] args){
//            //reference var(reference -> obj)
//            PolyTest polyTest = new PolyTestChild();
//            polyTest.dummy(2);
//            PolyTestChild polyTestChild = (PolyTestChild)polyTest;
//            A.dummy(1);
//        }
//    }
//
//    interface A{
//        int a = 1;
//        //java 8 add "default" to allow us to add new methods body to an interface that are automatically available in the implementations.
//        default void dummy2(boolean b){
//            dummy(1);
//        }
//        static void dummy(int i){
//            System.out.println("method in interface A");
//        }
//    }
//
//    interface B{
//        int a = 2;
//    }
//    class InheritTest implements A, B{
//        public static void main(String[] args){
//            System.out.println(A.a);
//            A.dummy(1);
//        }
//    }
//    class InheritTest1 implements A, B{
//        public static void main(String[] args){
//            System.out.println(B.a);
//        }
//    }

abstract class AbstractTest{
    int i;
    //constructor in the abstract class is just used in its child class
    AbstractTest(){
        System.out.println("this is default constr in Abstract Test");
    }
    AbstractTest(int i){
        this.i = i;
        System.out.println("father class constractor with para");
    }
    final int a = 1;
    abstract void dummyAbstract();
    final void dummy1(){
        System.out.println("final method in abstract");
    }
}
abstract class AbstractTest1{
    abstract void dummyAbstract();
}

class Test1 extends AbstractTest{
    Test1(){
        System.out.println("a");
    }
    Test1(int i){
        super(1);
        System.out.println("child contractor with para");
    }
    void dummyAbstract(){};//必须重写父抽象class的抽象方法，否则子类也必须是抽象类

    public static void main(String[] args) {
        Test1 t1 = new Test1(3);//new中无参数就先找父类无参构造器再调用子类无参构造器
        //new中有参数，就不在自动调用父类有参构造器，直接调用有参构造器
    }
}

class EncapTest{
    public int i;
    public int getI(){
        return i;
    }
}

/**
 * 2. static
 * the member directly related to the class/interface itself
 * cannot be inherited
 */
class A{
    //static data member related to class
        static int a = 1;
        //non static data member related to object
        int b = 1;
        static void dummy(){
            System.out.println("in class A");
        }
}
class AChild extends A{
    static void dummy(){
        System.out.println("in A Child");
    }

    public static void main(String[] args) {
        A aChild = new AChild();// upcasting 问题?????? 1:22:00
        A a = new A();
        aChild.dummy();
        //A.dummy();
    }
}
class A1Child extends A{
    public static void main(String[] args) {
        A1Child a1Child = new A1Child();
        A1Child a1Child1 = new A1Child();
        System.out.println(a1Child.a);//a is static, related to class, 只能用class调用不用对象调用
        a1Child.a = 10;
        System.out.println(a1Child1.a);//so a was changed to 10
        System.out.println(a1Child.b);//b is non static, related to object
        a1Child.b = 10;
        System.out.println(a1Child.b);//so b in a1Child(this object) was changed to 10
        System.out.println(a1Child1.b);//but b in a1Child1(this object) is still 1
    }
}

/**
 * 3. java is pass by value
 * primitive type
 *          int boolean byte char short long float double
 *         object
 */
//class Test2{
//    public static void main(String[] args) {
//        final int i = 1;
//        final List<Integer> list = new LinkedList<>();
//        list.add(1);
//        System.out.println(list);
//        String s1 = "aab";
//        s1 = s1 + "a";
//        //s1 "aaba
//        //1 + 2 + 3 + 4 +  ....n
//        //n ( n + 1) / 2
//        StringBuilder res = new StringBuilder("");
//        //space comp: O(n)
//        for(int j = s1.length() - 1; j >= 0; j--){
//            res.append(s1.charAt(j));
//        }
//        System.out.println("res is: " + res);
//        System.out.println("s1 is :" + s1);
//
//        //rv(reference value(address))
//        //Integer pool(range(-128 ~ 127))在这个范围的Integer在常量池中，不需要新建对象，但是超出这个范围之后，就要新建对象，
//        //就是引用对象的地址的比较，两个对象的地址是不一样的，所以是false
//        //equals比较 如果类型不一样，直接返回false，如果类型一样，则比较里面的值
//        //在String里面重新定义了 equals，两种同类型就比较两个变量里的内容
//        //但是StringBuffer里没有重新定义equals这个方法，因此这个方法就来自Object类，
//        //而Object类中的equals方法是用来比较“地址”的，所以等于false.
//        //StringBuffer s1 = new StringBuffer("a"); StringBuffer s2 = new StringBuffer("a");
//        // s1.equals(s2)  //是false
//        Integer l1 = -128;
//        Integer l2 = -128;
//        System.out.println(l1 == l2);
//    }
//    void dummy(Person p){
//        p.i = 1;
//    }
//
//}
class Person{
    int age;
    public Person(){}
    public Person(int age){
        this.age = age;
    }
}

class Test3 {
    static void changePerson(Person p) {
        //p(addr1)
        p = new Person(40); //p(addr2)
       // p.age = 40;
    }
    static void changeValue(int i) {
        i =10;
    }
    public static void main(String[] args) {
        Person p1 = new Person(20); //construt
        //p1(addr1)
        //p1(addr1->obj(person(20)))
        System.out.println("before: "+p1.age);
        changePerson(p1); // p (addr1)
        System.out.println("after: "+p1.age);
//        int i=1;
//        System.out.println(i);
//        changeValue(i);
//        System.out.println(i);


    }

}

/**
 * final
 *      attribute/rv
 *          value cannot change
 *      method
 *          cannot override
 *      class
 *          cannot be inherited
 */

final class TestFinal{
    public static void main(String[] args) {
        final List<Integer> list = new LinkedList<>();
        //list(addr1)
        System.out.println(list);
        list.add(1);
        System.out.println(list);
//        final int i;
//        i=1;//ini
//        i=10;
        final Integer i =1;



    }
    final void dummy(){}
    final void dummy(int i){}
}

/**
 * 5. Immutable
 *      Wrapper Integer, Boolean...
 *      String
 *    final, deep copy, private
 */

//final class CustomizedImmu{
//    //...
////    private final List<Integer> list = new LinkedList<>();
//    private final List<Integer> list;
//    CustomizedImmu(List<Integer> list1) {
////        for(int i: list) {
////            this.list.add(i);
////        }
//
//        //this.list = deepCopy(list1);
//        //[innerlistrv(addr1),addr2]          [addr1,addr2] //list of list
//        //deepCopy
//        this.list = new LinkedList<>(list1);
//    }
//    List<Integer> getList(){
//        //deepCopy
//        return new LinkedList<>(this.list);
//        //return[]            inner[]
//    }
//
//
//}
//
////class MalaciousImmu extends CustomizedImmu{
////    //...
////}
//
//class Test5 {
//    public static void main(String[] args) {
////        CustomizedImmu immu = new MalaciousImmu();
//        List<Integer> list = new LinkedList<>();
//        list.add(1);
//        list.add(2);
//        CustomizedImmu customizedImmu = new CustomizedImmu(list);
//
//        System.out.println(customizedImmu.getList());
//        list.add(3);
//        customizedImmu.getList().add(3);
//        System.out.println(customizedImmu.getList());
//
//
//
//
//
//    }
//}


final class CustomizedImmu{
    //...
//    private final List<Integer> list = new LinkedList<>();

    //[rv1(addr1), rv2(addr2)]  [rv3(addr1.add(100)), rv4(addr2)]
    private final List<List> list;
    CustomizedImmu(List<List> list1) {
//        for(int i: list) {
//            this.list.add(i);
//        }

        //this.list = deepCopy(list1);
        //[innerlistrv(addr1),addr2]          [addr1,addr2] //list of list
        //deepCopy
        this.list = new LinkedList<>(list1);
        for(int i=0;i<list1.size();i++) {
            //shallow
            this.list.set(i, new LinkedList(list1.get(i)));
        }
    }
    List<List> getList(){
        //deepCopy
        return new LinkedList<>(this.list);
        //return[]            inner[]
    }


}

//class MalaciousImmu extends CustomizedImmu{
//    //...
//}

class Test5 {
    public static void main(String[] args) {
//        CustomizedImmu immu = new MalaciousImmu();
        List<List> list = new LinkedList<>(); //list of list
        List<Integer> inner1 = new LinkedList<>();
        inner1.add(1);
        list.add(inner1);
        List<Integer> inner2 = new LinkedList<>();
        inner2.add(2);
        list.add(inner2);
        CustomizedImmu customizedImmu = new CustomizedImmu(list);

        System.out.println(customizedImmu.getList());
        list.get(0).add(100);
        System.out.println(customizedImmu.getList());





    }
}