package com.devmountain.training.pattern;

public class HelloWorld implements Hello {

    public void sayHello(String name) {

        System.out.println("Hello " + name);
    }

    public String greeting(String name) {

        return "How are you doing? " + name;
    }

    public int addNumbers(int num1, int num2) {
        return num1 + num2;
    }

    public boolean isOldEnough(int age) {
        boolean oldFlag = false;
        if(age > 60)
            oldFlag = true;
        return oldFlag;
    }

    public String getGoodName(String name) {
        String goodName = null;
        if(name.contains("Mo") || name.contains("Nate"))
            goodName = name;
        return goodName;
    }

}
