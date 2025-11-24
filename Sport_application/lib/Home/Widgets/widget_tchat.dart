import 'package:flutter/material.dart';
import 'package:my_app/Home/Widgets/Widgets_tchat/Widget_conversation_pres.dart';
import 'package:my_app/Data/data_tchat.dart' as data_tchat;
import 'package:my_app/Home/Widgets/Widgets_tchat/Widget_group_pres.dart';
import 'package:my_app/Home/Widgets/Widgets_tchat/widget_entete.dart';
import 'package:my_app/models/model.conversation.dart';

class WidgetTchat extends StatefulWidget {
  final void Function(int) Switch_index_0;
  final String initial_page;

  WidgetTchat({
    super.key,
    required this.Switch_index_0,
    required this.initial_page,
  });

  @override
  State<WidgetTchat> createState() => _WidgetTchatState();
}

class _WidgetTchatState extends State<WidgetTchat> {
  late String page;
  final List<conversation> convs =
      data_tchat.dialogues.where((c) => c.inscrit).toList();

  @override
  void initState() {
    super.initState();
    page = widget.initial_page;
  }

  void _change_page(String new_page) {
    setState(() {
      page = new_page;
    });
  }

  @override
  Widget build(BuildContext context) {
    return page == "pres"
        ? Scaffold(
            appBar: AppBar(
              centerTitle: true,
              title: Text("messagerie"),
              leading: Padding(
                padding: EdgeInsets.all(15),
                child: IconButton(
                  onPressed: () {
                    widget.Switch_index_0(0);
                  },
                  icon: Icon(Icons.chevron_left),
                ),
              ),
            ),
            body: ListView.builder(
              itemCount: convs.length,
              itemBuilder: (context, index) {
                var conv = convs[index];
                return GestureDetector(
                  onTap: () {
                    conv.oppended = true;
                    _change_page(conv.id);
                  },
                  child: Column(
                    children: [
                      conv is group
                          ? WidgetGroupPres(conv: conv)
                          : WidgetConversationPres(conv: conv),
                      SizedBox(height: 10),
                    ],
                  ),
                );
              },
            ),
          )
        : Container(
            height: double.infinity,
            child: () {
              final conv = convs.firstWhere((c) => c.id == page);
              return conv is group
                  ? WidgetEnteteGroup(
                      conv: conv,
                      changePage: () {
                        _change_page("pres");
                      },
                    )
                  : WidgetEnteteConv(
                      conv: conv,
                      changePage: () {
                        _change_page("pres");
                      },
                    );
            }(),
          );
  }
}
