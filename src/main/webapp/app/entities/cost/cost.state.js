(function() {
    'use strict';

    angular
        .module('trucksApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cost', {
            parent: 'entity',
            url: '/cost?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trucksApp.cost.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cost/costs.html',
                    controller: 'CostController',
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
                    $translatePartialLoader.addPart('cost');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cost-detail', {
            parent: 'cost',
            url: '/cost/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trucksApp.cost.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cost/cost-detail.html',
                    controller: 'CostDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cost');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cost', function($stateParams, Cost) {
                    return Cost.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cost',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cost-detail.edit', {
            parent: 'cost-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost/cost-dialog.html',
                    controller: 'CostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cost', function(Cost) {
                            return Cost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cost.new', {
            parent: 'cost',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost/cost-dialog.html',
                    controller: 'CostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                location: null,
                                time: null,
                                amount: null,
                                type: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cost', null, { reload: 'cost' });
                }, function() {
                    $state.go('cost');
                });
            }]
        })
        .state('cost.edit', {
            parent: 'cost',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost/cost-dialog.html',
                    controller: 'CostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cost', function(Cost) {
                            return Cost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cost', null, { reload: 'cost' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cost.delete', {
            parent: 'cost',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cost/cost-delete-dialog.html',
                    controller: 'CostDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cost', function(Cost) {
                            return Cost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cost', null, { reload: 'cost' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
