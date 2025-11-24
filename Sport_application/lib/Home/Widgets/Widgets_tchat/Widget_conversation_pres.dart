import 'package:flutter/material.dart';
import 'package:my_app/models/model.conversation.dart';
import 'package:my_app/models/model.utilisateur.dart';
import 'package:my_app/Data/data_utilisateurs.dart' as data_utilisateurs;

class WidgetConversationPres extends StatelessWidget {
  final conversation conv;
  const WidgetConversationPres({super.key, required this.conv});

  utilisateur get user => data_utilisateurs.utilisateurs
      .firstWhere((u) => u.name == conv.principale);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 80,
      width: double.infinity,
      child: Row(
        children: [
          Padding(
            padding: const EdgeInsets.only(left: 15),
            child: CircleAvatar(
              backgroundImage: AssetImage(user.profile_picture),
              radius: 40,
            ),
          ),
          SizedBox(
            width: 10,
          ),
          Expanded(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  conv.principale,
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
