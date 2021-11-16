import 'package:flutter/material.dart';
import 'package:google_maps_test/petrol_station_widget.dart';
import 'home_widget.dart';


void main() {
  runApp(MaterialApp(
    initialRoute: '/home',
    routes: {
      '/home': (context) => const Home()
    },
  ));
}


