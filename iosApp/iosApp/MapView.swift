import SwiftUI
import BigNumber
import MapboxMaps
import ComposeApp

private class MapState: ObservableObject {
    internal init(spots: [MapSpot]) {
        self.spots = spots
    }
    
    @Published var spots: [MapSpot]
}

private struct BoundsZoom {
    let bounds: MapBounds
    let zoom: Double
}

private class BoundsObservable: ObservableObject {
    internal init(boundsZoom: BoundsZoom?) {
        self.boundsZoom = boundsZoom
    }
    
    @Published var boundsZoom: BoundsZoom?
}

struct MapView: View {
    @StateObject fileprivate var state: MapState
    fileprivate var bounds: BoundsObservable
    
    @State private var viewport: Viewport = .styleDefault
    @State private var map: MapboxMap? = nil
    
    var body: some View {
//        let center = CLLocationCoordinate2D(latitude: 39.5, longitude: -98.0)
        let styleUri = Bundle.main.object(forInfoDictionaryKey: "MapStyleUri") as! String
        MapReader { proxy in
            Map(
    //            initialViewport: .camera(center: center, zoom: 2, bearing: 0, pitch: 0)
                viewport: $viewport
            ) {
                CircleAnnotationGroup(state.spots, id: \MapSpot.id) { spot in
                    CircleAnnotation(centerCoordinate: CLLocationCoordinate2D(
                        latitude: Double(spot.lat)!,
                        longitude: Double(spot.lon)!
                    )).circleRadius(10.0)
                        .circleColor(UIColor.red)
                }
            }.mapStyle(MapStyle(uri: StyleURI(rawValue: styleUri)!))
                .ornamentOptions(
                    OrnamentOptions(
                        scaleBar: ScaleBarViewOptions(),
                        compass: CompassViewOptions(),
                        logo: LogoViewOptions(),
                        attributionButton: AttributionButtonOptions()
                    )
                )
                .onCameraChanged { cameraChanged in
                    let camera = CameraOptions(cameraState: cameraChanged.cameraState)
                    if let mapUnwrapped = map {
                        let viewPortZoom = mapUnwrapped.coordinateBoundsZoomUnwrapped(for: camera)
                        let viewPort = viewPortZoom.bounds
                        let zoom = viewPortZoom.zoom
                        let n = BDouble(viewPort.north)
                        let s = BDouble(viewPort.south)
                        let rawW = BDouble(viewPort.west)
                        let rawE = BDouble(viewPort.east)
                        let w: BDouble
                        let e: BDouble
                        let bd360 = BDouble(integerLiteral: 360)
                        let minLon = BDouble("-179.999999").unsafelyUnwrapped
                        let maxLon = BDouble(180)
                        if (rawE - rawW >= bd360) {
                            w = minLon
                            e = maxLon
                        } else {
                            w = if (rawW < minLon) {
                                rawW + bd360
                            } else {
                                rawW
                            }
                            e = if (rawE > maxLon) {
                                rawE - bd360
                            } else {
                                rawE
                            }
                        }
                        let currentBounds = MapBounds(
                            n: n.decimalExpansion(precisionAfterDecimalPoint: 6),
                            e: e.decimalExpansion(precisionAfterDecimalPoint: 6),
                            s: s.decimalExpansion(precisionAfterDecimalPoint: 6),
                            w: w.decimalExpansion(precisionAfterDecimalPoint: 6)
                        )
                        bounds.boundsZoom = BoundsZoom(bounds: currentBounds, zoom: zoom)
                    }
                }
                .onAppear {
                    map = proxy.map
//                     let configuration = Puck2DConfiguration.makeDefault(showBearing: true)
//                     proxy.location?.options.puckType = .puck2D(configuration)
                }
        }
    }
}

class MapViewController: MapMapViewController {
    private var sink: Any? = nil
    private var onScroll: ((MapBounds, KotlinDouble) -> Void)? = nil
    
    func invoke(initialEvent: MapMapUpdateEvent, updateListener: Core_uiComposeUpdateListener<MapMapUpdateEvent>) -> UIViewController {
        let state = MapState(spots: initialEvent.spots)
        let bounds = BoundsObservable(boundsZoom: nil)
        onScroll = initialEvent.onScroll
        sink = bounds.$boundsZoom.sink { boundsZoom in
            if let unwrapped = boundsZoom {
                if let scrollUnwrapped = self.onScroll {
                    scrollUnwrapped(unwrapped.bounds, KotlinDouble(value: unwrapped.zoom))
                }
            }
        }
        
        let mapView = MapView(state: state, bounds: bounds)
        updateListener.listen { mapUpdateEvent in
            state.spots = mapUpdateEvent!.spots
            self.onScroll = mapUpdateEvent!.onScroll
        }
        return UIHostingController(rootView: mapView.ignoresSafeArea())
    }
}
