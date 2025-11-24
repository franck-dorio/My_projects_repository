import 'package:latlong2/latlong.dart';

class activity {
  String id;
  String desc_activity;
  String id_sport;
  String name_user;
  String publication;
  String activity_name;
  LatLng location;
  int participant;
  int joeurs;

  activity({
    required this.activity_name,
    required this.id,
    required this.desc_activity,
    required this.id_sport,
    required this.name_user,
    required this.publication,
    required this.location,
    required this.participant,
    required this.joeurs,
  });
}
