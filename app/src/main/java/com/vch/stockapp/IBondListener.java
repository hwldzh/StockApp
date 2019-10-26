package com.vch.stockapp;

import java.util.List;

/**
 * 可转债内容回调接口
 *
 * @author VC_H
 * @date 2019-10-26
 */
public interface IBondListener {
    void onBondMsgSuccess(List<BondModel> bondList);

    void onBondMsgFailed(String errMsg);
}
