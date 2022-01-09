import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({Key? key}) : super(key: key);

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  late Timer _timer;

  @override
  void dispose() {
    if (_timer != null) {
      _timer.cancel();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    timer();
    return Scaffold(
        body: Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          Container(
            margin: EdgeInsets.only(bottom: size.height * 0.35),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Container(
                    width: size.width * 0.2,
                    height: size.width * 0.2,
                    child: Image(image: AssetImage("assets/floppy.png")),
                  ),
                  Container(
                    margin: EdgeInsets.only(top: size.height * 0.01),
                    child: Text(
                      "Floppy Sticker",
                      style: TextStyle(
                          letterSpacing: 1,
                          fontWeight: FontWeight.bold,
                          color: Colors.blue,
                          fontSize: size.height * 0.04),
                    ),
                  ),
                ],
              ),
            ),
          ),
          Container(
            margin: EdgeInsets.only(
                bottom: size.height * 0.04, top: size.height * 0.04),
            child: Text(
              "Estarly",
              style: TextStyle(
                  fontFamily: "Hanoman",
                  fontWeight: FontWeight.bold,
                  fontSize: size.height * 0.025),
            ),
          ),
        ],
      ),
    ));
  }

  void timer() {
    const oneDecimal = Duration(seconds: 3);
    _timer = Timer.periodic(oneDecimal, (Timer timer) => nextScreen());
  }

  nextScreen() {
    _timer.cancel();
    Navigator.pushReplacementNamed(context, "/");
  }
}
