import 'package:flutter/material.dart';
import 'package:my_app/models/model.sport.dart';

List<sport> sports = [
  sport(
    id: "s1",
    name: "Basket",
    photo_sport: "assets/sports_presentation/basket_image.jpeg",
    favoris: false,
    icon: Icon(Icons.sports_basketball),
    color: Colors.orange,
  ),
  sport(
      id: "s2",
      name: "Course",
      photo_sport: "assets/sports_presentation/course_image.jpeg",
      favoris: false,
      icon: Icon(Icons.directions_run),
      color: Colors.green),
  sport(
      id: "s3",
      name: "Foot",
      photo_sport: "assets/sports_presentation/foot_image.jpeg",
      favoris: false,
      icon: Icon(Icons.sports_football),
      color: Colors.blue),
  sport(
      id: "s4",
      name: "Musculation",
      photo_sport: "assets/sports_presentation/musculation_image.jpeg",
      favoris: false,
      icon: Icon(Icons.fitness_center),
      color: Colors.black),
  sport(
      id: "s5",
      name: "Escalade",
      photo_sport: "assets/sports_presentation/escalade_image.jpeg",
      favoris: false,
      icon: Icon(Icons.terrain),
      color: Colors.grey),
  sport(
      id: "s6",
      name: "Volley",
      photo_sport: "assets/sports_presentation/volley_image.jpeg",
      favoris: false,
      icon: Icon(Icons.sports_volleyball),
      color: Colors.red),
  sport(
      id: "s7",
      name: "Tennis",
      photo_sport: "assets/sports_presentation/tennis_image.jpeg",
      favoris: false,
      icon: Icon(Icons.sports_volleyball),
      color: const Color.fromARGB(255, 220, 198, 0)),
];
