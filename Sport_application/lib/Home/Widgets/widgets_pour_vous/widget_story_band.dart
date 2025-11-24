import 'package:flutter/material.dart';
import 'package:my_app/models/model.utilisateur.dart';

class WidgetStoryBand extends StatelessWidget {
  final List<utilisateur> utilisateurs;

  WidgetStoryBand({required this.utilisateurs});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
        height: 155,
        child: Column(
          children: [
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 30, vertical: 5),
              child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  "Profil",
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: utilisateurs.length,
                scrollDirection: Axis.horizontal,
                itemBuilder: (context, index) {
                  var utilisateur = utilisateurs[index];
                  return Padding(
                    padding: EdgeInsets.all(5),
                    child: Column(
                      children: [
                        CircleAvatar(
                          backgroundImage:
                              AssetImage(utilisateur.profile_picture),
                          radius: 40,
                        ),
                        Text(utilisateur.name)
                      ],
                    ),
                  );
                },
              ),
            ),
          ],
        ));
  }
}
