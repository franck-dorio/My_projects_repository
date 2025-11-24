import 'package:flutter/material.dart';
import 'package:my_app/models/model.sport.dart';

class WidgetSportPres extends StatelessWidget {
  final sport sport_pres;
  final Function setfavorissport;
  final Function onAction;

  WidgetSportPres(
      {required this.sport_pres,
      required this.setfavorissport,
      required this.onAction});

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Padding(
            padding: EdgeInsets.all(5),
            child: InkWell(
              borderRadius: BorderRadius.circular(20),
              onTap: () => onAction(sport_pres.id),
              child: Container(
                height: 120,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(20),
                  image: DecorationImage(
                      image: AssetImage(sport_pres.photo_sport),
                      fit: BoxFit.cover),
                ),
              ),
            )),
        Positioned(
          top: 10,
          right: 10,
          child: IconButton(
            icon: Icon(
              sport_pres.favoris
                  ? Icons.favorite
                  : Icons.favorite_border, // Cœur plein si favoris est true
              color: sport_pres.favoris
                  ? Colors.red
                  : Colors.white, // Rouge si favoris est true
            ),
            onPressed: () => setfavorissport(
                sport_pres.id), // Passe la fonction correctement
            iconSize: 35,
          ),
        ),
      ],
    );
  }
}
