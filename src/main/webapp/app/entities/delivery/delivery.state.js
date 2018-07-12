(function() {
    'use strict';

    angular
        .module('trucksApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('delivery', {
            parent: 'entity',
            url: '/delivery?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trucksApp.delivery.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/delivery/deliveries.html',
                    controller: 'DeliveryController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('delivery');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('delivery-detail', {
            parent: 'delivery',
            url: '/delivery/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trucksApp.delivery.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/delivery/delivery-detail.html',
                    controller: 'DeliveryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('delivery');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Delivery', function($stateParams, Delivery) {
                    return Delivery.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'delivery',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('delivery-detail.edit', {
            parent: 'delivery-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delivery/delivery-dialog.html',
                    controller: 'DeliveryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Delivery', function(Delivery) {
                            return Delivery.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('delivery.new', {
            parent: 'delivery',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delivery/delivery-dialog.html',
                    controller: 'DeliveryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateAdded: null,
                                size: null,
                                weight: null,
                                pickupLocation: null,
                                deliveryLocation: null,
                                pickupDescription: null,
                                deliveryDescription: null,
                                status: null,
                                expectedIncome: null,
                                pickupDate: null,
                                deliveryDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('delivery', null, { reload: 'delivery' });
                }, function() {
                    $state.go('delivery');
                });
            }]
        })
        .state('delivery.edit', {
            parent: 'delivery',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delivery/delivery-dialog.html',
                    controller: 'DeliveryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Delivery', function(Delivery) {
                            return Delivery.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('delivery', null, { reload: 'delivery' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('delivery.delete', {
            parent: 'delivery',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delivery/delivery-delete-dialog.html',
                    controller: 'DeliveryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Delivery', function(Delivery) {
                            return Delivery.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('delivery', null, { reload: 'delivery' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
