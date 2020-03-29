
# react-native-hms-map [![npm version](https://img.shields.io/npm/v/react-native-hms-map.svg?style=flat)](https://www.npmjs.com/package/react-native-hms-map)

React Native HMS Map component for Android devices with Huawei mobile services (Huawei/Honor)

# THIS LIBRARY IS DEPRECATED AND IT IS NO MORE UNDER MAINTAINANCE
**THE OFFICIAL PLUGIIN FROM HUAWEI:** [https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/hms-map-v4-createamap-rn](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/hms-map-v4-createamap-rn)

## Configuration

**WARNING**: Before you can start using the HMS Platform APIs and SDKs, you must register your app in the AppGallery and configure your project, this step cannot be skipped.

See [configuration Instructions](https://github.com/hms-dtse-dxb/react-native-hms-map/blob/master/CONFIGURATION.md).

## Important

This library is wrapper for Huawei's map, it will only work on android devices with Huawei mobile services, it does not contain all features and APIs of the official native JAVA SDK. it can be used but it's still under development.

## Installation

After [configuration](https://github.com/hms-dtse-dxb/react-native-hms-map/blob/master/CONFIGURATION.md), install the library from npm:

```sh
npm i react-native-hms-map --save
```

The library ships with platform native code that needs to be compiled
together with React Native. This requires you to configure your build
tools.

```sh
react-native link
```

> if your min sdk version is less then 19 (can be found in `android/build.gradle#buildscript.minSdkVersion`):
> either: increase it to 19,
> or: override the library settings (NOT RECOMMENDED)
> go to `android/app/src/main/AndroidManifest.xml`
> add the following line to root tag:
>
> ```xml
> <manifest
> [...]
> xmlns:tools="http://schemas.android.com/tools">
>
> ```
>
> and in Application tag, add the following to override the library:
>
> ```xml
>  <application
>    [...]
>    tools:overrideLibrary="com.reactlibrary"
>  >
> ```

## Usage

```js
import HMSMap from 'react-native-hms-map';
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

zoom must be a number (integer), in range [1,20], example of zoom levels:

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
  console.log('Map pressed', coordinate);
};
render(){
  return (
    <HMSMap onMapPress={this.onMapPress} />;
  );
}
```

### listening to map long press event

```jsx
onMapLongPress = (coordinate) => {
  console.log('Map long pressed', coordinate);
};
render(){
  return (
    <HMSMap onMapLongPress={this.onMapLongPress} />;
  );
}
```

### Rendering a list of markers on a map

```jsx
markers = [
  {
    id: 1,
    latitude: 25.5,
    longitude: 55,
  },
  {
    id: 2,
    latitude: 50,
    longitude: -20,
  },
];
render(){
  return (
    <HMSMap markers={this.markers} />;
  );
}
```

or :

```jsx
markers = [
  {
    id: 1,
    latitude: 25.5,
    longitude: 55,
  },
  {
    id: 2,
    latitude: 50.1243,
    longitude: -20,
  },
];
renderMarkers = () => {
  this.refs.mapView.setMarkers(this.markers);
};
render(){
  return (
    <HMSMap
      ref="mapView"
      onMapReady={this.renderMarkers}
    />;
  );
}

```

### Rendering a Marker with a custom image

```jsx
defaultImage = '../assets/defaultPin.png';
markers =[
   {
      id:1,
      latitude:30,
      longitude:-42.32,
      description:"this marker will be drawn with a custom image",
      image: '../assets/specialPin.png'
   },  {
      id:2,
      latitude:40,
      longitude:34.3214,
      description:"this marker will be shown with default image"
   }
];
render() {
  return (
      <HMSMap
        defaultMarkerImage={this.defaultImage}
        markers={this.state.markers}
       />
   );
}
```

### listening to marker press event

```jsx
onMarkerPress = (marker) => {
  console.log('Marker pressed!', marker);
};
render() {
  return (
    <HMSMap markers={this.state.markers} onMarkerPress={this.onMarkerPress} />;
  );
}
```

### animate marker to new location

```jsx
markers = [
   {
      id: 1,
    latitude: 25.5,
    longitude: 55,
  },
  {
    id: 2,
    latitude: 50,
    longitude: -20,
   }
];

zoomLevel = 8;

animatePressedMarker = marker =>{
  const [newLatitude , newLongitude ] = [this.randomLatitude(),this.randomLongitude()];
  //animate the marker on the map to the new position
  this.refs.mapView.animateMarkerToCoordinate(marker.id,this.newLatitude,this.newLongitude);

  //find pressed marker on the list
  const listItem = this.markers.find( m => m.id === marker.id );

  //update the marker with the new coordinates
  listItem.latitude = newLatitude;
  listItem.longitude = newLongitude;

  //you can move the camera to the new position of the marker
  this.refs.mapView.animateCameraToCoordinate(newLat, newLng, zoomLevel);
}

 randomLatitude = () => {
  return (Math.random() * -90).toFixed(5) * 1;
}

 randomLongitude =()=> {
  return (Math.random() * -180).toFixed(5) * 1;
}

render() {
  return (
    <HMSMap ref='mapView' markers={this.markers} onMarkerPress={this.animatePressedMarker}  />;
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

### HMSMap with current available features

```jsx
onMapReady = () => {
  console.log('map is ready');
};
onMapPress = (coordinate) => {
  console.log('map tapped');
};
onMapLongPress = (coordinate) => {
  console.log('map long pressed ');
};
onMarkerPress = (marker) => {
  console.log('marker pressed');
};

render() {
  return (
    <HMSMap
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
  );
}

```

## Contribution
all kind of contributions are welcome :)
  
## Example

To run example:

```bash
npm i
npm start
npm run android

```
