package com.devmountain.training.pattern;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class HelloWorldTest {
    private Logger logger = LoggerFactory.getLogger(HelloWorldTest.class);

    private HelloWorld helloworld;

    @BeforeClass
    public static void setupOnce() {
    }

    @AfterClass
    public static void teardownOnce() {
    }

    @Before
    public void setup() {
        helloworld = new HelloWorld();
    }

    @After
    public void teardown() {
        helloworld = null;
    }

    @Test
    public void greetingTest() {
        String greetingText = helloworld.greeting("Mike");
        assertTrue("text should include Mike", greetingText.contains("Mike"));
        assertFalse("text should not include ABC", greetingText.contains("ABC"));
        assertNotNull(greetingText);
    }

//    @Test
    @Ignore
    public void addNumberTest() {

    }

    @Test
    public void oldEnoughTest() {

    }

    @Test
    public void getGoodNameTest() {

    }

}
