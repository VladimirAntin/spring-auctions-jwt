/**
 * Created by vladimir_antin on 19.5.17..
 */
function Auctions($scope,$http,$mdDialog,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.data = {
        btn_delete_auction:false
    };
    $scope.auctions_head_items = [
        {title:"id",icon:"arrow_drop_down", name:"id"},
        {title:"item name",icon:"arrow_drop_down", name:"item.id"},
        {title:"item description",icon:"arrow_drop_down", name:"item.description"},
        {title:"User",icon:"arrow_drop_down", name:"user.email"},
        {title:"start date",icon:"arrow_drop_down", name:"startDate"},
        {title:"end date",icon:"arrow_drop_down", name:"endDate"},
        {title:"start price",icon:"arrow_drop_down", name:"startPrice"},
        {title:"item sold",icon:"arrow_drop_down", name:"item.sold"}
    ];

    $scope.sort =function (name){
        angular.forEach($scope.auctions_head_items, function(value, key) {
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
    $scope.auctions = [];

    $http({
        method : "GET",
        url : "/api/auctions",
        headers:{
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200) {
            $scope.auctions = response.data;
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

    $scope.delete_auction = function (auction){
        var confirm = $mdDialog.confirm()
            .title('Do you sure?')
            .textContent('item with id: "'+auction.id+'" will be deleted')
            .ok('Ok')
            .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
            $http({
                method : "DELETE",
                url : "/api/auctions/"+auction.id,
                headers:{
                    "Content-type":"application/json",
                    "Authorization":$scope.token
                }
            }).then(function (response) {
                if(response.status ==204){
                    indexAuction = $scope.auctions.indexOf(auction);
                    $scope.auctions.splice(indexAuction,1);
                    toast_message("Auction deleted!","Ok",$mdToast);
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
                "Authorization":$scope.token
            }
        }).then(function(response) {
            if(response.status==200){
                $scope.me = response.data;
                if($scope.me!=null){
                    if($scope.me.role=="admin"){
                        $scope.data.btn_delete_auction=true;
                    }else{
                        $scope.data.btn_delete_auction=false;
                    }
                }
            }
        });
    }
}

