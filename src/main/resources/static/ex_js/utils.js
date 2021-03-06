/**
 * Created by vladimir_antin on 13.5.17..
 */

function toast_message(text,btn_ok,mdToast) {
    mdToast.show(
        mdToast.simple()
            .textContent(text)
            .action(btn_ok)
            .highlightAction(true)
    );
}

function delete_auction(auction,$scope,$http,$mdDialog,$mdToast){
    var confirm = $mdDialog.confirm()
        .title('Do you sure?')
        .textContent('Auction with id: "'+auction.id+'" will be deleted')
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
}

function delete_bid(bid,$scope,$http,$mdDialog,$mdToast){
    var confirm = $mdDialog.confirm()
        .title('Do you sure?')
        .textContent('Bid with id: "'+bid.id+'" will be deleted')
        .ok('Ok')
        .cancel('Cancel');
    $mdDialog.show(confirm).then(function() {
        $http({
            method : "DELETE",
            url : "/api/bids/"+bid.id,
            headers:{
                "Content-type":"application/json",
                "Authorization":$scope.token
            }
        }).then(function (response) {
            if(response.status ==204){
                indexBid = $scope.bids.indexOf(bid);
                $scope.bids.splice(indexBid,1);
                toast_message("Bid deleted!","Ok",$mdToast);
            }
        },function error(response) {
            if(response.status==401){
                toast_message("Unauthorized, it is your account","Ok",$mdToast);
            }else if(response.status>=500){
                toast_message("Server Error","Ok",$mdToast);
            }
        });
    });
}

function me_service($http,$scope,callback) {
    $http({
        method: 'GET',
        url: '/api/me',
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            callback(response.data);
        }
    });
}

function sort($scope,name,sort_items) {
    angular.forEach(sort_items, function(value, key) {
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
}


function parse_date(date) {
    try{
        var dateParts = date.split("/");
        d = dateParts[0];
        m = dateParts[1]-1; // problems parsing month, counting m+1, I'm returning it
        y = dateParts[2];
        return new Date(y,m,d);
    }catch(e){
        return null;
    }
}

