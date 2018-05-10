// Initializing the javascript fb sdk
window.fbAsyncInit = function() {
    FB.init({ 
      appId: '458327597624767',
      status: true, 
      cookie: true, 
      xfbml: true,
      version: 'v2.8'
    });
};

(function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = "//connect.facebook.net/en_US/sdk.js";
     fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
    
// defining the controller for the html
var myApp = angular.module('myApp', ['ngAnimate']);
myApp.controller('myCtrl', function($scope, $http) { 

    // Display items which have already been set as "favorite"
    $scope.init = function () {
        $scope.favorites = [];
        $scope.typeOfFav = [];
        $scope.isFavorite = [];
        $scope.posts = [];
        $scope.detailsPageStarBtn;
        $scope.dateTime = [];
        $scope.profilePic = [];
        $scope.name = [];
        
        navigator.geolocation.getCurrentPosition(function(position) { 
           $scope.lat = position.coords.latitude;
           $scope.long = position.coords.longitude;
        });

        if (window.localStorage.length != 0 ) {
            //get an existing object
            for ( var i = 0, len = window.localStorage.length; i < len; ++i ) {
                if(localStorage.key( i ).startsWith("fav")) {
                    var btnId = localStorage.key( i ).substring(5);
                    $scope.favorites[i] = JSON.parse(localStorage.getItem( localStorage.key( i ) ));
                    var prefix = localStorage.key( i ).substring(3,5);
                    $scope.typeOfFav[i] = $scope.getTypeFromPrefix(prefix);
                    $scope.isFavorite[btnId] = true;
                }                        
            }
        } 
    }
            
    $scope.getTypeFromPrefix = function(prefix) {
        var type;
        switch(prefix) {
            case 'us' : type = "users"; 
                        break;
            case 'pa' : type = "pages"; 
                        break;
            case 'ev' : type = "events"; 
                        break;
            case 'pl' : type = "places"; 
                        break;
            case 'gr' : type = "groups"; 
                        break;
        }
        return type;
    }
            
    $scope.init();
            
    $scope.onSubmit = function(type) {
        $scope.myValue = false;
        if(angular.isUndefined($scope.searchKeyword) || $scope.searchKeyword.trim() == "") {

            alert("Please type a keyword");
            $scope.favoriteTable = false;

        } else {
            $scope.favoriteTable = false;
            $scope.pageLoading = true;
            $scope.resultTable = false;
            $scope.nextPrevBtns = false;
            $scope.detailsPage = false;

            if(type == 'place') {
               $http.get("/index.php?keyword=" + $scope.searchKeyword +"&type=" + type + "&latitude=" + $scope.lat + "&longitude=" + $scope.long)
                    .then(function successCallBack(response) {
                        $scope.displayResults(response);
                    },function errorCallBack(response) {
                        alert("Please type a keyword");
                    });
            } 

            else {
                $http.get("/index.php?keyword=" + $scope.searchKeyword +"&type=" + type)
                    .then(function successCallBack(response) {
                        $scope.displayResults(response);
                    }, function errorCallBack(response) {
                        alert("Please type a keyword");
                    });
            }               
        }
    }
    
    // formulate data to be shown in the resultTable page
    $scope.displayResults = function(response) {
        $scope.objects = response.data.data;

        $scope.pageLoading = false;
        $scope.resultTable = true;

        $scope.paging = response.data.paging;

        if(angular.isDefined($scope.paging)) {
            $scope.nextPageUrl = $scope.paging.next;
            $scope.previousPageUrl = $scope.paging.previous;
            $scope.displayNextPrevious();
        }
    }
            
    // function invoked on click of "Next" button
    $scope.onNext = function() {
        var url = {
            params: {
                nextPageUrl: $scope.nextPageUrl
            }
        }

        $http.get("/index.php", url).then(function (response) {
            $scope.objects = response.data.data; 
            $scope.setNextAndPreviousPageUrl(response.data);          
        }), function(response) {
            alert("Failed response");   
        }
    }
        
    // function invoked on click of "Previous" button
    $scope.onPrevious = function() {   
        var url = {
            params: {
                previousPageUrl: $scope.previousPageUrl
            }
        }
        $http.get("/index.php", url).then(function (response) {
            $scope.objects = response.data.data; 
            $scope.setNextAndPreviousPageUrl(response.data);   
        }), function(response) {
            alert("Failed response");   
        }
    }
    
    // update next page url and previous page url
    $scope.setNextAndPreviousPageUrl = function(responseData) {
        if(angular.isDefined(responseData.paging)) {
            $scope.nextPageUrl = responseData.paging.next;
            $scope.previousPageUrl = responseData.paging.previous;                    
            $scope.displayNextPrevious();
        }
    }
            
    // function to decide if Next/Previous button(s) should be displayed or not
    $scope.displayNextPrevious = function() {
        $scope.nextPrevBtns = true;

        // if there is previous page, but no next page
        if(angular.isDefined($scope.previousPageUrl) && angular.isUndefined($scope.nextPageUrl)) {                   
            $scope.nextButton = false;
            $scope.previousButton = true;
        } 
        // if next page exits, but no previous page
        else if(angular.isDefined($scope.nextPageUrl) && angular.isUndefined($scope.previousPageUrl)) {            
            $scope.nextButton = true;
            $scope.previousButton = false;
        } 
        // if both next and previous pages exist
        else if(angular.isDefined($scope.nextPageUrl) && angular.isDefined($scope.previousPageUrl)){
            $scope.nextButton = true;
            $scope.previousButton = true;
        }
    }
       
    // determine if star button is favorite or not in the details page
    $scope.determineFavOrNot = function(rowId) {
        // check if the corresponding row is in the localstorage
        if(window.localStorage.length != 0) {
            for(var i=0; i<window.localStorage.length; i++){
                if(localStorage.key( i ).startsWith("fav")) {
                    var btnId = localStorage.key( i ).substring(5);
                    // if it is in local storage, means it is favorite
                    if(btnId == rowId) {
                       $scope.detailsPageStarBtn = "fav"+rowId;
                        break;
                    } else {
                        $scope.detailsPageStarBtn = "nfav"+rowId;
                    }
                }
            }
        } else {
            $scope.detailsPageStarBtn = "nfav"+rowId;
        }
        
    }
            
    $scope.showAlbumsAndPosts = function(event) {
        $scope.detailsPage = true;
        $scope.albumsAndPosts = true;
        $scope.buttonsInDetailsPage = true;
        $scope.showAlbumsProgressBar = true;
        $scope.showPostsProgressBar = true;
        $scope.showPostsData = false;
        $scope.showAlbumsData = false;   
        $scope.favoriteTable = false;
        $scope.pageLoading = false;
        $scope.resultTable = false;
        $scope.nextPrevBtns = false;

        $scope.rowId = event.currentTarget.id.substring(1);
        $scope.determineFavOrNot($scope.rowId);        
        
        var url = {
            params: {
                rowDetails: $scope.rowId
            }
        }
        $http.get("/index.php", url).then(function (response) {
            if(angular.isDefined(response.data.albums)) {       // If there are albums                
                $scope.albumData = response.data.albums.data;
                $scope.setAlbumData();
            } else {
                $scope.arr = [];
            }
            
            if(angular.isDefined(response.data.posts)) {        // If there are posts  
                $scope.postsData = response.data.posts.data;
                $scope.setPostsData($scope.rowId);              
            } else {
                $scope.posts = [];
            }
            
            $scope.setFbPicture($scope.rowId);
            
            $scope.showAlbumsProgressBar = false;
            $scope.showAlbumsData = true;  
            $scope.showPostsProgressBar = false;
            $scope.showPostsData = true;

        });
    }
    
    $scope.setFbPicture = function(rowid) {
        $scope.fbCaption = "";
        $scope.fbPicture;
        if(angular.isDefined($scope.objects)) {
            for(var i=0; i<$scope.objects.length; i++) {
                if($scope.objects[i].id == rowid) {
                    $scope.fbPicture = $scope.objects[i].picture.data.url;
                    $scope.fbCaption = $scope.objects[i].name;
                    break;
                }
            }
        } 
        else if($scope.selectedTab == "favorites" && (window.localStorage.length != 0)) {
            for(var i=0; i<window.localStorage.length; i++){
                if(localStorage.key( i ).startsWith("fav")) {
                    var btnId = localStorage.key( i ).substring(5);
                    var object = JSON.parse(localStorage.getItem(localStorage.key( i )));
                    if(btnId == rowid) {
                        $scope.fbPicture = object.picture.data.url;
                        $scope.fbCaption = object.name;
                        break;
                    } 
                }
            }
        }
    }

    $scope.setAlbumData = function() {
        $scope.pics;
        if($scope.albumData.length > 5) {
            $scope.numOfAlbums = 5;
        } else {
            $scope.numOfAlbums = $scope.albumData.length;
        }
            
        $scope.arr = []; 
        $scope.albName = [];
        for (var i = 0; i < $scope.numOfAlbums; i++) {
            $scope.arr[i] = [];
        }
        
        for (var j = 0; j < $scope.numOfAlbums; j++) {
            if(angular.isDefined($scope.albumData[j])) {
                $scope.albName[j] = $scope.albumData[j].name;
                
                if(angular.isDefined($scope.albumData[j].photos)) {
                    $scope.pics = $scope.albumData[j].photos.data;
                    
                    if($scope.pics.length > 2) {
                        $scope.numOfPicsPerAlbum = 2;
                    } else {
                        $scope.numOfPicsPerAlbum = $scope.pics.length;
                    }

                    for (var k = 0; k < $scope.numOfPicsPerAlbum; k++) {                     
                        $scope.fillarray(j, k);
                    }
                }  
            }
        }
    }

    $scope.fillarray = function(j, k) {
        var url = {
            params: {                            
                pictureId : $scope.albumData[j].photos.data[k].id                          
            }
        }                
        $http.get("/index.php", url).then(function (response) {
            $scope.hdImageUrl = response.data.data.url;
            $scope.arr[j][k] = $scope.hdImageUrl;
        });
    }
    
    $scope.setPostsData = function(rowId) {
        var len = $scope.postsData.length;
        if($scope.postsData.length > 5) {
            len = 5;
        }

        for(var i=0; i<len; i++) {
            $scope.dateTime[i] = [];
        }  

        var objectName;
        var objectProfilePic;
        
        // if "details" button is clicked when the curret active tab is "favorites"
        if($scope.selectedTab == "favorites") {
            for(var i=0; i<window.localStorage.length; i++){
                var btnId = localStorage.key( i ).substring(5);
                if(btnId == rowId) {
                    var currentObject = JSON.parse(localStorage.getItem(localStorage.key( i )));
                    objectName = currentObject.name;
                    objectProfilePic = currentObject.picture.data.url;
                    break;
                }
            }
        }
        // if "details" is clicked when on any other tab other than "favorites"
        else {
            for(var i=0; i<$scope.objects.length; i++) {
                if($scope.objects[i].id == rowId) {
                    objectName = $scope.objects[i].name;
                    objectProfilePic = $scope.objects[i].picture.data.url;
                }
            }
        }        

        for(var i=0; i<len; i++) {
            $scope.posts[i] = $scope.postsData[i].message;
            var unDateTime = $scope.postsData[i].created_time; 
            $scope.dateTime[i][0] = unDateTime.split('T')[0];
            $scope.dateTime[i][1] = unDateTime.split('T')[1].split('+')[0];

            $scope.profilePic[i] = objectProfilePic;
            $scope.name[i] = objectName;
        } 
    }
      
    // function called on the click of "clear" button
    $scope.clearResults =  function() {
        if(angular.isDefined($scope.searchKeyword)) {
            $scope.searchKeyword = "";
        }
        $scope.resultTable = false;
        $scope.detailsPage = false;                    
        $scope.nextPrevBtns = false;
        if($scope.selectedTab == "favorites") {                    
            $scope.favoriteTable = false;
        } 
        $scope.selectedTab = "users";
    }
           
    // function to change color, store in localstorage, store in local data structures on the click of the "star" button
    $scope.changeColor = function(event) {
        $scope.btnColor = event.currentTarget.style.color;
        var btnId = event.currentTarget.id;
        var storeLocally = false;

        // with the current click, it has to be made favorite
        if(btnId.startsWith("n")) {
            $scope.detailsPageStarBtn = btnId.substring(1);
            btnId = event.currentTarget.id.substring(4);
            storeLocally = true;
        }

        // with the current click, it has to be removed from favorites
        else if(btnId.startsWith("f")) {
            $scope.detailsPageStarBtn = "nfav"+btnId.substring(4);
            btnId = event.currentTarget.id.substring(3);
            storeLocally = false;
        }

        // This refers to the button on the main page
        else {
            if($scope.btnColor == "black") {
                event.currentTarget.style.color = "yellow";
                storeLocally = true;                        
            } else if($scope.btnColor == "yellow") {
                event.currentTarget.style.color = "black";
                storeLocally = false;                        
            }
        }

        $scope.makeFavorite(storeLocally, btnId);
    }

    $scope.makeFavorite = function(storeLocally, id) {
        $scope.objToStore;
        var prefix = $scope.getObjectType();
        if(storeLocally) {
            if (typeof(Storage) !== "undefined") {
                for(var i=0; i<$scope.objects.length; i++) {
                    if($scope.objects[i].id == id) {
                        $scope.objToStore = $scope.objects[i];
                        $scope.favorites.push($scope.objToStore);
                        $scope.typeOfFav.push($scope.selectedTab);
                        localStorage.setItem("fav"+prefix+id, JSON.stringify($scope.objToStore));
                        break;
                    }
                }                        
            }
        } else {
            // remove the item from array as well as local storage
            localStorage.removeItem("fav"+prefix+id);
            for(var i=0; i<$scope.favorites.length; i++) {
                if($scope.favorites[i].id == id) {
                    $scope.favorites.splice(i,1);
                    $scope.typeOfFav.splice(i,1);
                }
            }
        }
        $scope.isFavorite[id] = storeLocally;
    }
            
    $scope.getObjectType = function() {
        var objType;
        switch($scope.selectedTab) {
            case "users" : objType = "us"; 
                           break;
            case "pages" : objType = "pa"; 
                           break;
            case "events" : objType = "ev"; 
                           break;
            case "places" : objType = "pl"; 
                           break;
            case "groups" : objType = "gr"; 
                           break;
        }
        return objType;
    }
      
    // function called on the click of "favorites" tab
    $scope.displayFavorites = function(){
            $scope.selectedTab = "favorites";

            $scope.resultTable = false;
            $scope.nextPrevBtns = false;
            $scope.detailsPage = false;

            if($scope.favorites.length == 0) {
                $scope.favoriteTable = false;
            } else {
                $scope.favoriteTable = true;
            }
    }
          
    // function invoked on the click of "delete" button in favorites tab
    $scope.deleteFav = function(event) {
        var objIdToRemove = event.currentTarget.id;
        for(var i=0; i<$scope.favorites.length; i++){
            if($scope.favorites[i].id == objIdToRemove) {
                var prefix = $scope.typeOfFav[i].substring(0,2);
                $scope.favorites.splice(i,1);
                $scope.typeOfFav.splice(i,1);
                localStorage.removeItem("fav"+prefix+objIdToRemove);
                $scope.isFavorite[objIdToRemove] = false;
                break;
            }
        }

        // Do not show the table, once the last element is deleted
        if($scope.favorites.length == 0) {
            $scope.favoriteTable = false;
        }
    }
        
    // function invoked on the click of "back" button in details page
    $scope.goToMainPage = function() {
        // if 'favorites' tab selected, then display favorite table
        if($scope.selectedTab == "favorites") {
            $scope.favoriteTable = true;
        } else {
            $scope.resultTable = true;
            $scope.nextPrevBtns = true;
        }

        $scope.detailsPage = false;
    }
    
    // function invoked to show the feed dialog box on the click of "fb" button
    $scope.showFbFeedDialog = function() {
        FB.ui({
            method: 'share',
            link: 'window.location.href',
            picture: $scope.fbPicture,
            name: $scope.fbCaption,
            href: 'https://developers.facebook.com/docs/',
            caption: 'Posting to FB timeline'
        }, function(response){
            if (response && !response.error_message) {
                alert("Posted Successfully");
            } else {
                alert("Not posted");
            }

        });
     }

});