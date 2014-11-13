hrApp.controller('EmployeeEditController', ['$scope', '$http', '$routeParams', '$location', function ($scope, $http, $routeParams, $location) {

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

    /**
     * Reset form
     */
    $scope.reset = function () {
        $scope.employee = {};
    };

    /**
     * Persist an employee
     * @param addEmployee - employee to be persisted
     */
    $scope.create = function (addEmployee) {
        console.log(addEmployee);
        $http({url: 'http://localhost:8282/datamodel/employees/edit', method: 'POST',data:addEmployee}).
            success(function (data) {
                $scope.employee = data;
                $location.url('/employeeview/'+$scope.employee.employeeId);
            });
    }
}]);