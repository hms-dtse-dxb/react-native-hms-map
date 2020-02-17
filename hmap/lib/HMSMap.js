import React, { Component } from 'react';
import { requireNativeComponent, UIManager, findNodeHandle } from 'react-native';
import PropTypes from 'prop-types';
import bridgeOps from './bridgeOps';

const HMSMapView = requireNativeComponent('HMSMapView', HMSMap, {
  nativeOnly: {
    onMapReady: true,
    onMapPress: true,
    onMapLongPress: true,
    onMarkerPress: true,
  },
});
// const HMSMapView = requireNativeComponent('HMSMapView');
//new
export default class HMSMap extends Component {
  constructor(props) {
    super(props);
    this.state = {};
    //this.COMMANDS = UIManager.HMSMapView.Commands;
    this.COMMANDS = {
      clear: 'clear',
      animateCameraToCoordinate: 'animateCameraToCoordinate',
      animateMarkerToCoordinate: 'animateMarkerToCoordinate',
      animateCameraToRegion: 'animateCameraToRegion',
    };
    //  console.log(UIManager.HMSMapView.Commands);
  }

  /** JAVA -> JAVASCRIPT */
  onMapReady = (ignore) => bridgeOps.onMapReady(this.props);

  onMapLongPress = (data) => bridgeOps.onMapLongPress(this.props, data);

  onMarkerPress = (data) => bridgeOps.onMarkerPress(this.props, data);

  onMapPress = (data) => bridgeOps.onMapPress(this.props, data);

  /** JAVASCRIPT -> JAVA */

  addMarker = (addMarker) => this.setState({ addMarker });

  addMarkers = (addMarkers) => this.setState({ addMarkers });

  setMarkers = (setMarkers) => this.setState({ setMarkers });

  setMyLocationEnabled = (myLocationEnabled) => this.setState({ myLocationEnabled });

  setAutoUpdateCamera = (autoUpdateCamera) => this.setState({ autoUpdateCamera });

  setCameraOptions = (cameraOptions) => this.setState({ cameraOptions });

  setUiSettings = (uiSettings) => this.setState({ uiSettings });

  setDefaultMarkerImage = (defaultMarkerImage) =>
    this.setState({ defaultMarkerImage });

  /** ****** commands ********* */
  clear() {
    console.log('clear map', this.COMMANDS, this.COMMANDS.clear);
    this.sendCommand(this.COMMANDS.clear);
  }

  animateCameraToCoordinate(latitude, longitude, zoom) {
    latitude = 0 + latitude;
    longitude = 0 + longitude;
    console.log('animate to coordinate');
    this.sendCommand2(this.COMMANDS.animateCameraToCoordinate, [
      { latitude, longitude },
      zoom,
    ]);
  }
  //array of coordinates [{latitude,longitude},{lat2,lng2}...];
  animateCameraToRegion(coordinates, zoom) {
    this.sendCommand2(this.COMMANDS.animateCameraToRegion, [coordinates, zoom]);
  }

  animateMarkerToCoordinate(markerId, latitude, longitude) {
    this.sendCommand2(this.COMMANDS.animateMarkerToCoordinate, [
      markerId,
      latitude,
      longitude,
    ]);
  }

  sendCommand(command) {
    this.sendCommand2(command, []);
  }

  sendCommand2(command, args) {
    console.log('sendCommand2:command:', command, 'args:', args);
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.refs.mapView),
      command,
      args,
    );
  }

  render() {
    return (
      <HMSMapView
        ref="mapView"
        // props
        // uiSettings={this.state.uiSettings}
        // cameraOptions={this.state.cameraOptions}
        // defaultMarkerImage={this.state.defaultMarkerImage}
        // myLocationEnabled={this.state.myLocationEnabled}
        // private
        addMarker={this.state.addMarker}
        addMarkers={this.state.addMarkers}
        setMarkers={this.state.setMarkers}
        // ****************
        {...this.props}
        // events
        onMapReady={this.onMapReady}
        onMapPress={this.onMapPress}
        onMarkerPress={this.onMarkerPress}
        onMapLongPress={this.onMapLongPress}
      />
    );
  }
}

HMSMap.propTypes = {
  defaultMarkerImage: PropTypes.string,
  cameraOptions: PropTypes.object,
  uiSettings: PropTypes.object,
  myLocationEnabled: PropTypes.bool,
  markers: PropTypes.array,
};
