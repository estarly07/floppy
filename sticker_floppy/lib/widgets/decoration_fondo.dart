import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sticker_floppy/Utils/colors.dart';
import 'package:sticker_floppy/widgets/widgets.dart';

class DecorationFondo extends StatelessWidget {
  DecorationFondo({
    Key? key,
  }) : super(key: key);

  var firstCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[2]]);
  var secondCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[1]]);
  var thirdCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[3]]);
  var fourthCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[0]]);
  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
      child: Stack(
        fit: StackFit.expand,
        children: [
          Positioned(
              top: size.height * -0.1,
              bottom: size.height * 0.05,
              right: size.height * -0.05,
              left: size.height * 0.25,
              child: Circle(width: size.width * 0.25, gradient: firstCircle)),
          Positioned(
              top: size.height * -0.2,
              bottom: size.height * 0.035,
              right: size.height * -0.1,
              left: size.height * 0.38,
              child: Circle(width: size.width * 0.25, gradient: secondCircle)),
          Positioned(
              top: size.height * 0.12,
              bottom: size.height * 0.03,
              right: size.height * 0.065,
              left: size.height * 0.2,
              child: Circle(width: size.width * 0.5, gradient: fourthCircle)),
          Positioned(
              top: size.height * 0.1,
              bottom: size.height * 0.11,
              right: size.height * 0.05,
              left: size.height * 0.2,
              child: Circle(width: size.width * 0.5, gradient: thirdCircle)),
        ],
      ),
    );
  }
}
