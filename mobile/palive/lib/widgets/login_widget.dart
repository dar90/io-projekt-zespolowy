import 'package:flutter/material.dart';
import 'package:google_maps_test/providers/firebase_authentication.dart';
import 'package:provider/provider.dart';

class LoginWidget extends StatefulWidget {
  const LoginWidget({Key? key}) : super(key: key);

  @override
  _LoginWidgetState createState() => _LoginWidgetState();
}

class _LoginWidgetState extends State<LoginWidget> {
  final loginController = TextEditingController();
  final passwordController = TextEditingController();

  @override
  void dispose() {
    loginController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Logowanie'),
        centerTitle: true,
      ),
      body: Column(
        children: [
          TextField(
            controller: loginController,
            decoration: const InputDecoration(
                labelText: 'Adres email'
            ),
          ),
          TextField(
            controller: passwordController,
            obscureText: true,
            decoration: const InputDecoration(
                labelText: 'Hasło'
            ),
          ),
          ElevatedButton(
              onPressed: () async {
                FirebaseAuthentication auth = context.read();
                String email = loginController.text;
                String password = passwordController.text;
                String result = await auth.logInWithEmailAndPassword(email, password);
                if(result == "OK") {
                  Navigator.pop(context);
                }
              },
              child: Text('Zaloguj')
          )
        ],
      ),
    );
  }
}

