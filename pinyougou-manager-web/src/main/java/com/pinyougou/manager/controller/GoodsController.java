package com.pinyougou.manager.controller;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Autowired
	private Destination queueSolrDestination; //用于发送solr导入的消息

	@Autowired
	private Destination queueSolrDeleteDestination; //用户在索引库中删除记录

	@Autowired
	private Destination topicPageDestination;

	@Autowired
	private Destination topicPageDeleteDestination;//用于删除静态网页的消息



	@Autowired
	private JmsTemplate jmsTemplate;



//	@Reference
//	private ItemSearchService itemSearchService;

//    @Reference(timeout=40000)
//    private ItemPageService itemPageService;


	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	

	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});

          //删除页面
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return  session.createObjectMessage(ids);
				}
			});

//			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}



	/**
	 * 更新状态
	 * @param ids
	 * @param status
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			goodsService.updateStatus(ids, status);
			//按照SPU ID查询 SKU列表(状态为1)
			if(status.equals("1")){//审核通过
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids, status);
				//调用搜索接口实现数据批量导入
				if(itemList.size()>0){

					final String jsonString  = JSON.toJSONString(itemList);
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(jsonString);
						}
						
					});

//					itemSearchService.importList(itemList);
				}else{
					System.out.println("没有明细数据");
				}

				//静态页生成 生成商品详情页
//				for(Long goodsId:ids){
//					itemPageService.getItemHtml(goodsId);
//				}

				//静态页面生成
				for (final  Long goodsId:ids){
					jmsTemplate.send(topicPageDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(goodsId+"");
						}
					});
				}


			}


			return new Result(true, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "失败");
		}
	}

    /**
     * 生成静态页（测试）
     * @param goodsId
     */
    @RequestMapping("/genHtml")
    public void genHtml(Long goodsId){
//        itemPageService.getItemHtml(goodsId);
    }


}
