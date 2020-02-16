import { onMapReady, onMapLongPress, onMapPress } from './map';
import { onMarkerLongPress, onMarkerPress } from './marker';

const MAP_READY = 'MAP_READY';
const MARKER_CLICKED = 'MARKER_CLICKED';
const MAP_LONG_PRESS = 'MAP_LONG_PRESS';
const MAP_PRESSS = 'MAP_PRESS';

export default {
  onMapReady,
  onMarkerLongPress,
  onMarkerPress,
  onMapPress,
  onMapLongPress,
};

// export const onChange = (event, props) => {
//   const ops = {
//     MAP_READY: () => onMapReady(props),
//     MARKER_CLICKED: () => onMarkerPress(props, event),
//     MAP_LONG_PRESS: () => onMarkerLongPress(props, event),
//   };
//   const eventName = event.nativeEvent.eventName;
//   ops[eventName]();
// };

// export default {
//   onChange,
// };
