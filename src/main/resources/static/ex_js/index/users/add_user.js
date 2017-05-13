/**
 * Created by vladimir_antin on 13.5.17..
 */
function Add_users($scope,data,$http,$mdToast) {
    $scope.user ={
        name:"",
        email:"",
        password:"",
        address:"",
        phone:"",
        role:"bidder"
    };
    $scope.data={
        valid:{
            name:"",
            email:"",
            password:""
        },
        btn_save:true
    };
    $scope.change_inputs = function (add_form) {
        if(add_form.$valid){
            $scope.data.valid.name = "";
            $scope.data.valid.email = "";
            $scope.data.valid.password = "";
            $scope.data.btn_save = false;
        }else{
            if($scope.user.name==""){
                $scope.data.valid.name = "Name is empty.";
            }
            if(add_form.$error.pattern==undefined){
                if($scope.user.email==""){
                    $scope.data.valid.email = "Email is empty.";
                }
            }else{
                if($scope.user.email!=""){
                    $scope.data.valid.email = "Your email must be between 6 and 30 characters long and look like an e-mail address.";
                }
            }
            if($scope.user.password==""){
                $scope.data.valid.password = "Password is empty.";
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
                url: '/api/users',
                headers: {
                    'Content-Type': "application/json",
                    "Authorization":data.token
                },
                data: $scope.user
            }).then(function success(response) {
                if(response.status==201){
                    window.location.replace("#/users")
                }
            }, function error(response) {
                if(response.status==409){
                    $scope.data.valid.email = "Email is exist.";
                    toast_message("Conflict, email is exist","Ok",$mdToast);
                }
            });
        }
    }
}