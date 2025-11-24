import 'package:flutter/material.dart';
import 'package:my_app/Data/data_activities.dart';
import 'package:my_app/Home/Widgets/Widgets_tchat/widget_dialogue.dart';
import 'package:my_app/models/model.activity.dart';
import 'package:my_app/models/model.conversation.dart';
import 'package:my_app/Data/data_profile.dart';
import 'package:my_app/models/model.profil.dart';
import "package:my_app/Data/data_utilisateurs.dart";

import 'package:my_app/models/model.utilisateur.dart';

class WidgetEnteteConv extends StatefulWidget {
  final conversation conv;
  final VoidCallback changePage;

  const WidgetEnteteConv(
      {super.key, required this.conv, required this.changePage});

  @override
  State<WidgetEnteteConv> createState() => _WidgetDialogueState();
}

class _WidgetDialogueState extends State<WidgetEnteteConv> {
  profil my_profil = david_profil;

  @override
  Widget build(BuildContext context) {
    var user = utilisateurs.firstWhere((u) => u.name == widget.conv.principale);
    return Scaffold(
        appBar: AppBar(
          centerTitle: true,
          title: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
            CircleAvatar(
              backgroundImage: AssetImage(user.profile_picture),
            ),
            SizedBox(
              width: 8,
            ),
            Text(widget.conv.principale),
          ]),
          leading: Padding(
              padding: EdgeInsets.all(15),
              child: IconButton(
                onPressed: widget.changePage,
                icon: Icon(Icons.chevron_left),
              )),
        ),
        body: WidgetDialogue(conv: widget.conv, changePage: widget.changePage));
  }
}

class WidgetEnteteGroup extends StatefulWidget {
  final group conv;
  final VoidCallback changePage;

  const WidgetEnteteGroup(
      {super.key, required this.conv, required this.changePage});

  activity get acti => activities.firstWhere((u) => u.id == conv.acitivity_id);

  @override
  State<WidgetEnteteGroup> createState() => _WidgetDialogueGroup2();
}

class _WidgetDialogueGroup2 extends State<WidgetEnteteGroup> {
  profil my_profil = david_profil;

  @override
  Widget build(BuildContext context) {
    List<utilisateur> users = utilisateurs
        .where((u) => widget.conv.participants.contains(u.name))
        .take(3)
        .toList();

    return Scaffold(
        appBar: AppBar(
          toolbarHeight: 100,
          centerTitle: true,
          title: Column(
            children: [
              Text("Groupe : ${widget.acti.activity_name}"),
              SizedBox(
                height: 10,
              ),
              Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                Wrap(
                  children: users.map((user) {
                    return Padding(
                      padding: EdgeInsets.symmetric(horizontal: 4),
                      child: CircleAvatar(
                        backgroundImage: AssetImage(user.profile_picture),
                      ),
                    );
                  }).toList(),
                ),
                widget.conv.participants.length > 3
                    ? Icon(Icons.add)
                    : SizedBox.shrink()
              ])
            ],
          ),
          leading: Padding(
              padding: EdgeInsets.all(15),
              child: IconButton(
                onPressed: widget.changePage,
                icon: Icon(Icons.chevron_left),
              )),
        ),
        body: WidgetDialogue(conv: widget.conv, changePage: widget.changePage));
  }
}
