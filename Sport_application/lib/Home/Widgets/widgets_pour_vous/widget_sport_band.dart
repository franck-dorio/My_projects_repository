import 'package:flutter/material.dart';
import 'package:my_app/Home/Widgets/widgets_pour_vous/widget_sport_pres.dart';
import 'package:my_app/models/model.sport.dart';

class WidgetSportBand extends StatelessWidget {
  final List<sport> sports;
  final Function setfavorissport;
  final Function onAction;

  WidgetSportBand(
      {required this.sports,
      required this.setfavorissport,
      required this.onAction});

  @override
  Widget build(BuildContext context) {
    return Expanded(
        child: Padding(
      padding: EdgeInsets.all(10),
      child: SizedBox(
          height: 150,
          child: ListView.builder(
            scrollDirection: Axis.vertical,
            itemCount: sports.length,
            itemBuilder: (context, index) {
              var sport = sports[index];
              return WidgetSportPres(
                sport_pres: sport,
                setfavorissport: setfavorissport,
                onAction: onAction,
              );
            },
          )),
    ));
  }
}
