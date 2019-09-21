app.controller('login', function ($scope, $http, $location) {
    alert('hello');

    $scope.login_check = function () {
        if (($scope.username == '') || ($scope.password == '')) {
            return;
        }

        var data = {

            username: $scope.username,
            password: $scope.password,
            remember_me: $scope.remember_me,
            mobile: 'false'
        };


        $http({
            method: "POST",
            // url: "../vts_apis/session.php",
            url: '/login',
            // url: "../tracknovate_crm:8443/login",
            // url: "../login",
            data: data,
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).then(function (response) {

            // console.log(response.data.errorCode);

            if (response.data.data[0].user_type == 'user') {
                // sessionStorage.setItem("id", response.data.data[0].id);
                // sessionStorage.setItem("username", response.data.data[0].username);
                $location.path("/userDashboard");
            } else {
                $location.path("/adminDashboard");
            }
        }, function (response) {
            alert('server error occured')
        })
    }
    $scope.logout = function () {
        localStorage.removeItem('id', 'username');
        $location.path("/crm");
    }
});