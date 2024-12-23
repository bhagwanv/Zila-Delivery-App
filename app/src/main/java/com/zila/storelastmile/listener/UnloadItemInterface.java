package com.zila.storelastmile.listener;

import com.zila.storelastmile.data.model.UnloadItemListModel;

public interface UnloadItemInterface {
    void checkBoxClicked(boolean isChecked, UnloadItemListModel unloadItemListModel, int postion);

}