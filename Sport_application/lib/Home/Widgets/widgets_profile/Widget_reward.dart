import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class WidgetReward extends StatelessWidget {
  const WidgetReward({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
        child: NavigationBar(
      height: 50,
      backgroundColor: Colors.orange,
      destinations: [
        Icon(Icons.emoji_events, color: Colors.white),
        Icon(FontAwesomeIcons.medal, color: Colors.white),
        Icon(Icons.flag, color: Colors.white),
      ],
    ));
  }
}
