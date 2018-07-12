(function() {
    'use strict';

    angular
        .module('trucksApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('truck', {
            parent: 'entity',
            url: '/truck?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trucksApp.truck.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/truck/trucks.html',
                    controller: 'TruckController',
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
                    $translatePartialLoader.addPart('truck');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('truck-detail', {
            parent: 'truck',
            url: '/truck/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trucksApp.truck.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/truck/truck-detail.html',
                    controller: 'TruckDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('truck');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Truck', function($stateParams, Truck) {
                    return Truck.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'truck',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('truck-detail.edit', {
            parent: 'truck-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/truck/truck-dialog.html',
                    controller: 'TruckDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Truck', function(Truck) {
                            return Truck.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('truck.new', {
            parent: 'truck',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/truck/truck-dialog.html',
                    controller: 'TruckDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                driver: null,
                                registration: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('truck', null, { reload: 'truck' });
                }, function() {
                    $state.go('truck');
                });
            }]
        })
        .state('truck.edit', {
            parent: 'truck',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/truck/truck-dialog.html',
                    controller: 'TruckDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Truck', function(Truck) {
                            return Truck.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('truck', null, { reload: 'truck' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('truck.delete', {
            parent: 'truck',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/truck/truck-delete-dialog.html',
                    controller: 'TruckDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Truck', function(Truck) {
                            return Truck.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('truck', null, { reload: 'truck' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
