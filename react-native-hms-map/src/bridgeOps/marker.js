export const onMarkerPress = (props, event) => {
  const data = event.nativeEvent;
  console.log(data);
  if (!props.onMarkerPress) {
    return;
  }
  const marker = {
    id: data.id,
    latitude: data.latitude,
    longitude: data.longitude,
    title: data.title,
    description: data.description,
  };
  props.onMarkerPress(marker);
};

export const onMarkerLongPress = (props, data) => {
  if (!props.onMarkerLongPress) {
    return;
  }
  const Coordinate = {
    latitude: data.latitude,
    longitude: data.longitude,
  };
  props.onMarkerLongPress(Coordinate);
};
