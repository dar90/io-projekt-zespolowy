import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';

class FirebaseAuthentication {
  final FirebaseAuth _firebaseAuth;
  final _googleSignIn = GoogleSignIn();

  FirebaseAuthentication(this._firebaseAuth);

  Stream<User?> get currentUser => _firebaseAuth.idTokenChanges();

  Future<String> logInWithEmailAndPassword(String email, String password) async {
    try {
      await _firebaseAuth.signInWithEmailAndPassword(email: email, password: password);
      return "OK";
    } on FirebaseAuthException catch(e) {
      return e.message ?? "no error message";
    }
  }

  Future<String> logInWithGoogle() async {
    final googleUser = await _googleSignIn.signIn();
    if(googleUser == null) {
      return "null google user";
    }
    final googleAuth = await googleUser.authentication;
    final credential = GoogleAuthProvider.credential(
      accessToken: googleAuth.accessToken,
      idToken: googleAuth.idToken
    );

    try {
      _firebaseAuth.signInWithCredential(credential);
    } on FirebaseAuthException catch(e) {
      return e.message ?? "firebase auth error: no error message";
    }
    return "OK";
  }
}