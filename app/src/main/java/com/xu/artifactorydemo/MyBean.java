package com.xu.artifactorydemo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


/**
 * Created by xutengfei
 * on 2020/12/23
 */
public class MyBean extends BaseObservable {
    public String name;
    public String title;
    public String img;
    public String content;
    public String date;

    public MyBean(String name, String title, String img, String content, String date) {
        this.name = name;
        this.title = title;
        this.img = img;
        this.content = content;
        this.date = date;
    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
