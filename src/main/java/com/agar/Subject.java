package com.agar;

/**
 * Created by SDEV2 on 30/06/2016.
 */
public interface Subject
{
    void register(Subscriber subscriber);
    void unregister(Subscriber subscriber);
    void notifySubscribers();
    void setValue(Object object);
    Object getValue();
}
