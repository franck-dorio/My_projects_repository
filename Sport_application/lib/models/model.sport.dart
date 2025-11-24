import 'package:flutter/material.dart';

class sport {
  String id;
  String name;
  String photo_sport;
  bool favoris;
  Icon icon;
  Color color;

  sport(
      {required this.name,
      required this.photo_sport,
      required this.id,
      required this.favoris,
      required this.icon,
      required this.color});
}
