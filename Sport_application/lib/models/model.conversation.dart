class conversation {
  String id;
  bool inscrit;
  bool oppended;
  String principale;
  List<message> dialogues;

  conversation(
      {required this.id,
      required this.oppended,
      required this.principale,
      required this.dialogues,
      required this.inscrit});
}

class message {
  String envoyeur;
  String content;

  message({required this.envoyeur, required this.content});
}

class group extends conversation {
  List<String> participants;
  String acitivity_id;

  group(
      {required super.id,
      required super.oppended,
      required super.inscrit,
      required super.principale,
      required super.dialogues,
      required this.participants,
      required this.acitivity_id});
}
