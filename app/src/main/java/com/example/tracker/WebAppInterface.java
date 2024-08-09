package com.example.tracker;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void receiveExpenseData(String expenseData) {
        // This method will be called from JavaScript
        // You might log or process data here if needed
    }
}
