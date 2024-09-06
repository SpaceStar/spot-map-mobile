import SwiftUI
import MapboxMaps
import ComposeApp

class MapState: ObservableObject {
    internal init(spots: [MapSpot], onScroll: @escaping (MapBounds, KotlinDouble) -> Void) {
        self.spots = spots
        self.onScroll = onScroll
    }
    
    @Published var spots: [MapSpot]
    @Published var onScroll: (MapBounds, KotlinDouble) -> Void
}

struct MapView: View {
    @ObservedObject var state: MapState
    
    var body: some View {
        let center = CLLocationCoordinate2D(latitude: 39.5, longitude: -98.0)
        Map(initialViewport: .camera(center: center, zoom: 2, bearing: 0, pitch: 0))
    }
}

#Preview {
    MapView(state: MapState(spots: [], onScroll: { _, _ in }))
}

class MapViewController: MapMapViewController {
    func invoke(initialEvent: MapMapUpdateEvent, updateListener: Core_uiComposeUpdateListener<MapMapUpdateEvent>) -> UIViewController {
        let state = MapState(spots: initialEvent.spots, onScroll: initialEvent.onScroll)
        let mapView = MapView(state: state)
        updateListener.listen { mapUpdateEvent in
            state.spots = mapUpdateEvent!.spots
            state.onScroll = mapUpdateEvent!.onScroll
        }
        return UIHostingController(rootView: mapView)
    }
}
