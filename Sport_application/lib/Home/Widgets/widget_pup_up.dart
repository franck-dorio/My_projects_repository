import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:my_app/models/model.activity.dart';
import 'package:my_app/models/model.conversation.dart';
import 'package:my_app/models/model.profil.dart';
import 'package:my_app/models/model.sport.dart';
import 'package:my_app/models/model.utilisateur.dart';
import 'package:my_app/Data/data_tchat.dart';
import 'package:my_app/Data/data_profile.dart';

class WidgetPupUp extends StatelessWidget {
  BuildContext context;
  activity acti;
  utilisateur user;
  sport this_sport;
  final void Function(int) Switch_index_5;
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _controller = TextEditingController();
  final profil my_profil = david_profil;

  WidgetPupUp(
      {super.key,
      required this.context,
      required this.acti,
      required this.user,
      required this.this_sport,
      required this.Switch_index_5});

  group get conv {
    // remplace `dialogues` par ta vraie liste de conversations
    return dialogues
        .whereType<group>()
        .firstWhere((g) => g.acitivity_id == acti.id);
  }

  @override
  Widget build(BuildContext context) {
    return Dialog(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(20),
        ),
        child: Wrap(children: [
          Container(
            padding: EdgeInsets.all(20),
            child: Column(
              children: [
                Row(
                  children: [
                    Expanded(
                      child: Align(
                        alignment: Alignment.center,
                        child: Text(
                          "Vous souhaitez suivre cette activité?",
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                    IconButton(
                      icon: Icon(Icons.close),
                      onPressed: () => Navigator.of(context).pop(),
                    ),
                  ],
                ),
                SizedBox(
                  height: 10,
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    CircleAvatar(
                      backgroundImage: AssetImage(user.profile_picture),
                      radius: 30,
                    ),
                    SizedBox(
                      width: 5,
                    ),
                    Expanded(
                      child: AutoSizeText(
                        acti.activity_name,
                        style: TextStyle(
                          fontSize: 25,
                          fontWeight: FontWeight.bold,
                        ),
                        maxLines: 2, // nombre max de lignes
                        minFontSize:
                            12, // taille min avant de passer en ellipsis
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                    IconTheme(
                      data: IconThemeData(size: 40),
                      child: this_sport.icon,
                    ),
                  ],
                ),
                SizedBox(
                  height: 10,
                ),
                Align(
                  alignment: Alignment.topLeft,
                  child: Text(
                    "description :",
                    textAlign: TextAlign.left,
                  ),
                ),
                Text(
                  acti.desc_activity,
                  textAlign: TextAlign.center,
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                ),
                SizedBox(
                  height: 5,
                ),
                Align(
                  alignment: Alignment.topLeft,
                  child: Text("Participant : "),
                ),
                Text(
                  " il y actuellement ${acti.participant}/${acti.joeurs} participants",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                ),
                SizedBox(
                  height: 10,
                ),
                conv.inscrit
                    ? Text(
                        "Vous ête déjà inscrit",
                        style: TextStyle(
                          fontSize: 18,
                          decoration: TextDecoration.underline,
                        ),
                      )
                    : Column(
                        children: [
                          Text(
                            " A combien souhaitez vous participer",
                            style: TextStyle(
                              fontSize: 18,
                              decoration: TextDecoration.underline,
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(16.0),
                            child: Form(
                              key: _formKey,
                              child: Column(
                                children: [
                                  TextFormField(
                                    controller: _controller,
                                    keyboardType: TextInputType.number,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.digitsOnly,
                                    ],
                                    decoration: InputDecoration(
                                      labelText:
                                          "Entrez un nombre (1 à ${acti.joeurs - acti.participant})",
                                      border: OutlineInputBorder(),
                                    ),
                                    validator: (value) {
                                      if (value == null || value.isEmpty) {
                                        return 'Veuillez entrer un chiffre';
                                      }
                                      final number = int.tryParse(value);
                                      if (number == null) {
                                        return 'Ce n\'est pas un chiffre valide';
                                      }
                                      if (number >
                                          acti.joeurs - acti.participant) {
                                        return 'Le nombre doit être ${acti.joeurs - acti.participant} ou moins';
                                      }
                                      return null;
                                    },
                                  ),
                                  SizedBox(height: 20),
                                  ElevatedButton(
                                    onPressed: () {
                                      if (_formKey.currentState!.validate()) {
                                        final number =
                                            int.parse(_controller.text);
                                        conv.inscrit = true;
                                        acti.participant =
                                            acti.participant + number;
                                        conv.participants
                                            .add(david_profil.name);
                                        Switch_index_5(5);

                                        Navigator.of(context).pop();
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(SnackBar(
                                                content: Text(
                                                    'Vous avez rejoint cette activité à $number participants')));
                                      }
                                    },
                                    style: ElevatedButton.styleFrom(
                                      backgroundColor:
                                          Colors.blue, // 👈 fond bleu
                                      foregroundColor:
                                          Colors.white, // 👈 texte blanc
                                      padding: EdgeInsets.symmetric(
                                          horizontal: 24,
                                          vertical: 12), // optionnel
                                      textStyle:
                                          TextStyle(fontSize: 16), // optionnel
                                    ),
                                    child: Text("Valider"),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ],
                      )
              ],
            ),
          ),
        ]));
  }
}
