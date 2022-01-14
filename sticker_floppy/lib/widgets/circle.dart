import 'package:flutter/cupertino.dart';

class Circle extends StatelessWidget {
  final double width;
  final Gradient gradient;
  const Circle({Key? key, required this.width, required this.gradient})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      decoration: BoxDecoration(shape: BoxShape.circle, gradient: gradient),
    );
  }
}
