package com.jarvuino.core.io;

public interface ResponseListener<T> {

    void onMessage(T msg);

}
