/**
 * Created by vladimir_antin on 20.5.17..
 */
function Auction($scope,$http,$routeParams,$mdDialog,$mdToast) {
    $scope.current_date = new Date();
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.data = {
        show:{
            disable_inputs:true,
            disable_start_date:true,
            disable_end_date:true,
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
    }).then(function done(response) {
        if(response.status==200){
            $scope.auction = response.data;
            $scope.auction.startDate = parse_date($scope.auction.startDate);
            $scope.auction.endDate = parse_date($scope.auction.endDate);
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
    },function error(response) {
        if(response.status==404){
            window.location.replace("/404/auctions/"+$routeParams.auctionId);
        }
    });
    $scope.openDeleteMode = function (auction) {
        delete_auction(auction,$scope,$http,$mdDialog,$mdToast);
    };

    $scope.edit_mode = function (edit_form) {
        if($scope.data.show.disable_inputs){
            if(Date.parse($scope.auction.startDate)>Date.parse($scope.current_date)){
                $scope.data.show.disable_start_date=false;
            }else{
                $scope.data.show.disable_start_date=true;
            }
            $scope.data.show.disable_inputs = false;
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
                    toast_message("Auction updated!","Ok",$mdToast);
                    $scope.data.show.disable_inputs = true;
                    $scope.data.show.disable_start_date=true;
                    $scope.data.show.disable_end_date=true;
                    $scope.data.btn_edit.icon = "edit_mode";
                    $scope.data.btn_edit.tooltip = "Update auction";
                }
            },function error(response) {
                if(response.status==409){
                    toast_message("Conflict, problems with start date or end date","Ok",$mdToast);
                }else if(response.status=403){
                    toast_message("Form is not valid","Ok",$mdToast);
                }
            });
        }else{
            toast_message("Form is not valid","Ok",$mdToast);
        }
    };
}