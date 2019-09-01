package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */
public interface BrandService {

    public List<TbBrand> findAll();

    /**
     * 品牌分页
     * @param pageNumber 当前页码
     * @param pageSize 每一页大小
     * @return
     */
    public PageResult findPage(int pageNumber,int pageSize);

    /**
     * 增加
     * @param brand
     */
    public void add(TbBrand brand);

    /**
     * 根据id查询实体
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);


    /**
     * 修改
     * @param brand
     */
    public void update(TbBrand brand);


    /**
     * 删除
     * @param ids
     */

    public void delete(Long[] ids);

    /**
     * 条件查询
     * @param brand
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult findPage(TbBrand brand,int pageNumber,int pageSize);

    /**
     * 返回下拉列表数据
     * @return
     */
    public List<Map> selectOptionList();



}
