package com.blood.blooddonation;

import java.net.URL;

public class Updater {
    private float version;
    private String updateEnable;
    private String updateUrl;

    public Updater() {
    }

    public Updater(float version, String updateEnable, String updateUrl) {
        this.version = version;
        this.updateEnable = updateEnable;
        this.updateUrl = updateUrl;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(String updateEnable) {
        this.updateEnable = updateEnable;
    }

    public String  getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
