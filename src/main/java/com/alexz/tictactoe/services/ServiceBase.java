package com.alexz.tictactoe.services;

public abstract class ServiceBase implements IService {

    public ServiceBase() {
        this.init();
    }

    @Override
    public void init() {
    }
}
