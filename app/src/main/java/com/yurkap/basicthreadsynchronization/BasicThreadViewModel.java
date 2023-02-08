package com.yurkap.basicthreadsynchronization;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicThreadViewModel extends ViewModel implements Runnable {
    private static final String TAG = "BasicThreadViewModel";
    private volatile MutableLiveData<Integer> value;

    public MutableLiveData<Integer> getValue() {
        if (value == null) {
            value = new MutableLiveData<>();
        }

        return value;
    }

    public void setValue(int value) {
        this.value.postValue(value);
    }

    private MutableLiveData<Boolean> buttonStateListener;

    public MutableLiveData<Boolean> getButtonStateListener() {
        if (buttonStateListener == null) {
            buttonStateListener = new MutableLiveData<>();
        }
        return buttonStateListener;
    }

    public void setButtonStateListener(boolean buttonStateListener) {
        this.buttonStateListener.postValue(buttonStateListener);
    }

    private volatile boolean running = false;

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    private volatile int increment = 0;

    private synchronized void increment() {
        this.increment++;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public void clearData(int increment) {
        this.increment = increment;
        setValue(getIncrement());
    }

    @Override
    public void run() {
        //Set the disable state of the "Start" button.
        //Set the enable state of the "Stop" button.
        setButtonStateListener(true);
        int i = 0;
        int amount = 100;
        while (i < amount) {

            if (!running || amount == getIncrement()) {
                Log.d(TAG, "run: break ");
                break;
            }

            increment();
            setValue(getIncrement());
            Log.d(TAG, "run: Hello " + getIncrement());

            i++;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //Set the enable state of the "Start" button.
        //Set the disable state of the "Stop" button.
        setButtonStateListener(false);

        if (amount == getIncrement()) {
            setIncrement(0);
        }

    }

    public void init() {
        service.execute(this);
//        new Thread(this).start();
    }

    public static final ExecutorService service =
            Executors.newFixedThreadPool(1);


}
