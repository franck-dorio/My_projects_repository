import 'package:flutter/material.dart';
import 'package:my_app/models/model.sport.dart';

class WidgetSportsFav extends StatelessWidget {
  final List<sport> sports;
  final Function delete_favorite;

  WidgetSportsFav({required this.sports, required this.delete_favorite});

  List<sport> getFavoriteSports(List<sport> sports) {
    return sports.where((sport) => sport.favoris == true).toList();
  }

  @override
  Widget build(BuildContext context) {
    List<sport> favorit_sports = getFavoriteSports(sports);

    return Expanded(
      child: Container(
        color: Colors.blue,
        child: Column(
          children: [
            Padding(
              padding: EdgeInsets.all(8),
              child: Text(
                "Mes sports favoris",
                style: TextStyle(fontSize: 20, color: Colors.white),
              ),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: favorit_sports.length,
                itemBuilder: (context, index) {
                  var sport = favorit_sports[index];
                  return Padding(
                      padding: EdgeInsets.all(2),
                      child: Card(
                        shadowColor: Colors.black,
                        child: ListTile(
                          leading: sport.icon,
                          title: Text(sport.name),
                          trailing: IconButton(
                            icon: Icon(Icons.delete, color: Colors.red),
                            onPressed: () {
                              delete_favorite(sport);
                            },
                          ),
                        ),
                      ));
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
