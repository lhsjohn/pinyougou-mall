app.controller('baseController',function($scope){
    //分页控件配置 currentPage当前页 totalItems总记录数 itemsPerPage每一页记录 perPageOptions分页选项
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        //当页码变更后自动触发的方法
        onChange: function(){
            $scope.reloadList();
        }
    };

    //刷新列表
    $scope.reloadList = function(){
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }


    $scope.selectIds=[]; //用户勾选的id集合

    //用户勾选复选框
    $scope.updateSelection = function($event,id){
        if ($event.target.checked){
            $scope.selectIds.push(id); //向集合添加元素
        }else{
            var index = $scope.selectIds.indexOf(id);//查找值的位置
            //从集合中移掉元素, 参数1 移除的位置 参数2 移除的个数
            $scope.selectIds.splice(index,1);

        }

    }

    //提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
    $scope.jsonToString=function(jsonString,key){
        var json=JSON.parse(jsonString);//将json字符串转换为json对象
        var value="";
        for(var i=0;i<json.length;i++){
            if(i>0){
                value+=","
            }
            value+=json[i][key];
        }
        return value;
    }






});