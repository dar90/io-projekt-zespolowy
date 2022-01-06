import 'dart:async';
import 'dart:html';
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

  // fuel stations

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

  Future<List<FuelStation>> loadFuelStationsByCity(String city) async {
    Uri url = Uri.parse(API_URL + '/fuelStations/search/city?city=' + city);
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((res) {
                  Map body = jsonDecode(res.body);
                  List stations = body['_embedded']['fuelStations'];
                  return stations.map((e) =>
                      _fetchFuelStation(e)
                  ).toList();
                })
                .onError((error, stackTrace) => []);
  }

  Future<List<FuelStation>> loadFuelStationByCityAndStreet(String city, String street) async {
    Uri url = Uri.parse(API_URL + '/fuelStations/search/street?city=' + city + '&street=' + street);
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
        .then((res) {
          Map body = jsonDecode(res.body);
          List stations = body['_embedded']['fuelStations'];
          return stations.map((e) =>
              _fetchFuelStation(e)
          ).toList();
        })
        .onError((error, stackTrace) => []);
  }

  Future<Uri> addFuelStation(double lat, double lng, String name, String brand, 
                              int? ownerId, String city, String? street, 
                              String plotNumber, Set<String>? services, Url? logoUrl) 
                          async {

    Uri url = Uri.parse(API_URL + '/fuelStations/create');
    String token = await _bearerToken();
    return http.post(url,
                      headers: {HttpHeaders.authorizationHeader: token},
                      body: <String, Object?> {
                        'latitude': lat,
                        'longitude': lng,
                        'name': name,
                        'brand': brand,
                        'ownerId': ownerId,
                        'city': city,
                        'street': street,
                        'plotNumber': plotNumber,
                        'services': services,
                        'logoUrl': logoUrl
                      }
                )
                .then((value) => Uri.parse(value.body));
    
  }

  Future<void> updateFuelStation(int id, int? ownerId, String brand, String name) async {
    Uri url = Uri.parse(API_URL + '/fuelStations/update');
    String token = await _bearerToken();
    return http.put(url,
                    headers: {HttpHeaders.authorizationHeader: token},
                    body: <String, Object?> {
                      'id': id,
                      'ownerId': ownerId,
                      'brand': brand,
                      'name': name
                    }
                  ).then((value) => null);
  }

  // fuel prices

  Future<List<FuelPrice>> loadFuelPricesByStationId(int stationId) async {
    Uri url = Uri.parse(API_URL + '/fuelStations/' + stationId.toString() + '/prices');
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((res) {
                    Map body = jsonDecode(res.body);
                    List prices = body['_embedded']['fuelPrices'];
                    return prices.map((e) => _fetchFuelPrice(e)).toList();
                })
                .onError((error, stackTrace) => []);
  }

  Future<List<FuelPrice>> loadLowestPricesInCity(String city) async {
    Uri url = Uri.parse(API_URL + '/fuelPrices/search/city?city=' + city);
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((res) {
                  Map body = jsonDecode(res.body);
                  List prices = body['_embedded']['fuelPrices'];
                  return prices.map((e) => _fetchFuelPrice(e)).toList();
                })
                .onError((error, stackTrace) => []);
  }

  Future<List<FuelPrice>> loadLowestPricesByFuelType(String fuelType) async {
    Uri url = Uri.parse(API_URL + '/fuelPrices/search/fuelType?fuelType=' + fuelType);
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((res) {
                  Map body = jsonDecode(res.body);
                  List prices = body['_embedded']['fuelPrices'];
                  return prices.map((e) => _fetchFuelPrice(e)).toList();
                })
                .onError((error, stackTrace) => []);
  }
  
  Future<List<FuelPrice>> loadLowestPricesByCityAndType(String city, String type) async {
    Uri url = Uri.parse(API_URL + '/fuelPrices/search/cityAndType?city=' + city + '&fuelType=' + type);
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((res) {
                  Map body = jsonDecode(res.body);
                  List prices = body['_embedded']['fuelPrices'];
                  return prices.map((e) => _fetchFuelPrice(e)).toList();
                })
                .onError((error, stackTrace) => []);
  }

  Future<List<FuelPrice>> loadPricesByStationBrand(String brand) async {
    Uri url = Uri.parse(API_URL + '/fuelPrices/search/brand?brand=' + brand);
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((res) {
                  Map body = jsonDecode(res.body);
                  List prices = body['_embedded']['fuelPrices'];
                  return prices.map((e) => _fetchFuelPrice(e)).toList();
                })
                .onError((error, stackTrace) => []);
  }

  Future<Uri> addFuelPrice(String fuelType, int stationId, double price) async {
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
        .then((value) => Uri.parse(value.body));
  }

  Future<void> updateFuelPrice(int fuelPriceId, String fuelType, int stationId, double price) async {
    Uri url = Uri.parse(API_URL + '/fuelPrices/update');
    String token = await _bearerToken();
    return http.put(url,
                    headers: {HttpHeaders.authorizationHeader: token},
                    body: jsonEncode(<String, Object>{
                      'id': fuelPriceId,
                      'type': fuelType,
                      'stationId': stationId,
                      'price': price
                    }))
                .then((value) => null);
  }

  Future<void> deleteFuelPrice(int fuelPriceId) async {
    Uri url = Uri.parse(API_URL + '/fuelPrices/' + fuelPriceId.toString());
    String token = await _bearerToken();
    return http.delete(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((value) => null);
  }

  Future<Uri> reportFuelPrice(int fuelPriceId) async {
    Uri url = Uri.parse(API_URL + '/reports/' + fuelPriceId.toString());
    String token = await _bearerToken();
    return http.post(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((value) => Uri.parse(value.body));
  }

  // comments

  Future<List<Comment>> loadCommentsByStationId(int stationId) async {
    Uri url = Uri.parse(API_URL + '/fuelStations/' + stationId.toString() + '/comments');
    String token = await _bearerToken();
    return http.get(url, headers: {HttpHeaders.authorizationHeader: token})
                .then((res) {
                  Map body = jsonDecode(res.body);
                  List comments = body['_embedded']['comments'];
                  return comments.map((e) => _fetchComment(e)).toList();
                })
                .onError((error, stackTrace) => []);
  }

  // mapping functions

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

  // auth

  Future<String> _bearerToken() async {
    if(_currentUser == null) {
      return 'Bearer ';
    }
    String token = await _currentUser!.getIdToken();
    return Future.value('Bearer ' + token);
  }

}