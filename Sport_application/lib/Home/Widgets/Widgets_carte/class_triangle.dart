import 'package:flutter/material.dart';

class TrianglePainter extends CustomPainter {
  final Color color; // Nouveau paramètre pour la couleur

  // Constructeur qui permet de passer la couleur
  TrianglePainter({this.color = Colors.white});

  @override
  void paint(Canvas canvas, Size size) {
    Paint paint = Paint()
      ..color = color; // Utilise la couleur passée en paramètre
    Path path = Path();

    path.moveTo(0, 0); // Point de départ (gauche)
    path.lineTo(size.width / 2, size.height); // Pointe en bas
    path.lineTo(size.width, 0); // Coin droit
    path.close(); // Ferme le triangle

    canvas.drawPath(path, paint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) => false;
}
