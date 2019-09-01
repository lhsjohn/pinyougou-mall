package com.pinyougou.page.service;

/**
 * 商品详情页接口
 */
public interface ItemPageService {

    /**
     * 生成商品详情页
     * @param goodsId
     * @return
     */
    public boolean getItemHtml(Long goodsId);

    /**
     * 删除商品详细页
     * @param goodsIds
     * @return
     */
    public boolean deleteItemHtml(Long[] goodsIds);


}
