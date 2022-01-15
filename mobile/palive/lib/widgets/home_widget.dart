import 'package:flutter/material.dart';
import 'package:google_maps_test/widgets/menu_button_widget.dart';
import 'map_widget.dart';

class Home extends StatelessWidget {
  const Home({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: MapWidget(),
      floatingActionButton: MenuButton(),
    );
  }
}