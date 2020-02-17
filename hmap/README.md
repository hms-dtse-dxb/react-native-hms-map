# react-native-hms-maps [![npm version](https://img.shields.io/npm/v/react-native-hms-maps.svg?style=flat)](https://www.npmjs.com/package/react-native-hms-maps)

React Native HMS Map component for Android devices with Huawei mobile services (Huawei/Honor)

## Installation

See [Installation Instructions](docs/installation.md).

## Important!

This library is wrapper for Huawei's map, it will only work on android devices with Huawei mobile services, it does not contain all features and APIs of the official native JAVA SDK.

## Usage

```js
import MapView from 'react-native-hms-maps';
```

or

```js
var MapView = require('react-native-hms-maps');
```

This MapView component is built so that features on the map (such as Markers, Polygons, etc.) are
props of the map. This can be changed in future releases to provide an intuitive and react-like API.

### Rendering a Map with an initial region

```jsx
<HMSMap
  cameraOptions={{
    zoom: 5,
    latitude: 25,
    longitude: 55,
  }}
/>
```

zoom must an number (integer) in range [1,20],some zoom level:

```JS
const ZOOM_LEVELS = {
WORLD: 1,
CONTINENT: 5,
CITY: 10,
STREET: 15,
BUILDING: 20,
};
```

### listening to map press event

```jsx
onMapPress = (coordinate) => {
  console.log('Map tapped', coordinate);
};
<HMSMap onMapPress={this.onMarkerPress} />;
```

### listening to map long press event

```jsx
onMapLongPress = (coordinate) => {
  console.log('Map long pressed', coordinate);
};
<HMSMap onMapLongPress={this.onMapLongPress} />;
```

### Rendering a list of markers on a map

```jsx
<HMSMap cameraOptions={this.cameraOptions} markers={this.state.markers} />
```

or :

```jsx
renderMarkers = () => {
  this.refs.mapView.setMarkers(this.markers);
};

<HMSMap
  ref="mapView"
  cameraOptions={this.cameraOptions}
  onMapReady={this.renderMarkers}
/>;
```

### Rendering a Marker with a custom image

```jsx
defaultImage = '../assets/defaultPin.png';
this.markers =[
   {
      id:1,
      latitude:20,
      longitude:20,
      description:"this marker will be drawn with pin.png image"
      image: '../assets/specialPin.png'
   },  {
      id:1,
      latitude:20,
      longitude:20,
      description:"this marker will be shown with default image"
   }
]

<HMSMap
 cameraOptions={this.cameraOptions}
  markers={this.state.markers} />
```

### listening to marker press event

```jsx
onMarkerPress = (marker) => {
  console.log('Marker pressed!', marker);
};
<HMSMap markers={this.state.markers} onMarkerPress={this.onMarkerPress} />;
```

### animate marker to new location

```jsx
markers = [{
     {
      id:1,
      latitude:20,
      longitude:20,
   }
}];
newLatitude = 30;
newLongitude = 30;
zoomLevel = 8;

animatePressedMarker = marker =>{
  const [newLatitude , newLongitude ] = [this.randomLatitude(),this.randomLongitude()];
  //animate the marker on the map to the new position
  this.refs.hmsMap.animateMarkerToCoordinate(marker.id,this.newLatitude,this.newLongitude);

  //find pressed marker on the list
  const listItem = this.markers.find( m => m.id === marker.id );

  //update the marker with the new coordinates
  listItem.latitude = newLatitude;
  listItem.longitude = newLongitude;

  //you can move the camera to the new position of the marker
  this.refs.mapRef.animateCameraToCoordinate(newLat, newLng, zoomLevel;
}

 randomLatitude = () => {
  return (Math.random() * -90).toFixed(5) * 1;
}

 randomLongitude =()=> {
  return (Math.random() * -180).toFixed(5) * 1;
}

render() {
  return (
    <HMSMap ref='hmsMap' markers={this.markers} onMarkerPress={this.animatePressedMarker}  />;
  );
}
```

### Map's UI settings

```jsx
<HMSMap
  uiSettings={{
    zoomControls: true,
    myLocationButton: true,
    mapToolbar: true,
    compass: true,
    indoorLevelPicker: true,
  }}
/>
```

### HMSMap with current available features:

```jsx
onMapReady = () => {
  console.log('map is ready');
};
onMapPress = (coordinate) => {
  console.log('map is ready');
};
onMapLongPress = (coordinate) => {
  console.log('map is ready');
};
onMarkerPress = (marker) => {
  console.log('marker pressed');
};

<HMSMapView
  ref="mapView"
  // props
  uiSettings={{
    zoomControls: true,
    myLocationButton: true,
    mapToolbar: true,
    compass: true,
    indoorLevelPicker: true,
  }}
  cameraOptions={{
    zoom: 5,
    latitude: 25,
    longitude: 55,
  }}
  autoUpdateCamera={true}
  defaultMarkerImage={'assets/pin.png'}
  myLocationEnabled={true}
  markers={this.state.marker}
  onMapReady={this.onMapReady}
  onMapPress={this.onMapPress}
  onMarkerPress={this.onMarkerPress}
  onMapLongPress={this.onMapLongPress}
/>;
```

## Example:

To run example:

```bash
npm i
npm start
npm run android

```
