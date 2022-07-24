package com.basic.image.observable;


/**
 * author：luck
 * project：PictureSelector
 * package：com.basic.image.observable
 * email：893855882@qq.com
 * data：17/1/16
 */
public interface SubjectListener {
    void add(ObserverListener observerListener);

    void remove(ObserverListener observerListener);
}
