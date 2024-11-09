package com.example.aula_08_11_app_conversao_thiagodossantos.Api;

public interface ApiCallback<T>{

    void onSuccess(T result);
    void onError(String error);
}
