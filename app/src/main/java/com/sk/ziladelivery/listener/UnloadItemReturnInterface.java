package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.ReturnItemListResponseModel;

public interface UnloadItemReturnInterface {
    void checkBoxClick(boolean isChecked, ReturnItemListResponseModel unloadItemListModel, int postion);
    void llCamera(ReturnItemListResponseModel model, boolean cheked);

}