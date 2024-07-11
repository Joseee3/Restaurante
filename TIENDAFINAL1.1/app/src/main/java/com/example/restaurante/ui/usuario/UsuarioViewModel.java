package com.example.restaurante.ui.usuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.restaurante.Entities.Profile;

public class UsuarioViewModel extends ViewModel {
    private final MutableLiveData<Profile> userProfile = new MutableLiveData<>();

    public LiveData<Profile> getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Profile userProfile) {
        this.userProfile.setValue(userProfile);
    }
}