package com.tangdou.panda.utils;

import java.util.concurrent.ThreadFactory;

/**
 * Created by david on 2017/8/18.
 */
public class MyThreadFactoryBuilder {

    public static ThreadFactory buildThreadFactory(String preName) {
        return new MyThreadFactory(preName);
    }

}
