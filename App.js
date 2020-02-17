import React, { Component } from 'react';
import {
  Button,
  View,
  Text,
  StyleSheet,
  StatusBar,
  ToastAndroid,
} from 'react-native';

import HMSMap from 'hmap';
import { Colors } from 'react-native/Libraries/NewAppScreen';

const DEFAULT_IMAGE =
  'https://webstockreview.net/images250_/clipart-heart-map-4.png';
const MARKER_FROM_LONG_PRESS_IMAGE =
  'https://i.ya-webdesign.com/images/location-png-icon.png';

const ZOOM = {
  WORLD: 1,
  CONTINENT: 5,
  STATE: 8,
  CITY: 10,
  STREET: 15,
  BUILDING: 20,
};

export default class App extends Component {
  cameraOptions = {
    zoom: 5,
    latitude: 25,
    longitude: 55,
  };

  uiSettings = {
    zoomControls: true,
    myLocationButton: true,
    mapToolbar: true,
    compass: true,
    indoorLevelPicker: true,
  };
  selectedMarkerIndex;

  constructor(props) {
    super(props);
    this.state = {
      isReady: false,
      setMyLocationEnabled: false,
      markers: [],
      selectedMarker: {
        latitude: '0',
        longitude: '0',
        title: null,
        description: 'null',
      },
    };
  }

  /**
   * callback function gets called when the map is ready
   * you can call methods to show markers here
   */
  onMapReady = () => {
    console.log('map is ready');

    this.setState({
      isReady: true,
    });

    // this.mapRef.setMyLocationEnabled(true);initialMarker
    // this.mapRef.setZoomButtonsEnabled(true);
    // this.mapRef.setMyLocationButtonEnabled(true);
    // this.mapRef.setMarkersIcon(this.MARKER_ICON);
    // this.loadMarkers();
  };

  showMyLocation = () => {
    this.setState({
      setMyLocationEnabled: true,
    });
  };

  addMarkers = () => {
    this.refs.mapRef.setUiSettings(this.uiSettings);
    console.log(this.state.isReady);
    if (!this.state.isReady) {
      return;
    }
    const newMarkersCount = 5;
    let lastId = this.state.markers.length;
    let newMarkers = [...Array(newMarkersCount).keys()].map((m) => ({
      id: lastId,
      title: 'this is title ' + lastId,
      description: 'this marker was set from function map#addMarkers' + lastId++,
      latitude: randLat(),
      longitude: randLng(),
    }));

    this.refs.mapRef.addMarkers(newMarkers);

    newMarkers = [...this.state.markers, ...newMarkers];

    this.setState({
      markers: newMarkers,
    });

    // move camera to added markers
    this.refs.mapRef.animateCameraToRegion(
      newMarkers.map((m) => ({ latitude: m.latitude, longitude: m.longitude })),
      5,
    );

    //   this.setSelectedMarker(this.state.markers[0]);
  };

  next = () => {
    if (this.selectedMarkerIndex < this.state.markers.length - 1) {
      this.selectedMarkerIndex++;
    } else {
      this.selectedMarkerIndex = 0;
    }
    console.log('next', this.selectedMarkerIndex);

    const marker = this.state.markers[this.selectedMarkerIndex];
    this.setSelectedMarker(marker);
  };

  prev = () => {
    if (this.selectedMarkerIndex > 0) {
      this.selectedMarkerIndex--;
    } else {
      this.selectedMarkerIndex = this.state.markers.length - 1;
    }
    console.log('prev', this.selectedMarkerIndex);

    const marker = this.state.markers[this.selectedMarkerIndex];
    this.setSelectedMarker(marker);
  };

  onMarkerPress = (marker) => {
    console.log('marker pressed', marker.latitude, marker.longitude);
    //  this.selectedMarkerIndex = marker.id;
    this.setSelectedMarker(marker);
  };

  setSelectedMarker = (marker) => {
    this.setState({
      selectedMarker: marker,
    });

    this.refs.mapRef.animateCameraToCoordinate(
      marker.latitude,
      marker.longitude,
      ZOOM.STATE,
    );
  };

  onMapPress = () => {
    ToastAndroid.show(
      'long press on the map to create a marker',
      ToastAndroid.SHORT,
    );
  };

  onMapLongPress = (coordinates) => {
    console.log('longPress coords', coordinates);
    const markerFromLongPress = {
      id: this.state.markers.length,
      latitude: coordinates.latitude,
      longitude: coordinates.longitude,
      title: 'FromLongPress' + this.state.markers.length,
      description: 'marker generated from long press',
      image: MARKER_FROM_LONG_PRESS_IMAGE,
    };
    this.setState({
      markers: [...this.state.markers, markerFromLongPress],
    });
    this.refs.mapRef.addMarker(markerFromLongPress);
  };

