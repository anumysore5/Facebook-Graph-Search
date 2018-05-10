<?php

    if(isset($_GET['keyword']) and isset($_GET['type'])) {
        $keyword = urlencode($_GET['keyword']);
        $pageType = $_GET['type'];
        $centerValue = "";
        
        if($pageType == "place" and isset($_GET['latitude']) and isset($_GET['longitude'])) {
            $centerValue = $_GET['latitude'].",".$_GET['latitude'];

            $queryResult = "https://graph.facebook.com/v2.8/search?limit=10&q=".$keyword."&type=".$pageType."&amp;center=".$centerValue."&fields=id,name,picture.width(700).height(700)&access_token=EAAGg2LWlrb8BAEc6HWrdKly0pAtKc09YCwJPsqS9fC1NEKX7oFFDukANXTGcIANyTZBhMYPmTLCZAocJNpZAqnmZCNEMCRbIlETHqXh69SSTsXnpnwPOGAjUV30ZBOCj03pY1x5EqQZAYzntzfKsju6KEa04ZCEEbkZD";
        } else {
            $queryResult = "https://graph.facebook.com/v2.8/search?limit=10&q=".$keyword."&type=".$pageType."&fields=id,name,picture.width(700).height(700)&access_token=EAAGg2LWlrb8BAEc6HWrdKly0pAtKc09YCwJPsqS9fC1NEKX7oFFDukANXTGcIANyTZBhMYPmTLCZAocJNpZAqnmZCNEMCRbIlETHqXh69SSTsXnpnwPOGAjUV30ZBOCj03pY1x5EqQZAYzntzfKsju6KEa04ZCEEbkZD";
        }     
        
        $data = json_decode(file_get_contents($queryResult), true);

        echo json_encode($data);
    } 

    if(isset($_GET['nextPageUrl'])) {
        $queryResult = $_GET['nextPageUrl'];
        $nextPageData = json_decode(file_get_contents($queryResult), true);
        echo json_encode($nextPageData);
    }

    if(isset($_GET['previousPageUrl'])) {
        $queryResult = $_GET['previousPageUrl'];
        $nextPageData = json_decode(file_get_contents($queryResult), true);
        echo json_encode($nextPageData);
    }

    if(isset($_GET['rowDetails'])) {
        $queryResult = "https://graph.facebook.com/v2.8/".$_GET['rowDetails']."?fields=id,name,picture.width(700).height(700),albums.limit(5){name,photos.limit(2){name,picture}},posts.limit(5)&access_token=EAAGg2LWlrb8BAEc6HWrdKly0pAtKc09YCwJPsqS9fC1NEKX7oFFDukANXTGcIANyTZBhMYPmTLCZAocJNpZAqnmZCNEMCRbIlETHqXh69SSTsXnpnwPOGAjUV30ZBOCj03pY1x5EqQZAYzntzfKsju6KEa04ZCEEbkZD";
        
        $rowDetails = json_decode(file_get_contents($queryResult), true);
        echo json_encode($rowDetails);
    }

    if(isset($_GET['pictureId'])) {
        $x = $_GET['pictureId'];
        $queryResult = "https://graph.facebook.com/v2.8/".$x."/picture?redirect=false&access_token=EAAGg2LWlrb8BAEc6HWrdKly0pAtKc09YCwJPsqS9fC1NEKX7oFFDukANXTGcIANyTZBhMYPmTLCZAocJNpZAqnmZCNEMCRbIlETHqXh69SSTsXnpnwPOGAjUV30ZBOCj03pY1x5EqQZAYzntzfKsju6KEa04ZCEEbkZD";

        $hdImageUrl = json_decode(file_get_contents($queryResult), true);
        echo json_encode($hdImageUrl);
    }

?>