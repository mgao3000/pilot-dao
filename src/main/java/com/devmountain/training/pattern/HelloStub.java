package com.devmountain.training.pattern;

public class HelloStub implements Hello{


    @Override
    public void sayHello(String name) {

    }

    @Override
    public String greeting(String name) {
        return null;
    }

    @Override
    public int addNumbers(int num1, int num2) {
        return 10;
    }

    @Override
    public boolean isOldEnough(int age) {
        return true;
    }

    @Override
    public String getGoodName(String name) {
        return "Good";
    }
}
