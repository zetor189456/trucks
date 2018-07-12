(function() {
    'use strict';

    angular
        .module('trucksApp')
        .controller('CostDetailController', CostDetailController);

    CostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cost', 'Truck'];

    function CostDetailController($scope, $rootScope, $stateParams, previousState, entity, Cost, Truck) {
        var vm = this;

        vm.cost = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trucksApp:costUpdate', function(event, result) {
            vm.cost = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
