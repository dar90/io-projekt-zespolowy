import 'dart:io';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:google_maps_test/models/comment.dart';
import 'package:google_maps_test/models/fuel_price.dart';
import 'package:google_maps_test/models/fuel_station.dart';
import 'package:google_maps_test/providers/firebase_authentication.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class PaliveAPI {
  static const String API_URL = 'http://10.0.2.2:8080/api';

  final FirebaseAuthentication auth;

  User? _currentUser;

  PaliveAPI({required this.auth}) {
    auth.currentUser.listen((event) {
      _currentUser = event;
    });
  }

  Future<List<FuelStation>> loadFuelStationsByArea(LatLngBounds bounds) async {
    double top = bounds.northeast.latitude;
    double bottom = bounds.southwest.latitude;
    double left = bounds.southwest.longitude;
    double right = bounds.northeast.longitude;
    String searchString = '?top=$top&bottom=$bottom&left=$left&right=$right';
    Uri url = Uri.parse(API_URL + '/fuelStations/search/area' + searchString);
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
              .then((res) {
                Map body = jsonDecode(res.body);
                List stations = body['_embedded']['fuelStations'];
                return stations.map((e) =>
                    _fetchFuelStation(e)
                ).toList();
              }).onError((error, stackTrace) => []);
  }

  Future<FuelStation> loadFuelStationById(int id) async {
    Uri url = Uri.parse(API_URL + '/fuelStations/' + id.toString());
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((value) => _fetchFuelStation(value.body))
                .onError((error, stackTrace) => _fetchFuelStation(null));
  }

  Future<FuelPrice> addFuelPrice(String fuelType, int stationId, double price) async {
    Uri url = Uri.parse(API_URL + '/fuelPrices/create');
    String token = await _bearerToken();
    return http.post(
        url,
        body: jsonEncode(<String, Object>{
          'type': fuelType,
          'stationId': stationId,
          'price': price
        }),
        headers: {HttpHeaders.authorizationHeader: token})
        .then((value) => _fetchFuelPrice(value))
        .onError((error, stackTrace) => _fetchFuelPrice(null));
  }

  FuelStation _fetchFuelStation(dynamic e) {
    return FuelStation(
        id: e['id'] ?? -1,
        latitude: e['latitude'] ?? 0.0,
        longitude: e['longitude'] ?? 0.0,
        name: e['name'] ?? '-',
        verified: e['verified'] ?? false,
        services: e['services'] is List<String> ? e['services'] : [],
        logoUrl: e['logoUrl'] ?? '-',
        brand: e['brand'] ?? 'inna',
        city: e['city'] ?? '-',
        plotNumber: e['plotNumber'] ?? '-',
        street: e['street'] ?? '-'
    );
  }

  Comment _fetchComment(dynamic e) {
    return Comment(
        e['id'] ?? -1,
        e['rate'] ?? '-',
        e['content'] ?? '-',
        e['verified'] ?? false
    );
  }

  FuelPrice _fetchFuelPrice(dynamic e) {
    return FuelPrice(
        e['id'] ?? -1,
        e['fuelType'] ?? '-',
        e['reportsQuantity'] ?? 0,
        e['dateTime'] ?? DateTime.now(),
        e['price'] ?? 0.00
    );
  }

  Future<String> _bearerToken() async {
    if(_currentUser == null) {
      return 'Bearer ';
    }
    String token = await _currentUser!.getIdToken();
    return Future.value('Bearer ' + token);
  }

}