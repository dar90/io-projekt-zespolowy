import 'package:flutter/material.dart';
import 'package:google_maps_test/petrol_station.dart';

class PetrolStationWidget extends StatelessWidget {

  final PetrolStation petrolStation;

  const PetrolStationWidget ({Key? key, required this.petrolStation}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Informacje o stacji'),
        centerTitle: true,
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
