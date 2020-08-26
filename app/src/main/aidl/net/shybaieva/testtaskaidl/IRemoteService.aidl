// IRemoteService.aidl
package net.shybaieva.testtaskaidl;
import net.shybaieva.testtaskaidl.IAidlCallBack;

interface IRemoteService {

    void registerListener(IAidlCallBack callBack);
    void unRegisterListener(IAidlCallBack callBack);
    void doInBackGround();
}
