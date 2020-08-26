package net.shybaieva.testtaskaidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTV;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        button.setOnClickListener(MainActivity.this);

    }

    private void init (){
        button =findViewById(R.id.btn);
        resultTV = findViewById(R.id.resultTV);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        bindService();
        Toast.makeText(this, "onAttachedToWindow", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unbindService();
        Toast.makeText(this, "onDeAttachedToWindow", Toast.LENGTH_SHORT).show();
    }

    private void bindService() {
        Intent i = new Intent(MainActivity.this, RemoteService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        super.unbindService(serviceConnection);
    }

    private IRemoteService mIRemoteService;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIRemoteService = IRemoteService.Stub.asInterface(iBinder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onClick(View view) {
        try {
            mIRemoteService.doInBackGround();
            showTextView();
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private void showTextView() {
        if (mIRemoteService == null) {
            Toast.makeText(MainActivity.this, "mIRemoteService == null", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mIRemoteService.registerListener(new IAidlCallBack.Stub() {
                @Override
                public void onValueChanged(final int result) throws RemoteException {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            resultTV.setText("Button clicked " + result + " times");
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
