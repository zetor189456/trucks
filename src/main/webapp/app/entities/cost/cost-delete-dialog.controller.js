(function() {
    'use strict';

    angular
        .module('trucksApp')
        .controller('CostDeleteController',CostDeleteController);

    CostDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cost'];

    function CostDeleteController($uibModalInstance, entity, Cost) {
        var vm = this;

        vm.cost = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cost.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
