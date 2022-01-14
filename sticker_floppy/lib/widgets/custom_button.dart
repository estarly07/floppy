import 'package:flutter/cupertino.dart';

class CustomButton extends StatelessWidget {
  final String text;
  final double width;
  CustomButton({
    required this.texColor,
    required this.background,
    required this.text,
    required this.width,
  });

  final Color texColor;
  final Color background;

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.all(8.0),
      alignment: Alignment.center,
      width: width,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Text(text,
            style: TextStyle(color: texColor, fontWeight: FontWeight.bold)),
      ),
      decoration: BoxDecoration(
          color: background, borderRadius: BorderRadius.circular(100)),
    );
  }
}
