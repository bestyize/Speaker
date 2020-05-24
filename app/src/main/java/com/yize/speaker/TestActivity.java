package com.yize.speaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.yize.autobus.AutoBus;
import com.yize.autobus.Subscribe;
import com.yize.autobus.WorkMode;
import com.yize.autobus.WorkPriority;
import com.yize.speaker.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {
    private static final String TAG="TestActivity";
    private ActivityTestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int i=20;
                        while (i-->0){
                            try {
                                Thread.sleep(2000);
                                AutoBus.with(TestActivity.this).publish(new MyMessage("AutoBus发出的消息"));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 默认，主线程、中等优先级
     * @param message
     */
    @Subscribe
    public void receiver(MyMessage message){
        Log.i(TAG,message.msg);
    }
    /**
     * 默认主线程、中等优先级
     * @param message
     */
    @Subscribe(workMode = WorkMode.THREAD_MAIN,workPriority = WorkPriority.PRIORITY_DEFAULT)
    public void receiver2(MyMessage message){
        Log.i(TAG,message.msg);
    }
    /**
     * 默认，主线程、高优先级
     * @param message
     */
    @Subscribe(workPriority = WorkPriority.PRIORITY_HIGH)
    public void receiver3(MyMessage message){
        Log.i(TAG,message.msg);
    }
    /**
     * 默认，子线程、高优先级
     * @param message
     */
    @Subscribe(workMode = WorkMode.THREAD_ASYNC,workPriority = WorkPriority.PRIORITY_HIGH)
    public void receiver4(MyMessage message){
        Log.i(TAG,message.msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
