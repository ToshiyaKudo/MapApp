package com.example.kudotoshiya.mapapp;

import com.squareup.otto.Bus;

/**
 * Created by kudo.toshiya on 2015/06/05.
 */
public class BusHolder {

    private static Bus sBus = new Bus();

    public static Bus get() {
        return sBus;
    }
}
