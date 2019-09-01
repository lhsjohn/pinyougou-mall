//文件上传服务层
app.service("uploadService",function($http){
    this.uploadFile=function(){
        var formData=new FormData();
        formData.append("file",file.files[0]); // file.files[0]  第一个file 文件上传框的name
        return $http({
            method:'POST',
            url:"../upload.do",
            data: formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });
    }
});
