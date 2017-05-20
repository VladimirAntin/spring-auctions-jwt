/**
 * Created by vladimir_antin on 19.5.17..
 */
function Item($scope,$http,$routeParams,$mdDialog,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.data = {
        show:{
            disable_input:true,
            btn_edit:false,
            btn_delete:false,
            btn_delete_auction:false
        },
        btn_edit:{
            icon:"edit_mode",
            tooltip:"Upadate item"
        }

    };
    $http({
        method: 'GET',
        url: '/api/items/'+$routeParams.itemId,
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.item = response.data;
            me_service($http,$scope, function (me) {
                $scope.me=me;
                if($scope.me!=null){
                    if($scope.me.role=="admin"){
                        $scope.data.show.btn_edit=true;
                        $scope.data.show.btn_delete=true;
                        $scope.data.show.btn_delete_auction=true;
                    }else{
                        $scope.data.show.btn_edit=false;
                        $scope.data.show.btn_delete=false;
                        $scope.data.show.btn_delete_auction=false;
                    }
                }
            });
        }
    });
    $http({
        method: 'GET',
        url: '/api/items/'+$routeParams.itemId+"/auctions",
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.auctions = response.data;
        }
    });
    $scope.openDeleteMode = function (item) {
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
                    window.location.replace("#/items");
                }
            },function error(response) {
                if(response.status==401){
                    toast_message("Unauthorized, you can not delete item","Ok",$mdToast);
                }else if(response.status>=500){
                    toast_message("Server Error","Ok",$mdToast);
                }
            });
        });
    };

    $scope.edit_mode = function (edit_form) {
        if($scope.data.show.disable_input){
            $scope.data.show.disable_input = false;
            $scope.data.btn_edit.icon = "save";
            $scope.data.btn_edit.tooltip = "Save";
        }else if (edit_form.$valid){
            $http({
                method: 'PUT',
                url: '/api/items/'+$routeParams.itemId,
                headers: {
                    "Content-type":"application/json",
                    "Authorization":$scope.token
                },
                data:$scope.item
            }).then(function done(response) {
                if(response.status==200){
                    toast_message("Item updated!","Ok",$mdToast);
                    $scope.data.show.disable_input = true;
                    $scope.data.btn_edit.icon = "edit_mode";
                    $scope.data.btn_edit.tooltip = "Update item";
                }
            },function error(response) {
                if(response.status==401){
                    toast_message("Unauthorized","Ok",$mdToast);
                }
            });
        }else{
            toast_message("Form is not valid","Ok",$mdToast);
        }
    };

    $scope.change_photo = function () {
        $.ajax({
            url: 'api/items/'+$scope.item.id+'/upload',
            type: 'POST',
            headers:{
                "Authorization":$scope.token
            },
            data: new FormData($('form')[0]),
            cache: false,
            contentType: false,
            processData: false,
            statusCode:{
                200:function (response) {
                    window.location.reload();
                }
            }
        });
    };
    $scope.auctions_head_items = [
        {title:"id",icon:"arrow_drop_down", name:"id"},
        {title:"User",icon:"arrow_drop_down", name:"user.email"},
        {title:"start date",icon:"arrow_drop_down", name:"startDate"},
        {title:"end date",icon:"arrow_drop_down", name:"endDate"},
        {title:"start price",icon:"arrow_drop_down", name:"startPrice"},
        {title:"sold",icon:"arrow_drop_down", name:"item.sold"}
    ];
    $scope.items_sold = [
        {name:"All auctions",value:""},
        {name:"Sold",value:"true"},
        {name:"Not sold",value:"false"}
    ];
    $scope.itemSold;
    $scope.isSold = function() {
        if ($scope.itemSold !== undefined) {
            return $scope.itemSold.name;
        }
    };
    $scope.sort =function (name,sort_items){
        sort($scope,name,sort_items);
    };

    $scope.delete_auction = function (auction) {
        delete_auction(auction,$scope,$http,$mdDialog,$mdToast);
    }

}