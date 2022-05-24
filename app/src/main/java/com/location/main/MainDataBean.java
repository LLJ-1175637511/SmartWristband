package com.location.main;

import com.llj.baselib.IOTInterfaceId;

public class MainDataBean {

    @IOTInterfaceId("22719")
    private Float blood;

    @IOTInterfaceId("23724")
    private Float o2;

    @IOTInterfaceId("23725")
    private int tilt;

    public Float getBlood() {
        return blood;
    }

    public void setBlood(Float blood) {
        this.blood = blood;
    }

    public Float getO2() {
        return o2;
    }

    public void setO2(Float o2) {
        this.o2 = o2;
    }

    public int getTilt() {
        return tilt;
    }

    public void setTilt(int tilt) {
        this.tilt = tilt;
    }
}
