import 'package:flutter/material.dart';
import 'package:my_app/Home/Widgets/widget_carte.dart';
import 'package:my_app/Home/Widgets/widget_competition.dart';
import 'package:my_app/Home/Widgets/widget_pour_vous.dart';
import 'package:my_app/Home/Widgets/widget_profile.dart';
import 'package:my_app/Home/Widgets/widget_tchat.dart';

class home extends StatefulWidget {
  @override
  State<home> createState() => _homeState();
}

class _homeState extends State<home> {
  int index = 0;

  void Switchindex(newindex) {
    setState(() {
      index = newindex;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: index == 0
          ? WidgetPourVous(
              Switch_index_5: Switchindex,
            )
          : index == 1
              ? WidgetCarte(
                  Switch_index_5: Switchindex,
                )
              : index == 2
                  ? WidgetCompetition()
                  : index == 3
                      ? WidgetProfile()
                      : WidgetTchat(
                          initial_page: "pres",
                          Switch_index_0: Switchindex,
                        ),
      bottomNavigationBar: (index <= 3)
          ? BottomNavigationBar(
              type: BottomNavigationBarType.fixed,
              currentIndex: index,
              onTap: Switchindex,
              items: [
                BottomNavigationBarItem(
                  icon: Icon(Icons.loupe),
                  label: "Pour-vous",
                ),
                BottomNavigationBarItem(icon: Icon(Icons.map), label: "Carte"),
                BottomNavigationBarItem(
                    icon: Icon(Icons.emoji_events), label: "Compétition"),
                BottomNavigationBarItem(
                    icon: Icon(Icons.person), label: "Profil")
              ],
            )
          : null,
    );
  }
}
