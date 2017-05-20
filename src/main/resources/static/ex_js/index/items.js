/**
 * Created by vladimir_antin on 17.5.17..
 */
function Items($scope,$http,$mdDialog,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.data = {
        btn_delete_item:false
    };
    $scope.items_head_items = [
        {name:"id",icon:"arrow_drop_down"},
        {name:"name",icon:"arrow_drop_down"},
        {name:"description",icon:"arrow_drop_down"},
        {name:"sold",icon:"arrow_drop_down"}
    ];


    $scope.sort =function (name,sort_items){
        sort($scope,name,sort_items);
    };
    $scope.items = [];

    $http({
        method : "GET",
        url : "/api/items",
        headers:{
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200) {
            $scope.items = response.data;
            me_service($http,$scope, function (me) {
                $scope.me=me;
                if($scope.me!=null){
                    if($scope.me.role=="admin"){
                        $scope.data.btn_delete_item=true;
                    }else if($scope.me.role=="owner"){
                        $scope.data.btn_delete_item=false;
                    }else{
                        window.location.replace("#/")
                    }
                }
            });
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
                    "Authorization":$scope.token
                }
            }).then(function (response) {
                if(response.status ==204){
                    indexItem = $scope.items.indexOf(item);
                    $scope.items.splice(indexItem,1);
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
}

