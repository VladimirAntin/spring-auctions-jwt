/**
 * Created by vladimir_antin on 19.5.17..
 */
function Add_item($scope,$http,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    me_service($http,$scope, function (me) {
        $scope.me=me;
        if ($scope.me != null) {
            if ($scope.me.role == "bidder") {
                window.location.replace("/401/items/add");
            }
        }
    });
    $scope.item ={
        name:"",
        description:""
    };
    $scope.data={
        valid:{
            name:""
        },
        btn_save:true
    };
    $scope.change_inputs = function (add_form) {
        if(add_form.$valid){
            $scope.data.valid.name = "";
            $scope.data.btn_save=false;
        }else{
            if($scope.item.name==""){
                $scope.data.valid.name = "Name is empty.";
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
                url: '/api/items',
                headers: {
                    'Content-Type': "application/json",
                    "Authorization":$scope.token
                },
                data: $scope.item
            }).then(function success(response) {
                if(response.status==201){
                    window.location.replace("#/items")
                }
            }, function error(response) {
                if(response.status==409){
                    toast_message("Conflict, name is not valid","Ok",$mdToast);
                }
            });
        }
    }
}