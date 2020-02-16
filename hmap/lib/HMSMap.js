import React, { Component } from 'react';
import {
  requireNativeComponent,
  Text,
  View,
  UIManager,
  findNodeHandle,
} from 'react-native';
import PropTypes from 'prop-types';
import bridgeOps from './bridgeOps';

const HMSMapView = requireNativeComponent('HMSMapView', HMSMap, {
  nativeOnly: {
    onMapReady: true,
    onMapPress: true,
    onMapLongPress: true,
    onMarkerPress: true,
    onMarkerLongPress: true,
  },
});
// const HMSMapView = requireNativeComponent('HMSMapView');
//new
export default class HMSMap extends Component {
  r;
  constructor(props) {
    super(props);
    this.state = {};
    //this.COMMANDS = UIManager.HMSMapView.Commands;
    this.COMMANDS = {
      clear: 'clear',
      animateToCoordinate: 'animateToCoordinate',
      animateMarkerToCoordinate: 'animateMarkerToCoordinate',
      animateToRegion: 'animateToRegion',
    };
    //  console.log(UIManager.HMSMapView.Commands);
  }

  /** JAVA -> JAVASCRIPT */
  onMapReady = (ignore) => bridgeOps.onMapReady(this.props);

  onMapLongPress = (data) => bridgeOps.onMapLongPress(this.props, data);

  onMarkerPress = (data) => bridgeOps.onMarkerPress(this.props, data);

  onMapPress = (data) => bridgeOps.onMapPress(this.props, data);

  onMarkerLongPress = (data) =>
    bridgeOps.onMarkerLongPress(this.props.onMarkerLongPress, data);

  /** JAVASCRIPT -> JAVA */

  addMarker = (addMarker) => this.setState({ addMarker });

  addMarkers = (addMarkers) => this.setState({ addMarkers });

  setMarkers = (setMarkers) => this.setState({ setMarkers });

  setMyLocationEnabled = (myLocationEnabled) => this.setState({ myLocationEnabled });

  setCameraOptions = (cameraOptions) => this.setState({ cameraOptions });

  setUiSettings = (uiSettings) => this.setState({ uiSettings });

  setDefaultMarkerImage = (defaultMarkerImage) =>
    this.setState({ defaultMarkerImage });

  /** ****** commands ********* */
  clear() {
    console.log('clear map', this.COMMANDS, this.COMMANDS.clear);
    this.sendCommand(this.COMMANDS.clear);
  }

  animateToCoordinate(lat, lng, zoom) {
    lat = +lat;
    lng = +lng;
    console.log('animate to coordinate');
    this.sendCommand2(this.COMMANDS.animateToCoordinate, [{ lat, lng }, zoom]);
  }
  //array of coordinates [{lat,lng},{lat2,lng2}...];
  animateToRegion(coordinates, zoom) {
    this.sendCommand2(this.COMMANDS.animateToRegion, [coordinates, zoom]);
  }

  animateMarkerToCoordinate(markerId, lat, lng) {
    this.sendCommand2(this.COMMANDS.animateMarkerToCoordinate, [markerId, lat, lng]);
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
        onMarkerLongPress={this.onMarkerLongPress}
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
