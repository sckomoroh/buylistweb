package com.ghyrosoft.client.common;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/27/13
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeyValue<TKey, TValue>
{
    private TKey key;
    private TValue value;

    public KeyValue(TKey key, TValue value)
    {
        this.key = key;
        this.value = value;
    }

    public TKey getKey()
    {
        return this.key;
    }

    public TValue getValue()
    {
        return this.value;
    }

    public void setKey(TKey key)
    {
        this.key = key;
    }

    public void setValue(TValue value)
    {
        this.value = value;
    }
}