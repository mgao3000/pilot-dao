package com.devmountain.training.pattern;

public class HelloWorldSingleton {

    //No.1 thing to do, private constructor
    private HelloWorldSingleton() {

    }

    // No.2 create a private static class variable to itself
    private static HelloWorldSingleton singleton = new HelloWorldSingleton();

    //No3. create a public static method to return HelloWorldSingleton instance
    public static HelloWorldSingleton getInstance() {
//        if(singleton == null)
//            singleton = new HelloWorldSingleton();
        return singleton;
    }



    public void sayHello(String name) {

        System.out.println("Hello " + name);
    }

    public String greeting(String name) {

        return "How are you doing? " + name;
    }

}
