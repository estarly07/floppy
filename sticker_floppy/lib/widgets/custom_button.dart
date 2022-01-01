import 'package:flutter/cupertino.dart';

class CustomButton extends StatelessWidget {
  CustomButton({
    required this.size,
    required this.texColor,
    required this.background,
  });

  final Size size;
  final Color texColor;
  final Color background;

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.all(8.0),
      alignment: Alignment.center,
      width: size.width * 0.2,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Text("Obtener",
            style: TextStyle(color: texColor, fontWeight: FontWeight.bold)),
      ),
      decoration: BoxDecoration(
          color: background, borderRadius: BorderRadius.circular(100)),
    );
  }
}
