package com.devmountain.training.pattern;

public class Client {

    public static void main(String[] args) {

        HelloWorldSingleton helloEric = HelloWorldSingleton.getInstance();

        helloEric.sayHello("Eric");


        HelloWorldSingleton helloJennifer = HelloWorldSingleton.getInstance();

        helloJennifer.sayHello("Jennifer");

        HelloWorldSingleton helloLaura = HelloWorldSingleton.getInstance();

        helloLaura.sayHello("laura");

        if(helloEric == helloJennifer)
            System.out.println(("helloeric is the same a helloJennifer"));

    }
}
