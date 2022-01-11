import 'package:flutter/material.dart';
import 'package:google_maps_test/models/fuel_station.dart';
import 'package:google_maps_test/providers/palive_api.dart';

class PetrolStationWidget extends StatelessWidget {

  final FuelStation petrolStation;

  const PetrolStationWidget ({Key? key, required this.petrolStation}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Informacje o stacji'),
        centerTitle: true,
        backgroundColor: Colors.red,
      ),
      body: Center(
        child: Column(
          children: [
            Row(
              children: [
                const Text('Id stacji: '),
                Text('${petrolStation.id}')
              ],
            ),
            const SizedBox(height: 50),
            Row(
              children: [
                const Text('Nazwa stacji: '),
                Text(petrolStation.name)
              ],
            )
          ],
        ),
      ),
    );
  }
}
