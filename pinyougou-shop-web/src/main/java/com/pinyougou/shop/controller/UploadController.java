package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;//文件服务器地址



   @RequestMapping("/upload")
   public Result upload(MultipartFile file){

       //1、取文件的扩展名
       String originalFilename = file.getOriginalFilename(); //获取全文件名
       //得到扩展名
       String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
       try {
           //2、创建一个 FastDFS 的客户端
           FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
           //3、执行上传处理
           String fileId = client.uploadFile(file.getBytes(), extName);
           //4、拼接返回的 url 和 ip 地址，拼装成完整的 url
           String url = FILE_SERVER_URL + fileId;
           return new Result(true,url);

       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false,"上传失败");
       }


   }




}
