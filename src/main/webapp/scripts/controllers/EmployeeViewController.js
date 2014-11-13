hrApp.controller('EmployeeViewController', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {
    $http({url: 'http://localhost:8080/app/mvc/departments/all', method: 'GET'}).
        success(function (data) {
            $scope.departments = data;
        });
    $http({url: 'http://localhost:8080/app/mvc/employees/all', method: 'GET'}).
        success(function (data) {
            $scope.managers = data;
        });

    $http({url: 'http://localhost:8080/app/mvc/jobs/all', method: 'GET'}).
        success(function (data) {
            $scope.jobs = data;
        });

    $http({url: 'http://localhost:8080/app/mvc/employees/one?id='+$routeParams.employeeid, method: 'GET'}).
        success(function (data) {
            $scope.employee = data;
        });
}]);