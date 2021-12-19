import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:google_maps_test/models/fuel_station.dart';
import 'package:google_maps_test/providers/palive_api.dart';
import 'package:google_maps_test/widgets/petrol_station_widget.dart';
import 'package:provider/provider.dart';

class MapWidget extends StatefulWidget {
  const MapWidget({Key? key}) : super(key: key);

  @override
  _MapWidgetState createState() => _MapWidgetState();
}

class _MapWidgetState extends State<MapWidget> {

  List<FuelStation> petrolStations = [];

  late GoogleMapController mapController;

  void _onMapCreated(GoogleMapController controller) => mapController = controller;

  @override
  Widget build(BuildContext context) {
    PaliveAPI api = context.read();
    return GoogleMap(
      onMapCreated: _onMapCreated,
      initialCameraPosition: const CameraPosition(
        target: LatLng(51.220621, 18.5694656),
        zoom: 11.0,
      ),
      myLocationEnabled: true,
      myLocationButtonEnabled: true,
      onCameraIdle: () => mapController.getVisibleRegion().then((area) {
        api.loadFuelStationsByArea(area).then((value) {
          setState(() {
            petrolStations = value;
          });
        });
      }),
      zoomControlsEnabled: true,
      markers: petrolStations
                  .map<Marker>(
                    (petrolStation) => Marker(
                        markerId: MarkerId('${petrolStation.id}'),
                        position: LatLng(
                          petrolStation.latitude,
                          petrolStation.longitude
                        ),
                        onTap: () => Navigator.of(context).push(
                          MaterialPageRoute(builder: (context) =>
                              PetrolStationWidget(petrolStation: petrolStation)
                          )
                        )
                    )
                  )
                  .toSet(),
    );
  }
}