  randomizeSelectedMarkerCoordinate = () => {
    if (this.state.selectedMarker.title === null) {
      return;
    }
    //update selectedMarker's coordinate
    const [newLat, newLng] = [randLat(), randLng()];

    let newMarkers = [...this.state.markers];
    const temp = {
      id: this.state.selectedMarker.id,
      latitude: newLat,
      longitude: newLng,
      title: this.state.selectedMarker.title,
      description: this.state.selectedMarker.description,
      image: this.state.selectedMarker.image,
    };

    newMarkers[this.state.selectedMarker.id] = temp;

    this.setState({
      markers: newMarkers,
      selectedMarker: temp,
    });

    //animate selectedMarker to the new coordinates
    this.refs.mapRef.animateMarkerToCoordinate(
      this.state.selectedMarker.id,
      newLat,
      newLng,
    );

    //move the camera the new coordinates when the animation ends
    setTimeout(() => {
      this.refs.mapRef.animateCameraToCoordinate(newLat, newLng, ZOOM.STATE);
    }, 500);
  };

  //remove all markers form the map
  clear = () => {
    this.refs.mapRef.clear();
    this.setState({
      markers: [],
    });
  };

  renderMap = () => {
    return (
      <HMSMap
        ref="mapRef"
        style={styles.map}
        // properties
        myLocationEnabled={this.state.setMyLocationEnabled}
        defaultMarkerImage={DEFAULT_IMAGE}
        uiSettings={this.uiSettings}
        cameraOptions={this.cameraOptions}
        autoUpdateCamera={false}
        //markers={this.state.markers}
        // events
        onMapReady={this.onMapReady}
        onMarkerPress={this.onMarkerPress}
        onMapPress={this.onMapPress}
        onMapLongPress={this.onMapLongPress}
      />
    );
  };

  renderButtons = () => {
    return (
      <View style={styles.buttonsWrapper}>
        <Button title="add markers" onPress={() => this.addMarkers()} />
        <Button title="clear" onPress={() => this.clear()} />
        <Button title="myLocation" onPress={() => this.showMyLocation()} />
      </View>
    );
  };

  renderMarkerNavigationButtons = () => {
    if (!this.state.selectedMarker || this.state.markers.length < 1) {
      return <></>; //hide nav buttons if less then 1 marker is present on the map
    }
    return (
      <View style={styles.nav}>
        <Button style={styles.navButton} title="↓" onPress={() => this.prev()} />
        {/* START: SPACER */}
        <View style={{ width: 10 }} />
        {/* END: SPACER */}
        <Button style={styles.navButton} title="↑" onPress={() => this.next()} />
      </View>
    );
  };

  renderMarkerDetails = () => {
    return (
      <View style={styles.sectionContainer}>
        <Text>
          Map state: {this.state.isReady ? 'READY' : 'NOT READY!'}, markers:
          {this.state.markers ? this.state.markers.length : 0}
        </Text>
        <Text style={styles.sectionTitle}>Selected Marker info: </Text>

        <Text style={styles.highlight}>
          title: {this.state.selectedMarker.title}
        </Text>
        <Text style={styles.highlight}>
          description: {this.state.selectedMarker.description}
        </Text>
        <View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
          <Text style={styles.highlight}>
            Position:( {this.state.selectedMarker.latitude} {' , '}
            {this.state.selectedMarker.longitude})
          </Text>
          <Button
            style={styles.random}
            title="randomize"
            onPress={() => this.randomizeSelectedMarkerCoordinate()}
          />
        </View>
      </View>
    );
  };

  render = () => {
    return (
      <>
        <StatusBar barStyle="dark-content" />
        <View style={styles.body}>
          {/* START: huawei MAP */}
          {this.renderMap()}
          {/* END: huawei Map */}
          {/* START: buttons */}
          {this.renderButtons()}
          {/* END: buttons */}
          {/* START: NAVIGATION BUTTONS */}
          {this.renderMarkerNavigationButtons()}
          {/* END: NAVIGATION BUTTONS */}
          {/* START: MARKER DETAILS */}
          {this.renderMarkerDetails()}
          {/* END: MARKER DETAILS */}
        </View>
      </>
    );
  };
}

/**
 * return a random latitude value near dubai
 */
function randLat() {
  return randomCoord(24, 25.5);
}

/**
 * returns a random longitude value near dubai
 */
function randLng() {
  return randomCoord(55, 56);
}

/**
 *  returns a random float value in a range
 * @param {int} from start of  range, lowerBound
 * @param {*} to end of range, upperBound
 */
function randomCoord(from, to) {
  return (Math.random() * (to - from) + from).toFixed(5) * 1;
  // .toFixed() returns string, so ' * 1' is a trick to convert to number
}

/**
 * styles for the views
 */
const styles = StyleSheet.create({
  map: {
    width: '100%',
    minHeight: '65%',
    borderWidth: 2,
  },
  scrollView: {
    paddingTop: 50,
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    borderWidth: 2,
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    paddingHorizontal: 8,
    height: '20%',
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  nav: {
    flexDirection: 'row',
    marginHorizontal: 12,
    marginTop: 10,
    alignSelf: 'flex-end',
    alignContent: 'center',
  },
  highlight: {
    flex: 1,
    fontWeight: '700',
    width: '100%',
  },
  buttonsWrapper: {
    flexDirection: 'row',
    alignItems: 'stretch',
    width: '100%',
    alignContent: 'center',
    justifyContent: 'space-around',
  },
});
