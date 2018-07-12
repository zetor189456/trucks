(function() {
    'use strict';

    angular
        .module('trucksApp')
        .controller('DeliveryDetailController', DeliveryDetailController);

    DeliveryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Delivery', 'Truck'];

    function DeliveryDetailController($scope, $rootScope, $stateParams, previousState, entity, Delivery, Truck) {
        var vm = this;

        vm.delivery = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trucksApp:deliveryUpdate', function(event, result) {
            vm.delivery = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
