/**
 * Created by vladimir_antin on 20.5.17..
 */
function Auction($scope,$http,$routeParams,$mdDialog,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.data = {
        show:{
            disable_input:true,
            btn_edit:false,
            btn_delete:false
        },
        btn_edit:{
            icon:"edit_mode",
            tooltip:"Upadate auction"
        }

    };
    $http({
        method: 'GET',
        url: '/api/auctions/'+$routeParams.auctionId,
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.auction = response.data;
            me_service($http,$scope, function (me) {
                $scope.me=me;
                if($scope.me!=null){
                        $scope.data.show.btn_edit=true;
                        $scope.data.show.btn_delete=true;
                }else{
                    $scope.data.show.btn_edit=false;
                    $scope.data.show.btn_delete=false;
                }
            });
        }
    });
    $scope.openDeleteMode = function (auction) {
        delete_auction(auction,$scope,$http,$mdDialog,$mdToast);
    };

    $scope.edit_mode = function (edit_form) {
        if($scope.data.show.disable_input){
            $scope.data.show.disable_input = false;
            $scope.data.btn_edit.icon = "save";
            $scope.data.btn_edit.tooltip = "Save";
        }else if (edit_form.$valid){
            $http({
                method: 'PUT',
                url: '/api/auctions/'+$routeParams.auctionId,
                headers: {
                    "Content-type":"application/json",
                    "Authorization":$scope.token
                },
                data:$scope.auction
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
}