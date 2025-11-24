import 'package:flutter/material.dart';
import 'package:my_app/Home/Widgets/widget_pup_up.dart';
import 'package:my_app/models/model.activity.dart';
import 'class_triangle.dart';
import 'package:my_app/Data/data_sports.dart' as data_sports;
import 'package:my_app/models/model.sport.dart';
import 'package:my_app/Data/data_utilisateurs.dart' as data_utilisateurs;
import 'package:my_app/models/model.utilisateur.dart';

class WidgetPuce extends StatelessWidget {
  final activity acti;
  final void Function(int) Switch_index_5;

  WidgetPuce({super.key, required this.acti, required this.Switch_index_5});

  sport get this_sport =>
      data_sports.sports.firstWhere((s) => s.id == acti.id_sport);

  utilisateur get user => data_utilisateurs.utilisateurs
      .firstWhere((s) => s.name == acti.name_user);

  void _showPopup(
      BuildContext context, activity activity, utilisateur user, sport sport) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return WidgetPupUp(
          context: context,
          acti: acti,
          user: user,
          this_sport: this_sport,
          Switch_index_5: Switch_index_5,
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        Container(
          height: 120,
          width: 180,
          decoration: BoxDecoration(
            color: this_sport.color,
            borderRadius: BorderRadius.circular(10),
          ),
          alignment: Alignment.center,
          child: Padding(
            padding: EdgeInsets.all(3),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      GestureDetector(
                          onTap: () {
                            _showPopup(context, acti, user, this_sport);
                          },
                          child: CircleAvatar(
                              backgroundColor: Colors.blue,
                              radius: 30,
                              child: Icon(
                                Icons.arrow_circle_left,
                                color: Colors.white,
                                size: 50,
                              ))),
                      CircleAvatar(
                        backgroundImage: AssetImage(user.profile_picture),
                        radius: 30,
                      ),
                    ]),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Container(
                      width: 80,
                      child: Text(
                        acti.desc_activity,
                        textAlign: TextAlign.center,
                        style: TextStyle(fontSize: 17, color: Colors.white),
                      ),
                    ),
                    Icon(
                      Icons.person,
                      color: Colors.white,
                    ),
                    Container(
                      width: 35,
                      child: Text(
                        "${acti.participant}/${acti.joeurs}",
                        textAlign: TextAlign.center,
                        style: TextStyle(fontSize: 15, color: Colors.white),
                      ),
                    )
                  ],
                ),
              ],
            ),
          ),
        ),
        CustomPaint(
          size: Size(20, 10), // Taille de la flèche
          painter: TrianglePainter(color: this_sport.color),
        ),
      ],
    );
  }
}
