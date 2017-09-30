package com.skylight.testurlconnect;

/**
 * Created by tangfen on 2017/9/28.
 *
 * @创建者 tangfen
 * @创建时间 2017/9/28 10:55
 * @描述 ${TODO}
 */

public class Information {
    private String id;
    private String name;
    private String picSmall;
    private String picBig;
    private String description;

    public Information(String id, String name, String picSmall, String picBig, String description) {
        this.id = id;
        this.name = name;
        this.picSmall = picSmall;
        this.picBig = picBig;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicSmall() {
        return picSmall;
    }

    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall;
    }

    public String getPicBig() {
        return picBig;
    }

    public void setPicBig(String picBig) {
        this.picBig = picBig;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
