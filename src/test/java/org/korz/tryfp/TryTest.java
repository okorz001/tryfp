package org.korz.tryfp;

import org.junit.Assert;
import org.junit.Test;

public class TryTest {
    // something that could never be thrown by anything else
    private static class MyException extends RuntimeException { }

    @Test
    public void ofValue() throws Exception {
        Assert.assertEquals("foo", Try.ofValue("foo").getValue());
    }

    @Test(expected = MyException.class)
    public void ofError() throws Exception {
        Try.ofError(new MyException()).getValue();
    }

    @Test
    public void map() throws Exception {
        String result = Try.ofValue("foo")
            .map(String::toUpperCase)
            .getValue();
        Assert.assertEquals("FOO", result);
    }

    @Test(expected = MyException.class)
    public void mapThrows() throws Exception {
        Try.ofValue("foo")
            .map(unused -> { throw new MyException(); })
            .getValue();
    }

    @Test(expected = MyException.class)
    public void mapAbort() throws Exception {
        Try.ofError(new MyException())
            .map(unused -> { Assert.fail("This function should never run"); return null; })
            .getValue();
    }

    @Test
    public void flatMap() throws Exception {
        String result = Try.ofValue("foo")
            .flatMap(str -> Try.ofValue(str.toUpperCase()))
            .getValue();
        Assert.assertEquals("FOO", result);
    }

    @Test(expected = MyException.class)
    public void flatMapError() throws Exception {
        Try.ofValue("foo")
            .flatMap(unused -> Try.ofError(new MyException()))
            .getValue();
    }

    @Test(expected = MyException.class)
    public void flatMapThrows() throws Exception {
        Try.ofValue("foo")
            .flatMap(unused -> { throw new MyException(); })
            .getValue();
    }

    @Test(expected = MyException.class)
    public void flatMapAbort() throws Exception {
        Try.ofError(new MyException())
            .flatMap(unused -> { Assert.fail("This function should never run"); return null; })
            .getValue();
    }

    @Test
    public void unit() throws Exception {
        Assert.assertNull(Try.unit().getValue());
    }

    @Test
    public void of() throws Exception {
        Assert.assertEquals("bar", Try.of(() -> "bar").getValue());
    }

    @Test(expected = MyException.class)
    public void ofThrows() throws Exception {
        Try.of(() -> { throw new MyException(); })
            .getValue();
    }
}
