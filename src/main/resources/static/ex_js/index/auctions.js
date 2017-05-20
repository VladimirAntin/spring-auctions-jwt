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
            me_service($http,$scope, function (me) {
                $scope.me=me;
                if($scope.me!=null){
                    if($scope.me.role=="admin"){
                        $scope.data.btn_delete_auction=true;
                    }else{
                        $scope.data.btn_delete_auction=false;
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

    $scope.delete_auction = function (auction){
        delete_auction(auction,$scope,$http,$mdDialog,$mdToast);
    };

}

