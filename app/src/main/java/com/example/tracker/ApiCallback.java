// ApiCallback.java
package com.example.tracker;

public interface ApiCallback<T> {
    void onResponse(T result);
    void onFailure(String error);
}
