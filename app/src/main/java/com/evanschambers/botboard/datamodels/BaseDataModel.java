package com.evanschambers.botboard.datamodels;

import java.util.UUID;

/**
 * Created by timvalentine on 4/29/16.
 */
public class BaseDataModel {
    protected String uuid;

    public BaseDataModel() {
        init();
    }

    private void init() {
        int i = (int) (UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        uuid = i + "";
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid1) {
        this.uuid = uuid1;
    }
}
