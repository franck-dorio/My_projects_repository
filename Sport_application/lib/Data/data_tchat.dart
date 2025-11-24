import 'package:my_app/models/model.conversation.dart';

List<conversation> dialogues = [
  conversation(
    inscrit: true,
    id: "c1",
    oppended: false,
    principale: "Ronaldo",
    dialogues: [
      message(
        envoyeur: "Ronaldo",
        content: "Coucou David, chaud pour un petit foot",
      ),
    ],
  ),
  conversation(
    inscrit: true,
    oppended: false,
    id: "c2",
    principale: 'Leon',
    dialogues: [
      message(envoyeur: "David", content: "Merci, t'est un mec en or"),
      message(envoyeur: "Leon", content: "Ouais bien-sur il y a aucun soucis"),
      message(
          envoyeur: "David",
          content:
              "Est-ce que tu pourrais faire une ou deux séances ensemble pour que tu me donnes quelques tips"),
      message(
          envoyeur: "David",
          content:
              "Salut Leon, je voulais me mettre à un nouveau sport et je me suis dis que la natation ca serait pas mal!!!"),
    ],
  ),
  conversation(
    inscrit: true,
    oppended: false,
    id: "c3",
    principale: "Bolt",
    dialogues: [
      message(envoyeur: "Bolt", content: "pret à te prendre une vitesse?"),
      message(envoyeur: "David", content: "On se fait une course sur 100m?"),
    ],
  ),
  group(
    id: "c4",
    oppended: false,
    inscrit: true,
    principale: "Curry",
    dialogues: [
      message(
          envoyeur: "Curry",
          content: "Ducoup tout le monde est chaud basket mardi à 16h?"),
    ],
    participants: ["David", "Curry", "Lebron"],
    acitivity_id: "a1",
  ),
  group(
    id: "c5",
    oppended: false,
    inscrit: false,
    principale: "Ronaldo",
    dialogues: [
      message(
          envoyeur: "Ronaldo",
          content: "Je ramène également un pote qui s'appelle Wendie"),
      message(envoyeur: "Ronaldo", content: "Ouais bien sur sans soucis"),
      message(
          envoyeur: "Janja", content: "par conte je ne suis pas trés forte"),
      message(envoyeur: "Janja", content: "j'habite pas loin et j'ai un balle"),
      message(envoyeur: "Janja", content: "Est-ce que je peux venir?"),
      message(envoyeur: "Mbappe", content: "Ouais je sui super chaud"),
      message(envoyeur: "Ronaldo", content: "un petit city en 5x5?"),
    ],
    participants: ["Ronaldo", "Mbappe", "Janja", "Renard"],
    acitivity_id: "a2",
  ),
  group(
    id: "c6",
    oppended: false,
    inscrit: false,
    principale: "Bolt",
    dialogues: [
      message(
          envoyeur: "Bolt",
          content: "tu es assez odacieux pour me défier, j'aime ca"),
    ],
    participants: ["Bolt"],
    acitivity_id: "a3",
  ),
  group(
    id: "c7",
    oppended: false,
    inscrit: false,
    principale: "Ruth",
    dialogues: [
      message(
          envoyeur: "Ruth",
          content: "je pensais que personne ne serait pret à me suivre"),
    ],
    participants: ["Ruth"],
    acitivity_id: "a4",
  ),
  group(
    id: "c8",
    oppended: false,
    inscrit: false,
    principale: "Serena",
    dialogues: [
      message(envoyeur: "Renard", content: "Grave ! Je prends ma raquette !"),
      message(
          envoyeur: "Serena", content: "Prêtes pour un beau double féminin ?"),
    ],
    participants: ["Serena", "Renard"],
    acitivity_id: "a5",
  ),
  group(
    id: "c9",
    oppended: false,
    inscrit: false,
    principale: "Daweii",
    dialogues: [
      message(envoyeur: "Leon", content: "Let’s go ! J’ai trop hâte"),
      message(
          envoyeur: "Daweii", content: "On bosse le haut du corps ensemble ?"),
    ],
    participants: ["Daweii", "Leon"],
    acitivity_id: "a6",
  ),
  group(
    id: "c10",
    oppended: false,
    inscrit: false,
    principale: "Renard",
    dialogues: [
      message(envoyeur: "Camila", content: "J’espère qu’on gagne !"),
      message(envoyeur: "Ngapeth", content: "Ça va être chaud 🔥"),
      message(envoyeur: "Leon", content: "J’amène mes potes aussi !"),
      message(envoyeur: "Lena", content: "Je prends le ballon !"),
      message(envoyeur: "Renard", content: "RDV sur le city !"),
    ],
    participants: ["Renard", "Lena", "Leon", "Ngapeth", "Camila"],
    acitivity_id: "a7",
  ),
  group(
    id: "c11",
    oppended: false,
    inscrit: false,
    principale: "Camila",
    dialogues: [
      message(envoyeur: "Ngapeth", content: "Je suis là aussi !"),
      message(envoyeur: "Mickael", content: "Toujours partant !"),
      message(envoyeur: "Camila", content: "Un petit 4v4 chill ça vous dit ?"),
    ],
    participants: ["Camila", "Mickael", "Ngapeth"],
    acitivity_id: "a8",
  ),
  group(
    id: "c12",
    oppended: false,
    inscrit: false,
    principale: "Lebron",
    dialogues: [
      message(envoyeur: "Leon", content: "Trop chaud pour ce match"),
      message(envoyeur: "Curry", content: "On va mettre le feu "),
      message(envoyeur: "Renard", content: "Je vais mouiller le maillot"),
      message(envoyeur: "Camila", content: "Hâte de jouer avec vous !"),
      message(envoyeur: "Nadal", content: "Je me prépare déjà !"),
      message(envoyeur: "Lebron", content: "All star game bientôt !"),
    ],
    participants: ["Lebron", "Nadal", "Camila", "Renard", "Curry", "Leon"],
    acitivity_id: "a9",
  ),
  group(
    id: "c13",
    oppended: false,
    inscrit: false,
    principale: "Mbappe",
    dialogues: [
      message(
          envoyeur: "Ngapeth", content: "J’ai sorti mes plus beaux tricks !"),
      message(envoyeur: "Mbappe", content: "On jongle ensemble ?"),
    ],
    participants: ["Mbappe", "Ngapeth"],
    acitivity_id: "a10",
  ),
  group(
    id: "c14",
    oppended: false,
    inscrit: false,
    principale: "Nadal",
    dialogues: [
      message(envoyeur: "Nadal", content: "Match 1v1 pro, tu te sens prêt ?"),
    ],
    participants: ["Nadal"],
    acitivity_id: "a11",
  ),
  group(
    id: "c15",
    oppended: false,
    inscrit: false,
    principale: "Mickael",
    dialogues: [
      message(envoyeur: "Mickael", content: "On escalade ensemble demain ?"),
    ],
    participants: ["Mickael"],
    acitivity_id: "a12",
  ),
  group(
    id: "c16",
    oppended: false,
    inscrit: false,
    principale: "Ngapeth",
    dialogues: [
      message(envoyeur: "Serena", content: "On va se régaler !"),
      message(
          envoyeur: "Janja",
          content: "Carrément, j’apporte les lunettes de soleil "),
      message(
          envoyeur: "Ngapeth", content: "Go pour le 2x2 chill sur le sable ?"),
    ],
    participants: ["Ngapeth", "Janja", "Serena"],
    acitivity_id: "a13",
  ),
  group(
    id: "c17",
    oppended: false,
    inscrit: false,
    principale: "Janja",
    dialogues: [
      message(
          envoyeur: "Janja",
          content: "On va grimper et courir, ça vous tente ?"),
    ],
    participants: ["Janja"],
    acitivity_id: "a14",
  ),
  group(
    id: "c18",
    oppended: false,
    inscrit: false,
    principale: "Lena",
    dialogues: [
      message(envoyeur: "Lena", content: "On muscle le haut ensemble ?"),
    ],
    participants: ["Lena"],
    acitivity_id: "a15",
  ),
];
