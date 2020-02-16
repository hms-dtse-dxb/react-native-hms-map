export const onMapReady = (props) => {
  if (!props.onMapReady) {
    return;
  }
  props.onMapReady();
};

export const onMapLongPress = (props, event) => {
  const data = event.nativeEvent;
  console.log('data', data);
  if (!props.onMapLongPress) {
    return;
  }
  console.log('props.onMapLP');
  props.onMapLongPress(data);
};

export const onMapPress = (props, event) => {
  const data = event.nativeEvent;
  console.log('data', data);
  if (!props.onMapPress) {
    return;
  }
  console.log('props.onMapP');
  props.onMapPress(data);
};
