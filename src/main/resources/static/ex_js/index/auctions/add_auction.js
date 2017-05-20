/**
 * Created by vladimir_antin on 20.5.17..
 */
function Add_auction($scope,$http,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.currentDate =new Date();
    $http({
        method: 'GET',
        url: '/api/items',
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function done(response) {
        //
        $scope.items = response.data;
    });
    $scope.getSelectedItem = function() {
        if ($scope.auction.item !== undefined) {
            return $scope.auction.item.name;
        }
    };
    $scope.auction ={
        startPrice:"",
        startDate:new Date(),
        endDate:new Date(),
        item:{id:1}
    };
    $scope.data={
        valid:{
            startPrice:"",
            item:"",
            startDate:"",
            endDate:""
        },
        btn_save:true
    };
    $scope.change_inputs = function (add_form) {
        if(add_form.$valid){
            $scope.data.valid.startPrice = "";
            $scope.data.valid.item = "";
            $scope.data.valid.startDate = "";
            $scope.data.valid.endDate = "";
            $scope.data.btn_save=false;
        }else{
            if(isNaN($scope.auction.startPrice)){
                $scope.data.valid.startPrice = "Price must be positive number.";
            }else{
                $scope.data.valid.startPrice = "";
            }
            if($scope.auction.item==null){
                $scope.data.valid.item = "Please select item.";
            }else{
                $scope.data.valid.item = "";
            }
            if(Date.parse($scope.auction.startDate)>=Date.parse($scope.currentDate)){
                $scope.data.valid.startDate = "";
            }else{
                $scope.data.valid.startDate = "Date must be greater than today.";
            }
            if(Date.parse($scope.auction.startDate)>=Date.parse($scope.auction.endDate)){
                $scope.data.valid.endDate = "";
            }else{
                $scope.data.valid.endDate = "Date must be greater than start date.";
            }
            $scope.data.btn_save = true;
        }
    };
    $scope.save = function (add_form) {
        if(!add_form.$valid){
            toast_message("Form is not valid!","Ok",$mdToast);
        }else{
            $http({
                method: 'POST',
                url: '/api/auctions',
                headers: {
                    'Content-Type': "application/json",
                    "Authorization":$scope.token
                },
                data: $scope.auction
            }).then(function success(response) {
                if(response.status==201){
                    window.history.back();
                }
            }, function error(response) {
                if(response.status==409){
                    toast_message("Conflict, form is not valid","Ok",$mdToast);

                }
            });
        }
    }
}