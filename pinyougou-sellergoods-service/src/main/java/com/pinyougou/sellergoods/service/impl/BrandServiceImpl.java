package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class  BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;


    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNumber, int pageSize) {

        //使用mybatis的分页插件
        PageHelper.startPage(pageNumber,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TbBrand brand) {
         brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Long[] ids) {
      for (Long id:ids){
          brandMapper.deleteByPrimaryKey(id);
      }
    }

    @Override
    public PageResult findPage(TbBrand brand, int pageNumber, int pageSize) {
        //使用mybatis的分页插件
        PageHelper.startPage(pageNumber,pageSize);

        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (brand!=null){
            if (brand.getName()!=null&&brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar()!=null&&brand.getFirstChar().length()>0){

                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }


        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }


}
