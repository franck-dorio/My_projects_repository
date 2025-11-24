import 'package:flutter/material.dart';
import 'package:my_app/Data/data_utilisateurs.dart' as data_utilisateurs;
import 'package:my_app/Home/Widgets/widgets_pour_vous/widget_acitivty_pres.dart';
import 'package:my_app/Home/Widgets/widgets_pour_vous/widget_sport_band.dart';
import 'package:my_app/Home/Widgets/widgets_pour_vous/widget_story_band.dart';
import 'package:my_app/models/model.sport.dart';
import 'package:my_app/models/model.utilisateur.dart';
import 'package:my_app/Data/data_sports.dart' as data_sports;

class WidgetPourVous extends StatefulWidget {
  final List<utilisateur> utilisateurs = data_utilisateurs.utilisateurs;
  final List<sport> sports = List.from(data_sports.sports);

  final void Function(int) Switch_index_5;

  WidgetPourVous({required this.Switch_index_5});

  @override
  State<WidgetPourVous> createState() => _WidgetPourVousState();
}

class _WidgetPourVousState extends State<WidgetPourVous> {
  String page = "home";

  void SelectSportFavori(String id) {
    setState(() {
      for (var sport in data_sports.sports) {
        if (sport.id == id) {
          if (sport.favoris == true) {
            sport.favoris = false;
          } else {
            sport.favoris = true;
          }
          break;
        }
      }
    });
  }

  void SelectPage(String id) {
    setState(() {
      page = id;
    });
  }

  @override
  Widget build(BuildContext context) {
    final List<sport> sortedSports = List.from(data_sports.sports)
      ..sort((a, b) {
        if (a.favoris == b.favoris) return 0;
        return a.favoris ? -1 : 1;
      });
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          "Pour vous",
        ),
        actions: page == "home"
            ? [
                Padding(
                    padding: EdgeInsets.all(10),
                    child: IconButton(
                      onPressed: () {
                        widget.Switch_index_5(5);
                      },
                      icon: Icon(Icons.send),
                    ))
              ]
            : [
                Padding(
                  padding: EdgeInsets.all(10),
                  child: IconButton(
                    onPressed: () => (SelectPage("home")),
                    icon: Icon(
                      Icons.chevron_left,
                      size: 30,
                    ),
                  ),
                )
              ],
      ),
      body: page == "home"
          ? Column(
              children: [
                WidgetStoryBand(utilisateurs: widget.utilisateurs),
                WidgetSportBand(
                  sports: sortedSports,
                  setfavorissport: SelectSportFavori,
                  onAction: SelectPage,
                ),
              ],
            )
          : WidgetAcitivtyPres(
              sport_id: page,
              Switch_index_5: widget.Switch_index_5,
            ),
    );
  }
}
