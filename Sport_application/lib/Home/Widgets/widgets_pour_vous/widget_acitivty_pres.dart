import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:my_app/Data/data_activities.dart' as data_activitess;
import 'package:my_app/Data/data_sports.dart';
import 'package:my_app/Data/data_utilisateurs.dart';
import 'package:my_app/Home/Widgets/widget_pup_up.dart';
import 'package:my_app/models/model.activity.dart';
import 'package:my_app/models/model.sport.dart';
import 'package:my_app/models/model.utilisateur.dart';

class WidgetAcitivtyPres extends StatelessWidget {
  final String sport_id;
  final void Function(int) Switch_index_5;

  WidgetAcitivtyPres({required this.sport_id, required this.Switch_index_5});

  @override
  void _showPopup(
      BuildContext context, activity activity, utilisateur user, sport sport) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return WidgetPupUp(
          context: context,
          acti: activity,
          user: user,
          this_sport: sport,
          Switch_index_5: Switch_index_5,
        );
      },
    );
  }

  Widget build(BuildContext context) {
    final List<activity> activities = data_activitess.activities
        .where((act) => act.id_sport == sport_id)
        .toList();
    return Expanded(
      child: Padding(
        padding: EdgeInsets.all(5),
        child: ListView.builder(
          scrollDirection: Axis.vertical,
          itemCount: activities.length,
          itemBuilder: (context, index) {
            var activity = activities[index];
            var user =
                utilisateurs.firstWhere((u) => u.name == activity.name_user);
            var sport = sports.firstWhere((s) => s.id == sport_id);
            return Padding(
              padding: EdgeInsets.all(5),
              child: Column(
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      CircleAvatar(
                        backgroundImage: AssetImage(user.profile_picture),
                        radius: 40,
                      ),
                      Container(
                          width: 170,
                          child: AutoSizeText(
                            activity.activity_name,
                            style: TextStyle(
                              fontSize: 30,
                              fontWeight: FontWeight.bold,
                            ),
                            maxLines: 1, // ou plus selon ton besoin
                            minFontSize: 12, // taille minimale autorisée
                            overflow: TextOverflow.ellipsis,
                          )),
                      IconTheme(
                        data: IconThemeData(size: 50),
                        child: sport.icon,
                      ),
                    ],
                  ),
                  SizedBox(height: 10),
                  Container(
                    decoration: BoxDecoration(
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.4),
                          blurRadius: 12,
                          offset: Offset(0, 4),
                        ),
                      ],
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: ClipRRect(
                        borderRadius: BorderRadius.circular(20),
                        child: Stack(
                          children: [
                            Image.asset(
                              activity.publication,
                              width: double.infinity,
                              fit: BoxFit.cover,
                            ),
                            Positioned(
                              bottom: 25,
                              right: 25,
                              child: CircleAvatar(
                                backgroundColor: Colors.blue,
                                radius: 50,
                                child: GestureDetector(
                                  onTap: () {
                                    _showPopup(context, activity, user, sport);
                                  },
                                  child: Icon(
                                    Icons.arrow_circle_right,
                                    color: Colors.white,
                                    size: 90,
                                  ),
                                ),
                              ),
                            ),
                            Positioned(
                              top: 15,
                              right: 15,
                              child: Container(
                                height: 50,
                                width: 120,
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(25),
                                    color: Colors.white),
                                child: Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceEvenly,
                                  children: [
                                    Text("J'aime",
                                        style: TextStyle(
                                            fontWeight: FontWeight.w500,
                                            fontSize: 20)),
                                    Icon(
                                      Icons.favorite,
                                      color: Colors.black,
                                    )
                                  ],
                                ),
                              ),
                            )
                          ],
                        )),
                  ),
                  Text(
                    "${user.name} vous propose:",
                    style: TextStyle(
                      fontSize: 20,
                    ),
                  ),
                  SizedBox(
                    height: 10,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Text(
                        activity.desc_activity,
                        style: TextStyle(fontSize: 20),
                      ),
                      Icon(Icons.person),
                      Text(
                        "${activity.participant}/${activity.joeurs}",
                        style: TextStyle(fontSize: 20),
                      ),
                    ],
                  ),
                  if (index < activities.length - 1)
                    Divider(
                      color: Colors.grey[300],
                      thickness: 2,
                      indent: 10,
                      endIndent: 10,
                    ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }
}
