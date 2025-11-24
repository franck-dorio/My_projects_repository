import 'package:flutter/material.dart';
import 'package:my_app/Home/home.dart';

main() {
  runApp(pe_App());
}

class pe_App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: home(), //Home()
    );
  }
}
