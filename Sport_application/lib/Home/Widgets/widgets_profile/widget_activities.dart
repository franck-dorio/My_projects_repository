import 'package:flutter/material.dart';

class WidgetActivities extends StatelessWidget {
  const WidgetActivities({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 150,
      color: Colors.grey.withOpacity(0.2),
      alignment: Alignment.topCenter, // Aligner le contenu en haut
      padding: EdgeInsets.only(top: 10), // Ajouter un espace en haut
      child: Text("Mes activités"),
    );
  }
}
