package com.advance.imagewithviewpagerfromfirebase.Interface;

import com.advance.imagewithviewpagerfromfirebase.Model.Model;

import java.util.List;

public interface IFirebaseLoadDone {

     void onFirebaseLoadSuccess(List<Model> imgList);
     void onFirebaseLoadFailed(String message);
}
