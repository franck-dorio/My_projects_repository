import 'package:flutter/material.dart';
import 'package:my_app/Home/Widgets/widgets_profile/Widget_reward.dart';
import 'package:my_app/Home/Widgets/widgets_profile/widget_activities.dart';
import 'package:my_app/Home/Widgets/widgets_profile/widget_pres_profile.dart';
import 'package:my_app/Home/Widgets/widgets_profile/widget_sports_fav.dart';
import 'package:my_app/Data/data_profile.dart' as data_profile;
import 'package:my_app/models/model.sport.dart';
import 'package:my_app/Data/data_sports.dart' as data_sports;

class WidgetProfile extends StatefulWidget {
  @override
  State<WidgetProfile> createState() => _WidgetProfileState();
}

class _WidgetProfileState extends State<WidgetProfile> {
  List<sport> sports = data_sports.sports;

  void delete_favorite(sport sport) {
    setState(() {
      sport.favoris = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("Profil"),
        actions: [
          Padding(
            padding: EdgeInsets.all(10),
            child: IconButton(
              onPressed: () {},
              icon: Icon(Icons.settings, size: 30),
            ),
          )
        ],
      ),
      body: Column(
        children: [
          WidgetPresProfile(profile: data_profile.david_profil),
          WidgetReward(),
          WidgetActivities(),
          WidgetSportsFav(
            sports: sports,
            delete_favorite: delete_favorite,
          ),
        ],
      ),
    );
  }
}
