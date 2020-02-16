# react-native-hms-map

## TODO:

- ADD SAFETY TESTs
- Wrappe more features
- Improve documentation
- Add support of children: ex: `<hmap> <marker/> <marker/> <circle/> </hmap>`
- Migrate to typescript

## Getting started

`$ npm install react-native-hms-map --save`

### Mostly automatic installation

`$ react-native link react-native-hms-map`

### HMS configuration

#### register the app

#### download json

#### add plugin

Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`

- Add `import com.reactlibrary.RNChromeHmsPackage;` to the imports at the top of the file
- Add `new RNChromeHmsPackage()` to the list returned by the `getPackages()` method

2. Append the following lines to `android/settings.gradle`:
   ```
   include ':react-native-hms-map'
   project(':react-native-hms-map').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-hms-map/android')
   ```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
   ```
     compile project(':react-native-hms-map')
   ```

## Usage

map : onMapReady, cameraConfig , uiOptions, mapPress, mapLongPress,
marker: add marker, remove marker, animateMarker, markerPress, markerLongPress, clearAll
this library does not wrap all of huawei map features, please do request any features of the native app that you want to see in this library, you can find the contact information in the package.json#author

zoom: 1: World 5: Landmass/continent 10: City 15: Streets 20: Buildings
