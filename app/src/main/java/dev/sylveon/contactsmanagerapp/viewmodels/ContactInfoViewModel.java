package dev.sylveon.contactsmanagerapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dev.sylveon.contactsmanagerapp.model.IContactInfo;

public class ContactInfoViewModel<T extends IContactInfo> extends ViewModel {
    private final MutableLiveData<List<T>> data = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<T>> getLiveData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data.setValue(data);
    }

    public void addValue(T value) {
        ArrayList<T> vals = new ArrayList<>(data.getValue());
        vals.add(value);
        data.setValue(vals);
    }

    public void removeValue(T value) {
        ArrayList<T> vals = new ArrayList<>(data.getValue());
        vals.remove(value);
        data.setValue(vals);
    }
}
