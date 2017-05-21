/**
 * Created by vladimir_antin on 21.5.17..
 */
function ChangePasswordController($scope,$mdDialog,user,$routeParams,$mdToast,$http) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.user =user;
    $scope.btn_save_password = false;
    $scope.save_password = function() {
        $http({
            method: 'PATCH',
            url: '/api/users/'+$routeParams.userId+"/password",
            headers: {
                "Content-type":"application/json",
                "Authorization":$scope.token
            },
            data:$scope.user
        }).then(function(response) {
            if(response.status==200){
                toast_message("Password was changed!","Ok",$mdToast);
                $mdDialog.hide();
            }
        },function (response) {
            if(response.status==401){
                toast_message("Unauthorized","Ok",$mdToast);
            }else if(response.status>=500){
                toast_message("Server error","Ok",$mdToast);
            }
        });
    };
    $scope.cancel = function() {
        $mdDialog.hide();
    };
    $scope.check_password = function(change_password) {
        if(change_password.$valid){
            $scope.btn_save_password = true;
        }else{
            $scope.btn_save_password = false;
        }
    };
}