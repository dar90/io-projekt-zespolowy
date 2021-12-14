import 'package:flutter/material.dart';
import 'home_widget.dart';


void main() {
  runApp(MaterialApp(
    initialRoute: '/home',
    routes: {
      '/home': (context) => const Home()
    },
  ));
}


