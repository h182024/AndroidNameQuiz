package com.example.namequizapp.data;


import java.util.List;

public class UploadList extends Uploads{
    private List<Uploads> uList;

    public UploadList(){

    }
    public UploadList( List<Uploads> list){
        this.uList = list;

    }

    public List<Uploads> getuList() {
        return uList;
    }

    public void setuList(List<Uploads> uList) {
        this.uList = uList;
    }

}
