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

export default class HMSMap extends Component {
  /**
   * @param  props props from parent component
   */
  constructor(props) {
    super(props);
    this.state = {};

    this.COMMANDS = {
      clear: 'clear',
      animateCameraToCoordinate: 'animateCameraToCoordinate',
      animateMarkerToCoordinate: 'animateMarkerToCoordinate',
      animateCameraToRegion: 'animateCameraToRegion',
    };
  }

  /**
   * this method will be called when the map is drawn and ready to be interacted with, ex:adding markers
   */
  onMapReady = (ignore) => bridgeOps.onMapReady(this.props);
  /**
   * Will be called when the user presses (click/tap) on the map
   * @param coordinate the location {@link Coordinate} where the user pressed
   */
  onMapPress = (data) => bridgeOps.onMapPress(this.props, data);
  /**
   * Will be called when the user long presses (long click/long tap) on the map
   *  @param coordinate the location {@link Coordinate} where the user long pressed
   */
  onMapLongPress = (data) => bridgeOps.onMapLongPress(this.props, data);
  /**
   * Will be called when the user presses (click/tap) on a marker on the map
   * @param marker the marker that the users pressed
   */
  onMarkerPress = (data) => bridgeOps.onMarkerPress(this.props, data);

  /**
   * Add a list of markers to the existing set of markers on the map
   *  @param markers an array of markers {@link Marker} that will be added to the map
   */
  addMarker = (addMarker) => this.setState({ addMarker });

  addMarkers = (addMarkers) => this.setState({ addMarkers });
  /**
   * Add a list of markers to the map, after removing all the existing markers
   *  @param markers an array of markers {@see Marker}, that will be added to the map
   */
  setMarkers = (setMarkers) => this.setState({ setMarkers });
  /**
   * Shows the user's current location (position) on the map, represented by a blue dot
   * @param value true to show the user's location, false otherwise
   */
  setMyLocationEnabled = (myLocationEnabled) => this.setState({ myLocationEnabled });
  /**
   * Automatically moves the animates (moves) the camera to the newly added marker(s)
   * @param value true to show the user's location, false otherwise
   */
  setAutoUpdateCamera = (autoUpdateCamera) => this.setState({ autoUpdateCamera });
  /**
   * Set the initial camera options, including coordinates (location) and zoom
   * @param cameraOptions the object holding the camera options  {@see CameraOptions}
   */
  setCameraOptions = (cameraOptions) => this.setState({ cameraOptions });
  /**
   * Control the visibility of some of the map's UI (buttons and icons)
   * @param uiSettings the object holding the UI settings (flags) {@see UiSettings}
   */
  setUiSettings = (uiSettings) => this.setState({ uiSettings });
  /**
   * Sets the default marker's image (icon) that overrides the default one (TomTom black marker)
   * the marker should be used if the add marker(s) does not have an image (@see Marker#image)
   * using large images cal impact the performance
   * @param defaultMarkerImage the url of the image(icon) that will be the default marker image (icon)
   */
  setDefaultMarkerImage = (defaultMarkerImage) =>
    this.setState({ defaultMarkerImage });
  /**
   * Clear the map by removing all the graphical element (ex:markers)
   * in other word, reset the map to its initial state except for the camera's location
   */
  clear() {
    console.log('clear map', this.COMMANDS, this.COMMANDS.clear);
    this.sendCommand(this.COMMANDS.clear);
  }
  /**
   * Move the camera of the map to the specified location
   * @param latitude  latitude of the location where the camera will move
   * @param longitude  longitude of the location where the camera will move
   * @param zoom the zoom level of the camera ,must be in range [1,20]; ex:1 (world),10(city)
   */
  animateCameraToCoordinate(latitude, longitude, zoom) {
    latitude = 0 + latitude;
    longitude = 0 + longitude;
    console.log('animate to coordinate');
    this.sendCommand2(this.COMMANDS.animateCameraToCoordinate, [
      { latitude, longitude },
      zoom,
    ]);
  }
  /**
   * Move the camera of the map to a location where all the coordinates (or markers) are in the viewport (visible part of the map)
   * @param coordinates an array of coordinates or markers that will used to calculate the location of the camera
   * @param zoom the zoom level of the camera ,must be in range [1,20]; ex:1 (world),10(city)
   */
  animateCameraToRegion(coordinates, zoom) {
    this.sendCommand2(this.COMMANDS.animateCameraToRegion, [coordinates, zoom]);
  }
  /**
   * Animates (moves) a marker in the map from its coordinate (location) to a new coordinate (location)
   * @param markerId the id of the marker that will be animates (moved)
   * @param latitude new latitude of the coordinate (location) where the marker will move
   * @param longitude new longitude of the coordinate (location) where the marker will move
   */
  animateMarkerToCoordinate(markerId, latitude, longitude) {
    this.sendCommand2(this.COMMANDS.animateMarkerToCoordinate, [
      markerId,
      latitude,
      longitude,
    ]);
  }

  /**
   * Dispatch a command to the native side (java)
   * @param  command the id of the command
   */
  sendCommand(command) {
    this.sendCommand2(command, []);
  }

  /**
   * Dispatch a command to the native side (java) with a set of arguments
   * @param {*} command the id of the command
   * @param {*} args the set of arguments that will be passed along
   */
  sendCommand2(command, args) {
    console.log('sendCommand2:command:', command, 'args:', args);
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.refs.mapView),
      command,
      args,
    );
  }
  /**
   * rendering the HMSMapView native view, return from {@see HmsMapManager.java#createViewInstance}
   */
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
