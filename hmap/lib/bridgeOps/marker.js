export const onMarkerPress = (props, event) => {
  const data = event.nativeEvent;
  console.log(data);
  if (!props.onMarkerPress) {
    return;
  }
  const marker = {
    id: data.id,
    lat: data.lat,
    lng: data.lng,
    title: data.title,
    description: data.description,
  };
  props.onMarkerPress(marker);
};

export const onMarkerLongPress = (props, data) => {
  if (!props.onMarkerLongPress) {
    return;
  }
  const latlng = {
    lat: data.lat,
    lng: data.lng,
  };
  props.onMarkerLongPress(latlng);
};
