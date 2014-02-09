package br.com.ingenieux.gae;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private AppEngineSQSSender sender;

    @Before
    public void before() throws Exception {
        sender = new AppEngineSQSSender("yourkey", "yoursecret", "us-east-1");
    }

    @Test
    public void testSomething() throws Exception {
        String result = sender.send("235368163414/whatever", "Hello, World!");

        System.out.println(result);
    }
}
