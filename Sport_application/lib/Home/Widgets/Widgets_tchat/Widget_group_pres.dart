import 'package:flutter/material.dart';
import 'package:my_app/models/model.activity.dart';
import 'package:my_app/models/model.conversation.dart';
import 'package:my_app/Data/data_activities.dart';
import 'package:my_app/Data/data_sports.dart';
import 'package:my_app/models/model.sport.dart';

class WidgetGroupPres extends StatelessWidget {
  final group conv;
  const WidgetGroupPres({super.key, required this.conv});

  activity get acti => activities.firstWhere((u) => u.id == conv.acitivity_id);
  sport get acti_sport => sports.firstWhere((u) => u.id == acti.id_sport);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 80,
      width: double.infinity,
      child: Row(
        children: [
          Padding(
              padding: const EdgeInsets.only(left: 15),
              child: Icon(acti_sport.icon.icon, size: 80)),
          SizedBox(
            width: 10,
          ),
          Expanded(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  "GP : ${acti.activity_name}",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 25,
                  ),
                ),
                Text(conv.dialogues[conv.dialogues.length - 1].content,
                    overflow: TextOverflow.ellipsis,
                    maxLines: 1,
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.grey,
                    )),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(right: 15),
            child: CircleAvatar(
              radius: 5,
              backgroundColor:
                  conv.oppended == true ? Colors.grey : Colors.blue,
            ),
          )
        ],
      ),
    );
  }
}
