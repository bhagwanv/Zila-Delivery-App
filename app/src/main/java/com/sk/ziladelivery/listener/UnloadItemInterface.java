package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.data.model.UnloadItemListModel;

public interface UnloadItemInterface {
    void checkBoxClicked(boolean isChecked, UnloadItemListModel unloadItemListModel, int postion);

}