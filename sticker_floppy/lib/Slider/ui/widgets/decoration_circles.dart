import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class DecorationCircles extends StatelessWidget {
  final String image;
  const DecorationCircles({
    Key? key,
    required this.size,
    required this.image,
  }) : super(key: key);

  final Size size;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Stack(
        alignment: Alignment.center,
        children: [
          Container(
            width: size.width * 0.75,
            height: size.height * 0.75,
            decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.1), shape: BoxShape.circle),
          ),
          Container(
            width: size.width * 0.5,
            height: size.height * 0.5,
            decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.3), shape: BoxShape.circle),
          ),
          Container(
            width: size.width * 0.6,
            height: size.height * 0.6,
            child: Image.asset(image),
          )
        ],
      ),
    );
  }
}
