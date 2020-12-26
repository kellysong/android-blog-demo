package com.sjl.jni;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename JniDemo.java
 * @time 2018/5/3 16:44
 * @copyright(C) 2018 song
 */
public class JniDemo {
    static {
        System.loadLibrary("JniDemo");
    }
    //com.sjl.jni.JniDemo
    public static native String sayHelloWorld();

    public static native int add(int a, int b);
}
