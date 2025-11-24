import 'package:my_app/models/model.activity.dart';
import 'package:my_app/models/model.sport.dart';

class profil {
  String name;
  String picture;
  List<sport> favoritsport;
  List<activity> my_activities;

  profil({
    required this.name,
    required this.picture,
    required this.favoritsport,
    required this.my_activities,
  });
}
