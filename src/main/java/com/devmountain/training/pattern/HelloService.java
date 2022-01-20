package com.devmountain.training.pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    private Hello hello;

    public void Foo(String str, HelloWorldSingleton singleton) {
        //hello.
        hello.addNumbers(10, 30);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder strBuilder = new StringBuilder();

     //   strBuilder.append("aaa").insert(0, 1).delete()
//Builder

    }

    public String Boo(int num, String name) {
        //
        hello.addNumbers(10, 30);
        return "dummy";
    }
}
