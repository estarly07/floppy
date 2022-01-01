import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:sticker_floppy/widgets/widgets.dart';

class CardCollection extends StatelessWidget {
  final Color color;
  CardCollection({Key? key, required this.color}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var gradientTop = LinearGradient(
        begin: Alignment.topLeft,
        end: Alignment.bottomRight,
        stops: [0.01, 0.6],
        colors: [Colors.white, color]);

    var gradientBottom = LinearGradient(
        begin: Alignment.topRight,
        end: Alignment.bottomLeft,
        stops: [0.2, 0.35],
        colors: [Colors.white, color]);

    final size = MediaQuery.of(context).size;
    return Container(
      width: size.width * 0.4,
      margin: EdgeInsets.symmetric(horizontal: size.width * 0.02),
      child: ClipRRect(
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(size.width * 0.02),
          topRight: Radius.circular(size.width * 0.1),
          bottomRight: Radius.circular(size.width * 0.02),
          bottomLeft: Radius.circular(size.width * 0.1),
        ),
        child: Container(
          decoration: BoxDecoration(
            color: color.withOpacity(0.7),
          ),
          child: Stack(
            children: [
              Positioned(
                  top: size.height * -0.2,
                  left: 10,
                  right: size.height * -0.1,
                  bottom: 0,
                  child:
                      Circle(width: size.width * 0.7, gradient: gradientTop)),
              Positioned(
                  top: 0,
                  left: size.height * -0.3,
                  right: 0,
                  bottom: size.height * -0.7,
                  child: Circle(
                      width: size.width * 0.1, gradient: gradientBottom)),
              _Body(size: size, texColor: color, background: Colors.white),
            ],
          ),
        ),
      ),
    );
  }
}

class _Body extends StatelessWidget {
  final Color background, texColor;
  const _Body({
    Key? key,
    required this.size,
    required this.background,
    required this.texColor,
  }) : super(key: key);

  final Size size;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(size.width * 0.01),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          FadeInImage(
              height: size.height * 0.2,
              width: size.height * 0.2,
              fit: BoxFit.contain,
              placeholder: AssetImage('assets/no-image.jpg'),
              image: NetworkImage(
                  "https://stickershop.line-scdn.net/stickershop/v1/product/8894057/LINEStorePC/main.png;compress=true")),
          Container(
            height: 15,
          ),
          Text(
            "Monkey",
            maxLines: 2,
            style: TextStyle(
                fontSize: size.height * 0.025,
                fontWeight: FontWeight.bold,
                color: Colors.white),
          ),
          Container(
              child: Align(
                  alignment: Alignment.centerRight,
                  child: CustomButton(
                      size: size, texColor: texColor, background: background)))
        ],
      ),
    );
  }
}
