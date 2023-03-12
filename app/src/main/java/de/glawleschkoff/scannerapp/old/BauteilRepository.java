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
    private MutableLiveData<Integer> responseState = new MutableLiveData<>();

    public BauteilRepository(){
        Retrofit retrofit = RetrofitInstance.getInstance();
        httpApi = retrofit.create(HttpApi.class);
        responseState.setValue(0);
    }

    public void getData(String id){
        Call<BauteilModel> call = httpApi.getBauteil(id);
        call.enqueue(new Callback<BauteilModel>() {
            @Override
            public void onResponse(Call<BauteilModel> call, Response<BauteilModel> response) {
                if(!response.isSuccessful()){
                    responseState.setValue(-1);
                    System.out.println("Code: " + response.code());
                } else {
                    Map<String, String> map = BauteilModel.object2Map(response.body());
                    List<RecyclerViewItem> list = new ArrayList<>();

                    SortedSet<String> keys = new TreeSet<>(map.keySet());
                    for (String key : keys) {
                        list.add(new RecyclerViewItem(key, map.get(key)));
                    }
                    data.setValue(list);
                    responseState.setValue(+1);
                }
            }

            @Override
            public void onFailure(Call<BauteilModel> call, Throwable t) {
                responseState.setValue(-1);
            }

        });
    }

    public MutableLiveData<List<RecyclerViewItem>> bindData(){
        return data;
    }
    public MutableLiveData<Integer> bindResponseSuccessfull(){
        return responseState;
    }
}
