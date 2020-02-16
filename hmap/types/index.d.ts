 
 
interface LatLng{
    lat:number,
    lng:number
}

interface Marker{
    id:number,
    location:LatLng,
    title:string,
    snipper:string
} 

export declare function setMyLocationEnabled(value:boolean):void; 
export declare function animateCameraTo(marker:Marker):void;
export declare function animateToRegion(point1:LatLng , point2:LatLng):void; 
 
/* 
onMarkerClick,
onMapLongPress,
addMarkers,
addMarker,
removeMarker
 setZoomButtonsEnabled 
 setMyLocationEnabled 
 setMyLocationButtonEnabled 
setMarkerIcon
setZoom
clear

*/