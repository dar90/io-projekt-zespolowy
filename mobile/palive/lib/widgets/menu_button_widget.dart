import 'package:flutter/material.dart';
import 'package:google_maps_test/widgets/login_widget.dart';

class MenuButton extends StatelessWidget {
  const MenuButton({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        ElevatedButton(
          onPressed: () => Navigator.of(context).push(
            MaterialPageRoute(builder: (context) => const LoginWidget())
          ),
          child: const Icon(Icons.list),
          style: ElevatedButton.styleFrom(
              shape: const CircleBorder(),
              padding: const EdgeInsets.all(15)
          ),
        ),
      ],
    );
  }
}
