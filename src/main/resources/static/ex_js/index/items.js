/**
 * Created by vladimir_antin on 17.5.17..
 */
function Items($scope,data,$http,$mdDialog,$mdToast) {
    $scope.data = {
        btn_delete_item:false
    };
    $scope.items_head_items = [
        {name:"id",icon:"arrow_drop_down"},
        {name:"name",icon:"arrow_drop_down"},
        {name:"description",icon:"arrow_drop_down"},
        {name:"sold",icon:"arrow_drop_down"}
    ];


    $scope.sort =function (name){
        angular.forEach($scope.items_head_items, function(value, key) {
            if(value.name==name){
                if(value.icon == "arrow_drop_down"){
                    value.icon = "arrow_drop_up";
                    $scope.orderByHead = "-"+name;
                }else{
                    value.icon = "arrow_drop_down";
                    $scope.orderByHead = name;
                }
            }
        });
    };
    $scope.items = [];

    $http({
        method : "GET",
        url : "/api/items",
        headers:{
            "Content-type":"application/json",
            "Authorization":data.token
        }
    }).then(function(response) {
        if(response.status==200) {
            $scope.items = response.data;
            $scope.me_service();
        }
    },function error(response) {
        if(response.status==401){
            toast_message("Unauthorized, you do not have access","Ok",$mdToast);
            window.location.replace("/home")
        }else if(response.status>=500){
            toast_message("Server Error","Ok",$mdToast);
        }
    });

    $scope.delete_item = function (item){
        var confirm = $mdDialog.confirm()
            .title('Do you sure?')
            .textContent('item with id: "'+item.id+'" will be deleted')
            .ok('Ok')
            .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
            $http({
                method : "DELETE",
                url : "/api/items/"+item.id,
                headers:{
                    "Content-type":"application/json",
                    "Authorization":data.token
                }
            }).then(function (response) {
                if(response.status ==204){
                    indexItem = $scope.items.indexOf(user);
                    $scope.items.splice(indexUser,1);
                    toast_message("Item deleted!","Ok",$mdToast);
                }
            },function error(response) {
                if(response.status==401){
                    toast_message("Unauthorized, it is your account","Ok",$mdToast);
                }else if(response.status>=500){
                    toast_message("Server Error","Ok",$mdToast);
                }
            });
        });
    };


    $scope.me_service = function () {
        $http({
            method: 'GET',
            url: '/api/me',
            headers: {
                "Content-type":"application/json",
                "Authorization":data.token
            }
        }).then(function(response) {
            if(response.status==200){
                $scope.me = response.data;
                if($scope.me!=null){
                    if($scope.me.role=="admin"){
                        $scope.data.btn_delete_item=true;
                    }else{
                        $scope.data.btn_delete_item=false;
                    }
                }
            }
        });
    }
}

