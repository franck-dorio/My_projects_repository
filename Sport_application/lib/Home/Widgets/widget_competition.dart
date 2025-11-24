import 'package:flutter/material.dart';

class WidgetCompetition extends StatelessWidget {
  const WidgetCompetition({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          centerTitle: true,
          title: Text("Compétition"),
        ),
        body: Container(
          decoration: BoxDecoration(
            image: DecorationImage(
                image:
                    AssetImage("assets/data_competition/new_competition.png"),
                fit: BoxFit.cover),
          ),
        ));
  }
}
