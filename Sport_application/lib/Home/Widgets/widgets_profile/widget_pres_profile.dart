import 'package:flutter/material.dart';
import 'package:my_app/models/model.profil.dart';

class WidgetPresProfile extends StatelessWidget {
  final profil profile;

  WidgetPresProfile({required this.profile});

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Container(
            padding: EdgeInsets.all(1),
            alignment: Alignment.center,
            child: Text(
              profile.name,
              style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: Text(
              "description",
              style: TextStyle(color: Colors.grey),
            ),
          ),
          Container(
              alignment: Alignment.center,
              padding: EdgeInsets.only(top: 10),
              child: Container(
                alignment: Alignment.center,
                height: 200,
                child: Stack(alignment: Alignment.center, children: [
                  Column(
                    children: [
                      Container(
                        height: 100,
                      ),
                      Container(
                        color: Colors.grey.withOpacity(0.2),
                        height: 100,
                      ),
                    ],
                  ),
                  Positioned(
                    top: 0,
                    child: CircleAvatar(
                      backgroundImage: AssetImage(profile.picture),
                      radius: 90,
                    ),
                  ),
                ]),
              )),
        ],
      ),
    );
  }
}
