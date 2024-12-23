package com.zila.storelastmile.listener;

import com.zila.storelastmile.ui.views.fragment.unloadReturnItem.ReturnItemListResponseModel;

public interface UnloadItemReturnInterface {
    void checkBoxClick(boolean isChecked, ReturnItemListResponseModel unloadItemListModel, int postion);
    void llCamera(ReturnItemListResponseModel model, boolean cheked);

}