import 'package:flutter/material.dart';

import 'package:my_app/models/model.conversation.dart';
import 'package:my_app/Data/data_profile.dart';
import 'package:my_app/models/model.profil.dart';
import "package:my_app/Data/data_utilisateurs.dart";
import 'package:my_app/models/model.utilisateur.dart';

class WidgetDialogue extends StatefulWidget {
  final conversation conv;
  final VoidCallback changePage;

  const WidgetDialogue({
    super.key,
    required this.conv,
    required this.changePage,
  });

  @override
  State<WidgetDialogue> createState() => _WidgetDialogueState();
}

class _WidgetDialogueState extends State<WidgetDialogue> {
  profil my_profil = david_profil;
  final TextEditingController _controller = TextEditingController();
  final ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollToTop();
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  void envoie_message(String mess) {
    setState(() {
      widget.conv.dialogues.insert(
        0,
        message(envoyeur: david_profil.name, content: mess),
      );
    });

    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollToTop();
    });
  }

  void _scrollToTop() {
    if (_scrollController.hasClients) {
      _scrollController.animateTo(
        _scrollController.position.minScrollExtent,
        duration: Duration(milliseconds: 300),
        curve: Curves.easeOut,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Expanded(
          child: Align(
            alignment: Alignment.topCenter,
            child: ListView.builder(
              controller: _scrollController,
              padding: EdgeInsets.all(10),
              reverse: true, // liste inversée
              itemCount: widget.conv.dialogues.length,
              itemBuilder: (context, index) {
                var conv_exchanges = widget.conv.dialogues[index];

                utilisateur? user;
                if (conv_exchanges.envoyeur != my_profil.name) {
                  user = utilisateurs.firstWhere(
                    (u) => u.name == conv_exchanges.envoyeur,
                    orElse: () => utilisateur(name: "", profile_picture: ""),
                  );
                }

                // Nouvelle logique : est-ce que le message précédent est du même auteur ?
                final previousIsSameSender = index > 0 &&
                    conv_exchanges.envoyeur ==
                        widget.conv.dialogues[index - 1].envoyeur;

                return conv_exchanges.envoyeur == my_profil.name
                    ? Column(
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.end,
                            children: [
                              Container(
                                constraints: BoxConstraints(maxWidth: 300),
                                padding: EdgeInsets.symmetric(
                                    horizontal: 12, vertical: 8),
                                decoration: BoxDecoration(
                                  color: Colors.orange,
                                  borderRadius: BorderRadius.circular(20),
                                ),
                                child: Text(
                                  conv_exchanges.content,
                                  style: TextStyle(
                                      color: Colors.white, fontSize: 18),
                                ),
                              ),
                              previousIsSameSender
                                  ? SizedBox(width: 30)
                                  : Container(
                                      padding:
                                          EdgeInsets.symmetric(horizontal: 10),
                                      child: CircleAvatar(
                                        backgroundImage:
                                            AssetImage(my_profil.picture),
                                        radius: 23,
                                      ),
                                    ),
                            ],
                          ),
                          SizedBox(height: previousIsSameSender ? 2 : 15),
                        ],
                      )
                    : Column(
                        children: [
                          Row(
                            children: [
                              previousIsSameSender
                                  ? SizedBox(width: 30)
                                  : Container(
                                      padding:
                                          EdgeInsets.symmetric(horizontal: 5),
                                      child: CircleAvatar(
                                        backgroundImage: user == null
                                            ? AssetImage("")
                                            : AssetImage(user.profile_picture),
                                        radius: 23,
                                      ),
                                    ),
                              Container(
                                constraints: BoxConstraints(maxWidth: 300),
                                padding: EdgeInsets.symmetric(
                                    horizontal: 12, vertical: 8),
                                decoration: BoxDecoration(
                                  color: Colors.blue,
                                  borderRadius: BorderRadius.circular(20),
                                ),
                                child: Text(
                                  conv_exchanges.content,
                                  style: TextStyle(
                                      color: Colors.white, fontSize: 18),
                                ),
                              )
                            ],
                          ),
                          SizedBox(height: previousIsSameSender ? 2 : 15),
                        ],
                      );
              },
            ),
          ),
        ),
        Padding(
          padding: EdgeInsets.symmetric(horizontal: 10),
          child: Row(
            children: [
              Expanded(
                child: TextField(
                  controller: _controller,
                  autocorrect: false,
                  enableSuggestions: false,
                  decoration: InputDecoration(
                    filled: true,
                    fillColor: Colors.grey[200],
                    hintText: 'Entrez votre message...',
                    contentPadding:
                        EdgeInsets.symmetric(vertical: 12, horizontal: 16),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(20),
                      borderSide: BorderSide.none,
                    ),
                  ),
                ),
              ),
              IconButton(
                icon: Icon(Icons.send),
                onPressed: () {
                  final message = _controller.text.trim();
                  if (message.isNotEmpty) {
                    envoie_message(message);
                    _controller.clear();
                  }
                },
              ),
            ],
          ),
        ),
        SizedBox(height: 20),
      ],
    );
  }
}
