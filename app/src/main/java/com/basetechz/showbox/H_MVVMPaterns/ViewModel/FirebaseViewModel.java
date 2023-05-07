package com.basetechz.showbox.H_MVVMPaterns.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.basetechz.showbox.H_MVVMPaterns.Repo.FirebaseRepo;
import com.basetechz.showbox.G_models.parent_model;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class FirebaseViewModel extends ViewModel implements FirebaseRepo.OnRealtimeDbTaskComplete{
    private String reference;
    private MutableLiveData<List<parent_model>> parentModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();
    private FirebaseRepo firebaseRepo;

    public MutableLiveData<List<parent_model>> getParentModelMutableLiveData() {
        return parentModelMutableLiveData;
    }

    public void setParentModelMutableLiveData(MutableLiveData<List<parent_model>> parentModelMutableLiveData) {
        this.parentModelMutableLiveData = parentModelMutableLiveData;
    }

    public MutableLiveData<DatabaseError> getDatabaseErrorMutableLiveData() {
        return databaseErrorMutableLiveData;
    }

    public void setDatabaseErrorMutableLiveData(MutableLiveData<DatabaseError> databaseErrorMutableLiveData) {
        this.databaseErrorMutableLiveData = databaseErrorMutableLiveData;
    }

    public FirebaseViewModel(){
        firebaseRepo = new FirebaseRepo(this);
    }
    public void getAllData(){
        firebaseRepo.getAllData(reference);
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public void onSuccess(List<parent_model> parentModelList) {
        parentModelMutableLiveData.setValue(parentModelList);
    }

    @Override
    public void onFailure(DatabaseError error) {
        databaseErrorMutableLiveData.setValue(error);
    }
}
