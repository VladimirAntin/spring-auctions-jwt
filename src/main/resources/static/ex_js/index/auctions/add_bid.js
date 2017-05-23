/**
 * Created by vladimir_antin on 23.5.17..
 */
function AddBidController($scope,$mdDialog,bid,bids,$mdToast,$http) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.bid =bid;
    $scope.bids=bids;
    $scope.btn_save_bid = false;
    $scope.save_bid = function() {
        $http({
            method: 'POST',
            url: '/api/bids/',
            headers: {
                "Content-type":"application/json",
                "Authorization":$scope.token
            },
            data:$scope.bid
        }).then(function(response) {
            if(response.status==201){
                $scope.bids.push(response.data);
                toast_message("Added new bid!","Ok",$mdToast);
                $mdDialog.hide();
            }
        },function (response) {
            if(response.status==401) {
                toast_message("Unauthorized", "Ok", $mdToast);
            }else if(response.status==404){
                toast_message("Not found, auction is not found","Ok",$mdToast);
            }else if(response.status==409){
                toast_message("Conflict, sorry end date is greater than today","Ok",$mdToast);
            }else if(response.status>=500){
                toast_message("Server error","Ok",$mdToast);
            }
        });
    };
    $scope.cancel = function() {
        $mdDialog.hide();
    };
    $scope.check_price = function(add_bid) {
        if($scope.bid.price>0){
            if(add_bid.$valid){
                $scope.btn_save_bid = true;
            }else{
                $scope.btn_save_bid = false;
            }
        }else{
            $scope.btn_save_bid = false;
        }
    };
}