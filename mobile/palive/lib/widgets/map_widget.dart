
import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:google_maps_test/models/fuel_station.dart';
import 'package:google_maps_test/widgets/petrol_station_widget.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class MapWidget extends StatefulWidget {
  const MapWidget({Key? key}) : super(key: key);

  @override
  _MapWidgetState createState() => _MapWidgetState();
}

class _MapWidgetState extends State<MapWidget> {

  List<FuelStation> petrolStations = [];

  late GoogleMapController mapController;

  void _onMapCreated(GoogleMapController controller) => mapController = controller;

  Future fetchData(LatLngBounds bounds) async {
    double top = bounds.northeast.latitude;
    double bottom = bounds.southwest.latitude;
    double left = bounds.southwest.longitude;
    double right = bounds.northeast.longitude;
    String searchString = '?top=$top&bottom=$bottom&left=$left&right=$right';
    http.Response res = await http.get(Uri.parse('http://10.0.2.2:8080/api/fuelStations/search/area'+searchString));
    if(res.statusCode == 200) {
      setState(() {
        Map body = jsonDecode(res.body);
        List stations = body['_embedded']['fuelStations'];
        petrolStations = stations.map((e) =>
            FuelStation(
                id: e['id'],
                latitude: e['latitude'],
                longitude: e['longitude'],
                name: e['name'],
                verified: e['verified'],
                logoUrl: e['logoUrl'],
                brand: e['brand'],
                city: e['city'],
                plotNumber: e['plotNumber'],
                street: e['street']
            )
        ).toList();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return GoogleMap(
      onMapCreated: _onMapCreated,
      initialCameraPosition: const CameraPosition(
        target: LatLng(51.220621, 18.5694656),
        zoom: 11.0,
      ),
      myLocationEnabled: true,
      myLocationButtonEnabled: true,
      onCameraIdle: () => mapController.getVisibleRegion().then((value) => fetchData(value)),
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