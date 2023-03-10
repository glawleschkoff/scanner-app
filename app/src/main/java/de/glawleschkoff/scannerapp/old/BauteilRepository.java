package de.glawleschkoff.scannerapp.old;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.glawleschkoff.scannerapp.RecyclerViewItem;
import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BauteilRepository {

    private HttpApi httpApi;
    private MutableLiveData<List<RecyclerViewItem>> data = new MutableLiveData<>();
    private MutableLiveData<Boolean> responseSuccessful = new MutableLiveData<>();

    public BauteilRepository(){
        Retrofit retrofit = RetrofitInstance.getInstance();
        httpApi = retrofit.create(HttpApi.class);
    }

    public void getData(String id){
        Call<BauteilModel> call = httpApi.getBauteil(id);
        call.enqueue(new Callback<BauteilModel>() {
            @Override
            public void onResponse(Call<BauteilModel> call, Response<BauteilModel> response) {
                if(!response.isSuccessful()){
                    responseSuccessful.setValue(Boolean.FALSE);
                    System.out.println("Code: " + response.code());
                } else {
                    Map<String, String> map = BauteilModel.object2Map(response.body());
                    List<RecyclerViewItem> list = new ArrayList<>();

                    SortedSet<String> keys = new TreeSet<>(map.keySet());
                    for (String key : keys) {
                        list.add(new RecyclerViewItem(key, map.get(key)));
                        //String value = map.get(key);
                        // do something
                    }
                    /*
                    for(Map.Entry<String,String> entry : map.entrySet()){
                        list.add(new RecyclerViewItem(entry.getKey(), entry.getValue()));
                        //System.out.println(entry.getKey() + "      " + entry.getValue());
                    }

                     */
                    data.setValue(list);
                    responseSuccessful.setValue(Boolean.TRUE);
                    //System.out.println(response.body().getRowDDMFields());
                }
            }

            @Override
            public void onFailure(Call<BauteilModel> call, Throwable t) {
                responseSuccessful.setValue(Boolean.FALSE);
                System.out.println("onFailure: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<List<RecyclerViewItem>> bindData(){
        return data;
    }
    public MutableLiveData<Boolean> bindResponseSuccessfull(){
        return responseSuccessful;
    }
}
