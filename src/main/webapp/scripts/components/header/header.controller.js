'use strict';

angular.module('mydreamplusApp')
    .controller('HeaderController', function($rootScope, $scope, $location, $state, ENV, Auth, Principal, FormLoginEnabledUsers, AuthObserver,Images) {
        function resolveAccount(){
            Principal.identity().then(function(account) {
                if (account !== undefined && account !== null) {
                    onAccountLoaded(account);
                } else {
                    clearAccount();
                }
            });
        }

        function onAccountLoaded(account){
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            $scope.logout = function() {
                Auth.logout();

                if (FormLoginEnabledUsers.contains(account.login)) {
                    $state.go('login-by-form');
                } else {
                    $state.go('login-by-qrcode');
                }
            };

            $scope.editMember = function() {
                $state.go('organizations.members.edit', {
                    organizationId: account.organizationId,
                    id: account.id,
                    personalInfo: true
                });
            };

            if ($scope.account !== undefined && $scope.account !== null && $scope.account.avatar === null) {
                $scope.account.avatar =Images.getDefaultUserAvatarImageData();
            }
        }

        function clearAccount(){
            $scope.account = null;
            $scope.isAuthenticated = angular.noop;
        }

        AuthObserver.onLogin(resolveAccount);
        AuthObserver.onLogout(clearAccount);

        resolveAccount();
    });
