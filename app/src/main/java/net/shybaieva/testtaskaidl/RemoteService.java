package net.shybaieva.testtaskaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.widget.Toast;

public class RemoteService extends Service {
    private RemoteCallbackList<IAidlCallBack> mRemoteCallbackList = new RemoteCallbackList<>();
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service is started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service is done", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    class Binder extends IRemoteService.Stub{

        @Override
        public void registerListener(IAidlCallBack callBack) {
            if (mRemoteCallbackList == null) {
                return;
            }
            mRemoteCallbackList.register(callBack);
        }

        @Override
        public void unRegisterListener(IAidlCallBack callBack) {
            mRemoteCallbackList.unregister(callBack);
        }

        @Override
        public void doInBackGround() {
            new Thread(){
                @Override
                public void run() {

                    int count = mRemoteCallbackList.beginBroadcast();
                    long result = currentThread().getId();
                    if (count == 0) {
                        return;
                    }
                    try {
                        for (int i = 0; i < count; i++) {

                            mRemoteCallbackList.getBroadcastItem(i).onValueChanged ((int) result);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } finally {
                        mRemoteCallbackList.finishBroadcast();
                    }
                }
            }.start();


        }
    }
}
