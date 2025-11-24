import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:latlong2/latlong.dart';
import 'package:my_app/Home/Widgets/Widgets_carte/widget_puce.dart';
import 'package:my_app/models/model.activity.dart';

import 'package:my_app/Data/data_activities.dart' as data_activitess;
import 'package:my_app/Data/data_sports.dart' as data_sports;
import 'package:my_app/models/model.sport.dart';

class WidgetCarte extends StatefulWidget {
  static const String routeName = "/google-map";
  final List<activity> activities = data_activitess.activities;
  final List<sport> sports = data_sports.sports;
  final void Function(int) Switch_index_5;

  WidgetCarte({required this.Switch_index_5});

  Color getColorBySportId(String sportId) {
    return sports
        .firstWhere((sport) => sport.id == sportId,
            orElse: () => sport(
                id: "",
                name: "",
                photo_sport: "",
                favoris: false,
                icon: Icon(Icons.error),
                color: Colors.grey) // Valeur par défaut
            )
        .color;
  }

  @override
  _WidgetCarteState createState() => _WidgetCarteState();
}

class _WidgetCarteState extends State<WidgetCarte> {
  int? selectedMarkerIndex; // Stocke l'index du marqueur sélectionné

  @override
  Widget build(BuildContext context) {
    // On copie la liste pour ne pas modifier l'originale
    List<int> sortedIndexes = List.generate(widget.activities.length, (i) => i);

    // Si un marqueur est sélectionné, on le met à la fin
    if (selectedMarkerIndex != null) {
      sortedIndexes.remove(selectedMarkerIndex);
      sortedIndexes.add(selectedMarkerIndex!);
    }

    return Scaffold(
      appBar: AppBar(title: Text("Carte Offline - Lyon")),
      body: FlutterMap(
        options: MapOptions(
          center: LatLng(45.763811, 4.840052), // Coordonnées de Lyon
          zoom: 13,
          interactiveFlags: InteractiveFlag.drag | InteractiveFlag.pinchZoom,
        ),
        children: [
          TileLayer(
            urlTemplate: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
            subdomains: ['a', 'b', 'c'],
          ),
          MarkerLayer(
            markers: sortedIndexes.map((index) {
              bool isSelected = selectedMarkerIndex == index;
              return Marker(
                width: 180,
                height: 200,
                point: widget.activities[index].location,
                alignment: Alignment(0.0, -0.4),
                child: GestureDetector(
                  onTap: () {
                    setState(() {
                      selectedMarkerIndex =
                          (selectedMarkerIndex == index) ? null : index;
                    });
                  },
                  child: Stack(
                    alignment: Alignment.center,
                    children: [
                      if (isSelected)
                        Positioned(
                          bottom: 62,
                          child: WidgetPuce(
                            acti: widget.activities[index],
                            Switch_index_5: widget.Switch_index_5,
                          ),
                        ),
                      if (!isSelected)
                        Icon(
                          Icons.location_pin,
                          color: widget.getColorBySportId(
                              widget.activities[index].id_sport),
                          size: 80,
                        ),
                    ],
                  ),
                ),
              );
            }).toList(),
          )
        ],
      ),
    );
  }
}
