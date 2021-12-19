import 'package:flutter/material.dart';
import 'package:google_maps_test/providers/palive_api.dart';
import 'package:provider/provider.dart';
import 'home_widget.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import '../providers/firebase_authentication.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(const MainWidget());
}

class MainWidget extends StatelessWidget {

  const MainWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MultiProvider(providers: [
      Provider(create: (_) => FirebaseAuthentication(FirebaseAuth.instance)),
      Provider(create: (context) => PaliveAPI(auth: context.read<FirebaseAuthentication>()))
    ],
    child: MaterialApp(
      initialRoute: '/home',
      routes: {
        '/home': (context) => const Home()
      },
    ),);
  }
}


